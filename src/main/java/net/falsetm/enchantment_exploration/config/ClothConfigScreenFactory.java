package net.falsetm.enchantment_exploration.config;

import me.shedaniel.clothconfig2.api.ConfigBuilder;
import me.shedaniel.clothconfig2.api.ConfigCategory;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import net.falsetm.enchantment_exploration.EnchantmentExploration;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public class ClothConfigScreenFactory {
    public static Screen makeConfig(Screen parent) {
        EnchantmentExplorationConfig defaultConfig = new EnchantmentExplorationConfig();

        ConfigBuilder builder = ConfigBuilder.create()
                .setParentScreen(parent)
                .setTitle(Component.translatable("title.enchantment-exploration.config"))
                .setSavingRunnable(EnchantmentExploration.getConfig()::saveAsync);
        ConfigEntryBuilder entryBuilder = builder.entryBuilder();

        ConfigCategory main = builder.getOrCreateCategory(Component.translatable("category.enchantment-exploration.main"));
        ConfigCategory anvil = builder.getOrCreateCategory(Component.translatable("category.enchantment-exploration.anvil"));
        ConfigCategory lootTables = builder.getOrCreateCategory(Component.translatable("category.enchantment-exploration.loot-table"));
        ConfigCategory villager = builder.getOrCreateCategory(Component.translatable("category.enchantment-exploration.villager"));

        main.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.enchantment-exploration.main.enabled"), EnchantmentExploration.getConfig().isEnabled())
                .setDefaultValue(defaultConfig.isEnabled())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.main.enabled"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setEnabled(newValue))
                .build());
        main.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.enchantment-exploration.show-bump"), EnchantmentExploration.getConfig().shouldShowBumpUp())
                .setDefaultValue(defaultConfig.shouldShowBumpUp())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.show-bump"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setShowBumpUp(newValue))
                .build());
        main.addEntry(entryBuilder.startFloatField(Component.translatable("option.enchantment-exploration.chance-up"), EnchantmentExploration.getConfig().getBumpUpChance())
                .setDefaultValue(defaultConfig.getBumpUpChance())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.chance-up"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setBumpUpChance(newValue))
                .build());
        main.addEntry(entryBuilder.startFloatField(Component.translatable("option.enchantment-exploration.chance-enchantments"), EnchantmentExploration.getConfig().getAdditionalEnchantmentChance())
                .setDefaultValue(defaultConfig.getAdditionalEnchantmentChance())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.chance-enchantments"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setAdditionalEnchantmentChance(newValue))
                .build());
        main.addEntry(entryBuilder.startFloatField(Component.translatable("option.enchantment-exploration.chance-down"), EnchantmentExploration.getConfig().getBumpDownChance())
                .setDefaultValue(defaultConfig.getBumpDownChance())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.chance-down"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setBumpDownChance(newValue))
                .build());
        main.addEntry(entryBuilder.startIntField(Component.translatable("option.enchantment-exploration.cost0"), EnchantmentExploration.getConfig().getCost0())
                .setDefaultValue(defaultConfig.getCost0())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.cost0"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setCost0(newValue))
                .build());
        main.addEntry(entryBuilder.startIntField(Component.translatable("option.enchantment-exploration.cost1"), EnchantmentExploration.getConfig().getCost1())
                .setDefaultValue(defaultConfig.getCost1())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.cost1"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setCost1(newValue))
                .build());
        main.addEntry(entryBuilder.startIntField(Component.translatable("option.enchantment-exploration.cost2"), EnchantmentExploration.getConfig().getCost2())
                .setDefaultValue(defaultConfig.getCost2())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.cost2"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setCost2(newValue))
                .build());

        anvil.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.enchantment-exploration.anvil.tool.enabled"), EnchantmentExploration.getConfig().shouldRemoveToolAnvilCombination())
                .setDefaultValue(defaultConfig.shouldRemoveToolAnvilCombination())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.anvil.tool.enabled"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setRemoveToolAnvilCombination(newValue))
                .build());
        anvil.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.enchantment-exploration.anvil.book.enabled"), EnchantmentExploration.getConfig().shouldRemoveBookAnvilCombination())
                .setDefaultValue(defaultConfig.shouldRemoveBookAnvilCombination())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.anvil.book.enabled"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setRemoveBookAnvilCombination(newValue))
                .build());
        anvil.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.enchantment-exploration.anvil.combine.enabled"), EnchantmentExploration.getConfig().shouldAnvilCombineBookUpgrade())
                .setDefaultValue(defaultConfig.shouldAnvilCombineBookUpgrade())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.anvil.combine.enabled"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setAnvilCombineBookUpgrade(newValue))
                .build());
        anvil.addEntry(entryBuilder.startStrList(Component.translatable("option.enchantment-exploration.anvil.upgrade-materials"), EnchantmentExploration.getConfig().getAnvilBookUpgradeItems())
                .setDefaultValue(defaultConfig.getAnvilBookUpgradeItems())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.anvil.upgrade-materials"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setAnvilBookUpgradeItems(newValue))
                .build());
        anvil.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.enchantment-exploration.anvil.vanilla-materials.disabled"), EnchantmentExploration.getConfig().doDefaultRepairMaterialsWork())
                .setDefaultValue(defaultConfig.doDefaultRepairMaterialsWork())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.anvil.vanilla-materials.disabled"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setDefaultRepairMaterialsWork(newValue))
                .build());
        anvil.addEntry(entryBuilder.startStrList(Component.translatable("option.enchantment-exploration.anvil.custom-materials"), EnchantmentExploration.getConfig().getCustomRepairMaterialsAsStringList())
                .setDefaultValue(defaultConfig.getCustomRepairMaterialsAsStringList())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.anvil.custom-materials.1")
                        .append(Component.translatable("tooltip.enchantment-exploration.anvil.custom-materials.2").withStyle(ChatFormatting.YELLOW)))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setCustomRepairMaterialsFromStringList(newValue))
                .build());

        lootTables.addEntry(entryBuilder.startStrList(Component.translatable("option.enchantment-exploration.loot-table.ignore-skip"), EnchantmentExploration.getConfig().getIgnoreTables())
                .setDefaultValue(defaultConfig.getIgnoreTables())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.loot-table.ignore-skip"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setIgnoreTables(newValue))
                .build());
        lootTables.addEntry(entryBuilder.startStrList(Component.translatable("option.enchantment-exploration.loot-table.skip"), EnchantmentExploration.getConfig().getLootTableSkipEnchantmentsList())
                .setDefaultValue(defaultConfig.getLootTableSkipEnchantmentsList())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.loot-table.skip"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setLootTableSkipEnchantmentsFromList(newValue))
                .build());
        lootTables.addEntry(entryBuilder.startStrList(Component.translatable("option.enchantment-exploration.loot-table.book-pulls"), EnchantmentExploration.getConfig().getLootTableBookPullsList())
                .setDefaultValue(defaultConfig.getLootTableBookPullsList())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.loot-table.skip.book-pulls.1")
                        .append(Component.translatable("tooltip.enchantment-exploration.anvil.skip.book-pulls.2").withStyle(ChatFormatting.YELLOW)))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setLootTableBookPullsList(newValue))
                .build());

        villager.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.enchantment-exploration.villager.book.enabled"), EnchantmentExploration.getConfig().shouldDisableVillagerBookTrades())
                .setDefaultValue(defaultConfig.shouldDisableVillagerBookTrades())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.villager.book.enabled")
                        .append(Component.translatable("tooltip.enchantment-exploration.villager.warning").withStyle(ChatFormatting.YELLOW)))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setFilterEnchantmentsVillagerBookTrades(newValue))
                .build());

        villager.addEntry(entryBuilder.startBooleanToggle(Component.translatable("option.enchantment-exploration.villager.tool.enabled"), EnchantmentExploration.getConfig().shouldDisableVillagerToolTrades())
                .setDefaultValue(defaultConfig.shouldDisableVillagerToolTrades())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.villager.tool.enabled")
                        .append(Component.translatable("tooltip.enchantment-exploration.villager.warning").withStyle(ChatFormatting.YELLOW)))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setFilterEnchantmentsVillagerToolTrades(newValue))
                .build());

        villager.addEntry(entryBuilder.startStrList(Component.translatable("option.enchantment-exploration.villager.filter-list"), EnchantmentExploration.getConfig().getVillagerSkipEnchantmentsList())
                .setDefaultValue(defaultConfig.getVillagerSkipEnchantmentsList())
                .setTooltip(Component.translatable("tooltip.enchantment-exploration.villager.filter-list"))
                .setSaveConsumer(newValue -> EnchantmentExploration.getConfig().setVillagerSkipEnchantmentsFromList(newValue))
                .build());

        return builder.build();
    }
}
