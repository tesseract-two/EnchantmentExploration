package net.falsetm.enchantment_exploration;

import net.fabricmc.api.ModInitializer;

import net.falsetm.enchantment_exploration.config.EnchantmentExplorationConfig;
import net.falsetm.enchantment_exploration.events.*;
import net.falsetm.enchantment_exploration.mixin.EnchantmentScreenHandlerAccessor;
import net.falsetm.enchantment_exploration.mixin_ducks.EnchantmentHandlerDuck;
import net.falsetm.enchantment_exploration.mixin_ducks.LootTableDuck;
import net.falsetm.enchantment_exploration.util.EnchantmentHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.IdMap;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.core.Registry.*;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.ReloadableServerRegistries;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.ItemEnchantments;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.entity.ChiseledBookShelfBlockEntity;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EnchantmentExploration implements ModInitializer {
	public static final String MOD_ID = "enchantment-exploration";

	// This logger is used to write text to the console and the log file.
	// It is considered best practice to use your mod id as the logger's name.
	// That way, it's clear which mod wrote info, warnings, and errors.
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	private static final Identifier mixerID = Identifier.fromNamespaceAndPath(MOD_ID,"mixer");
	private static final Identifier all_enchantsID = Identifier.fromNamespaceAndPath(MOD_ID,"all_enchants");

	private static final String enchantmentAllowedString = "enchantment-exploration-allowed";

	private static EnchantmentExplorationConfig config;

	public static EnchantmentExplorationConfig getConfig()
	{
		return config;
	}

	//because of dumb mixin stuff, and the fact that it's static, this was the best I could do.
	private static final ThreadLocal<Set<Holder<?>>> registrySkipEntrySet = ThreadLocal.withInitial(HashSet::new);

	public static ThreadLocal<Set<Holder<?>>> getRegistrySkipEntrySet() {
		return registrySkipEntrySet;
	}

	@Override
	public void onInitialize() {
		// This code runs as soon as Minecraft is in a mod-load-ready state.
		// However, some things (like resources) may still be uninitialized.
		// Proceed with mild caution.
		loadConfig();

		//makes the enchantment power provider chiseled bookshelves, on toggle
		IsAccessPowerProviderCallback.EVENT.register((state, key) -> {
			if(config.isEnabled()){
                return state.getBlock() == Blocks.CHISELED_BOOKSHELF;
            }
			return null;
		});

		EnchantmentContentChangedCallback.EVENT.register((receiver, stack, world, pos) -> {
			if(config.isEnabled() && world instanceof ServerLevel serverWorld){
				Map<Holder<Enchantment>, Integer> enchantmentLevelMap = new HashMap<>();
				for (BlockPos offset : EnchantingTableBlock.BOOKSHELF_OFFSETS) {
					//if we can use the block
					if (EnchantingTableBlock.isValidBookShelf(world, pos, offset)) {
						BlockPos shelfPos = new BlockPos(pos).offset(offset);
						if(world.getBlockState(shelfPos).getBlock() == Blocks.CHISELED_BOOKSHELF){
							ChiseledBookShelfBlockEntity bookshelfBlockEntity = serverWorld.getBlockEntity(shelfPos, BlockEntityType.CHISELED_BOOKSHELF).orElse(null);
							if(bookshelfBlockEntity != null){
								for(int i = 0; i < ChiseledBookShelfBlockEntity.MAX_BOOKS_IN_STORAGE; i++){
									ItemStack bookItem = bookshelfBlockEntity.getItem(i);
									if(bookItem.is(Items.ENCHANTED_BOOK)){
										ItemEnchantments enchantmentComponent = net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(bookItem);
										if(enchantmentComponent != null){
											for(Holder<Enchantment> enchantment : enchantmentComponent.keySet()){
												Integer oldLevel = enchantmentLevelMap.getOrDefault(enchantment, null);
												int newLevel = enchantmentComponent.getLevel(enchantment);
												if(oldLevel == null || oldLevel < newLevel){
													enchantmentLevelMap.put(enchantment, newLevel);
												}
											}
										}
									}
								}
							}
						}
					}
				}

				List<EnchantmentInstance> possibleEnchants = new ArrayList<>();
				for(var entry : enchantmentLevelMap.entrySet()){
					possibleEnchants.add(new EnchantmentInstance(entry.getKey(), entry.getValue()));
				}
				possibleEnchants = EnchantmentHelper.legalEnchantments(stack, possibleEnchants);

				((EnchantmentHandlerDuck)receiver).falsetm$SetPossibleEnchants(possibleEnchants);

				if(!possibleEnchants.isEmpty()){
					Registry<Enchantment> registryManager = world.registryAccess().lookupOrThrow(Registries.ENCHANTMENT);
					IdMap<Holder<Enchantment>> indexedIterable = registryManager.asHolderIdMap();

					RandomSource random = ((EnchantmentScreenHandlerAccessor)receiver).getRandom();
					random.setSeed(receiver.getEnchantmentSeed());

					EnchantmentInstance selected = possibleEnchants.get(random.nextInt(possibleEnchants.size()));

					//bump up now if we should show it
					float roll = random.nextFloat();
					int selectedEnchantsLevel = selected.level();
					if(config.shouldShowBumpUp() && roll <= config.getBumpUpChance() && selectedEnchantsLevel < selected.enchantment().value().getMaxLevel()){
						selected = new EnchantmentInstance(selected.enchantment(), selected.level()+1);
					}

					//set the enchantment. Edit this to make it use text for display
					receiver.costs[0] = config.getCost0();
					receiver.enchantClue[0] = indexedIterable.getId(selected.enchantment());
					receiver.levelClue[0] = selected.level();

					int mixerRealID = -1;
					Optional<Holder.Reference<Enchantment>> mixer = registryManager.get(mixerID);
					if(mixer.isPresent()){
						mixerRealID = indexedIterable.getId(mixer.get());
					}
					int allRealID = -1;
					Optional<Holder.Reference<Enchantment>> all = registryManager.get(all_enchantsID);
					if(all.isPresent()){
						allRealID = indexedIterable.getId(all.get());
					}
					receiver.costs[1] = config.getCost1();
					receiver.enchantClue[1] = mixerRealID;
					receiver.levelClue[1] = 1;
					receiver.costs[2] = config.getCost2();
					receiver.enchantClue[2] = allRealID;
					receiver.levelClue[2] = 1;

					receiver.broadcastChanges();
				}
				else{
					//set to nothing if no possible enchantments
					for (int i = 0; i < 3; i++) {
						receiver.costs[i] = 0;
						receiver.enchantClue[i] = -1;
						receiver.levelClue[i] = -1;
					}
				}

				return InteractionResult.FAIL;
			}
			return InteractionResult.PASS;
		});

		GenerateEnchantCallback.EVENT.register((receiver, registryManager, stack, slot, level) -> {
			if(config.isEnabled()){
				List<EnchantmentInstance> possibleEnchants = ((EnchantmentHandlerDuck)receiver).falsetm$GetPossibleEnchants();
				List<EnchantmentInstance> returnEnchants = new ArrayList<>();

				RandomSource random = ((EnchantmentScreenHandlerAccessor)receiver).getRandom();
				random.setSeed(receiver.getEnchantmentSeed() + slot);

				if(possibleEnchants != null && !possibleEnchants.isEmpty()){
					//top slot (SINGLE MAX LEVEL ENCHANT)
					if(slot == 0){
						Optional<Registry<Enchantment>> optional = registryManager.lookup(Registries.ENCHANTMENT);
						if(optional.isPresent()){
							Optional<Holder.Reference<Enchantment>> enchantment = optional.get().get(receiver.enchantClue[0]);
							if(enchantment.isPresent()){

								Enchantment selectedEnchantment = enchantment.get().value();
								float roll = random.nextFloat();
								int selectedEnchantsLevel = receiver.levelClue[0];
								if(!config.shouldShowBumpUp() && roll <= config.getBumpUpChance() && selectedEnchantsLevel < selectedEnchantment.getMaxLevel()){
									selectedEnchantsLevel++;
								}
								EnchantmentInstance outputEnchantmentEntry = new EnchantmentInstance(enchantment.get(), selectedEnchantsLevel);
								returnEnchants.add(outputEnchantmentEntry);
							}
						}
					}
					//middle slot (MIXED UP)
					else if(slot == 1){
						int originalSize = possibleEnchants.size();
						for(int i = 0; i < originalSize; i++) {
							int randomIndex = random.nextInt(possibleEnchants.size());
							EnchantmentInstance entry = possibleEnchants.get(randomIndex);
							possibleEnchants.remove(randomIndex);

							boolean compatibleWithAll = net.minecraft.world.item.enchantment.EnchantmentHelper.isEnchantmentCompatible(returnEnchants.stream().map(EnchantmentInstance::enchantment).collect(Collectors.toList()), entry.enchantment());

							if(compatibleWithAll){
								float roll = random.nextFloat();
								if(returnEnchants.isEmpty() || roll <= config.getAdditionalEnchantmentChance()){
									int curLevel = entry.level();

									if(curLevel > 1){
										roll = random.nextFloat();
										if(roll <= config.getBumpDownChance()){
											curLevel--;
										}
									}
									returnEnchants.add(new EnchantmentInstance(entry.enchantment(), curLevel));
								}
							}
						}
					}
					//bottom slot (ALL ENCHANTS LOWER LEVEL)
					else{
						int originalSize = possibleEnchants.size();
						for(int i = 0; i < originalSize; i++){
							int randomIndex = random.nextInt(possibleEnchants.size());
							EnchantmentInstance entry = possibleEnchants.get(randomIndex);
							possibleEnchants.remove(randomIndex);

							boolean compatibleWithAll = net.minecraft.world.item.enchantment.EnchantmentHelper.isEnchantmentCompatible(returnEnchants.stream().map(EnchantmentInstance::enchantment).collect(Collectors.toList()), entry.enchantment());
							if(compatibleWithAll){
								int curLevel = entry.level();
								if(curLevel > 1){
									returnEnchants.add(new EnchantmentInstance(entry.enchantment(), curLevel-1));
								}
								else{
									returnEnchants.add(entry);
								}
							}
						}
					}
				}
				return returnEnchants;
			}
			return null;
		});

		EnchantmentScreenHandlerApplyCostCallback.EVENT.register((receiver, itemStack, inputLevels) -> {
			if(config.isEnabled()){
				int selected = inputLevels-1;
				if(selected < receiver.costs.length){
					return receiver.costs[selected];
				}
			}
			return inputLevels;
		});

		AnvilScreenHandlerUpdateResultCallback.EVENT.register((receiver -> {
			if(config.isEnabled()){
				ItemStack input1 = receiver.getSlot(AnvilMenu.INPUT_SLOT).getItem();
				ItemStack input2 = receiver.getSlot(AnvilMenu.ADDITIONAL_SLOT).getItem();
				boolean cancelBook = false;
				if(input2.is(Items.ENCHANTED_BOOK)){
					cancelBook = true;
					if((input1.is(Items.ENCHANTED_BOOK) && config.shouldAnvilCombineBookUpgrade())){
						cancelBook = false;
						ItemEnchantments enchants1 = net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(input1);
						ItemEnchantments enchants2 = net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(input2);
						for(var enchantment : enchants1.keySet()){
							if(!enchants2.keySet().contains(enchantment) || enchants1.getLevel(enchantment) != enchants2.getLevel(enchantment)){
								cancelBook = true;
								break;
							}
						}
					}
				}
				if((input2.isDamageableItem() && config.shouldRemoveToolAnvilCombination()) || (config.shouldRemoveBookAnvilCombination() && cancelBook)){
					receiver.setItem(AnvilMenu.RESULT_SLOT, receiver.incrementStateId(), ItemStack.EMPTY);
					//receiver.levelCost.set(0);
					return InteractionResult.FAIL;
				}
			}
			return InteractionResult.PASS;
		}));

		AnvilScreenUpdateResultGetSecondInputCallback.EVENT.register((receiver, inventory) -> {
			if(config.isEnabled()){
				ItemStack book = inventory.getItem(0);
				if(book.is(Items.ENCHANTED_BOOK)){
					ItemStack input2 = inventory.getItem(1);
					String itemID = BuiltInRegistries.ITEM.getKey(input2.getItem()).toString();
					if(config.getAnvilBookUpgradeItems().contains(itemID)){
						ItemEnchantments enchantmentsComponent = net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(book);
						if(!enchantmentsComponent.keySet().isEmpty()){
							ItemStack returnStack = Items.ENCHANTED_BOOK.getDefaultInstance();
							net.minecraft.world.item.enchantment.EnchantmentHelper.setEnchantments(returnStack, enchantmentsComponent);
							return returnStack;
						}
					}
				}
			}
			return null;
		});

		//If you are a developer and are having an issue with compatibility because of this. leave an issue. I'll do something better, but ATM I don't want to deal with this.
		//patching removing the second stack on repair
		AnvilScreenTakeOutputNoRepairClearSecondCallback.EVENT.register((receiver, inventory) -> {
			if(config.isEnabled()){
				ItemStack input2 = inventory.getItem(1);
				if(!input2.isEmpty() && input2.getCount() > 1){
					input2.shrink(1);
					inventory.setItem(1, input2);
				}
				else{
					inventory.setItem(1, ItemStack.EMPTY);
				}
				return InteractionResult.FAIL;
			}
			return InteractionResult.PASS;
		});

		ItemStackCanRepairWithCallback.EVENT.register((receiver, repairStack) -> {
			if(config.isEnabled()){
				var repair = config.getRepairItem(BuiltInRegistries.ITEM.getKey(repairStack.getItem()).toString());
				if(repair != null && repair.containsItem(BuiltInRegistries.ITEM.getKey(receiver.getItem()).toString())){
					return true;
				}
				if(!config.doDefaultRepairMaterialsWork()){
					return false;
				}
			}
			return null;
		});

		//I should redo this function to instead of passing the operation, to return a consumer which will get added to the stack, but seeing as how I'm not using this function twice ATM and that's extra work I'm leaving as is for now
		LootTableApplyFunctionsCallback.EVENT.register((receiver, itemApplier, lootConsumer, context, original) -> {
			if(config.isEnabled()){
				return original.call(itemApplier, generateLootAfterFunctions(receiver, lootConsumer, context), context);
			}
			return null;
		});

		LootTableFinishGenerateUnprocessedCallback.EVENT.register((receiver, context, lootConsumer) -> {
			if(config.isEnabled()){
				for (var entry : config.getLootTableBookPulls().entrySet()) {

					ReloadableServerRegistries.Holder lookup = context.getLevel().getServer().reloadableRegistries();

					Identifier table1ID = Identifier.parse(entry.getKey());
					var attempted = lookup.getLootTable(ResourceKey.create(Registries.LOOT_TABLE, table1ID));

					if (receiver.equals(attempted)) {
						Identifier table2ID = Identifier.parse(entry.getValue());
						var secondaryTable = lookup.getLootTable(ResourceKey.create(Registries.LOOT_TABLE, table2ID));
						if(secondaryTable != null){
							((LootTableDuck) secondaryTable).falsetm$skipMixin();
							secondaryTable.getRandomItemsRaw(context, lootConsumer);
						}
						break;
					}
				}
			}
			return InteractionResult.PASS;
		});

		//Villager trades
		EnchantBookFactoryBeforeGenerateEnchantmentCallback.EVENT.register((receiver, registry, currentPossibleEnchants) -> {
			if(config.isEnabled() && config.shouldDisableVillagerBookTrades()){
				for(String stringEntry : config.getVillagerSkipEnchantments()){
					Identifier id = Identifier.tryParse(stringEntry);
					if(id != null){
						Holder<Enchantment> entry = registry.wrapAsHolder(registry.getValue(id));
						if(entry != null){
							registrySkipEntrySet.get().add(entry);
						}
					}
				}
			}
			return InteractionResult.PASS;
		});

		EnchantBookFactoryAfterGenerateEnchantmentCallback.EVENT.register((receiver, registry, currentPossibleEnchants) -> {
			registrySkipEntrySet.remove();

			return InteractionResult.PASS;
		});

		SellEnchantedToolFactoryCreateCallback.EVENT.register(((receiver, original) -> {
			if(config.isEnabled() && config.shouldDisableVillagerToolTrades()){
				ItemStack itemEnchanted = original.copy();
				ItemEnchantments enchantmentComponent = net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(itemEnchanted);

				net.minecraft.world.item.enchantment.EnchantmentHelper.updateEnchantments(original, components -> components.removeIf(removeEnchant -> true));
				for (Holder<Enchantment> enchantment : enchantmentComponent.keySet()) {
					if (!config.getVillagerSkipEnchantments().contains(enchantment.getRegisteredName())) {
						original.enchant(enchantment, enchantmentComponent.getLevel(enchantment));
					}
				}

				return original;
			}

			return null;
		}));
	}

	public static Consumer<ItemStack> generateLootAfterFunctions(LootTable receiver, Consumer<ItemStack> lootConsumer, LootContext context) {
		return (itemStack) -> {
			if(itemStack.is(Items.ENCHANTED_BOOK)) {
				boolean ignore = false;
				for (String entry : config.getIgnoreTables()) {

					Identifier id = Identifier.parse(entry);
					var attempted = context.getLevel().getServer().reloadableRegistries().getLootTable(ResourceKey.create(Registries.LOOT_TABLE, id));

					if (receiver.equals(attempted)) {
						ignore = true;
						break;
					}
				}
				if (!ignore) {
					ItemStack itemEnchanted = itemStack.copy();
					ItemEnchantments enchantmentComponent = net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(itemEnchanted);

					net.minecraft.world.item.enchantment.EnchantmentHelper.updateEnchantments(itemStack, components -> components.removeIf(removeEnchant -> true));
					for (Holder<Enchantment> enchantment : enchantmentComponent.keySet()) {
						if (!config.getLootTableSkipEnchantments().contains(enchantment.getRegisteredName())) {
							itemStack.enchant(enchantment, enchantmentComponent.getLevel(enchantment));
						}
					}
					if (net.minecraft.world.item.enchantment.EnchantmentHelper.getEnchantmentsForCrafting(itemStack).keySet().isEmpty()) {
						itemStack = ItemStack.EMPTY;
					}
				}
			}
			lootConsumer.accept(itemStack);
		};
	}

	private static void loadConfig() {
		Path configFile = EnchantmentExplorationConfig.CONFIG_FILE;
		if (Files.exists(configFile)) {
			try(BufferedReader reader = Files.newBufferedReader(configFile)) {
				config = EnchantmentExplorationConfig.fromJson(reader);
			} catch (Exception e) {
				LOGGER.error("Error loading Enchantment Exploration config file. Default values will be used for this session.", e);
				config = new EnchantmentExplorationConfig();
			}
		} else {
			config = new EnchantmentExplorationConfig();
		}

		// Immedietly save config to file to update any fields that may have changed.
		config.saveAsync();
	}
}