package net.falsetm.enchantment_exploration.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.falsetm.enchantment_exploration.events.LootTableApplyFunctionsCallback;
import net.falsetm.enchantment_exploration.events.LootTableFinishGenerateUnprocessedCallback;
import net.falsetm.enchantment_exploration.mixin_ducks.LootTableDuck;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.function.BiFunction;
import java.util.function.Consumer;

@Mixin(LootTable.class)
public class LootTableMixin implements LootTableDuck {
    @Unique
    private boolean skipFunctionMixin = false;

    @Override
    public void falsetm$skipMixin() {
        skipFunctionMixin = true;
    }

    @WrapOperation(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/functions/LootItemFunction;decorate(Ljava/util/function/BiFunction;Ljava/util/function/Consumer;Lnet/minecraft/world/level/storage/loot/LootContext;)Ljava/util/function/Consumer;"))
    public Consumer<ItemStack> falsetm$applyFunctions(BiFunction<ItemStack, LootContext, ItemStack> itemApplier, Consumer<ItemStack> lootConsumer, LootContext context, Operation<Consumer<ItemStack>> original){
        if(!skipFunctionMixin){
            @Nullable Consumer<ItemStack> result = LootTableApplyFunctionsCallback.EVENT.invoker().onApply((LootTable)((Object)this), itemApplier, lootConsumer, context, original);

            if(result != null){
                return result;
            }
        }
        skipFunctionMixin = false;
        return lootConsumer;
    }

    @Inject(method = "getRandomItemsRaw(Lnet/minecraft/world/level/storage/loot/LootContext;Ljava/util/function/Consumer;)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/storage/loot/LootContext;popVisitedElement(Lnet/minecraft/world/level/storage/loot/LootContext$VisitedEntry;)V"), cancellable = true)
    public void falsetm$endUnprocessed(LootContext context, Consumer<ItemStack> lootConsumer, CallbackInfo ci){
        InteractionResult result = LootTableFinishGenerateUnprocessedCallback.EVENT.invoker().beforeInactive((LootTable)((Object)this), context, lootConsumer);
        if(result == InteractionResult.FAIL){
            ci.cancel();
        }
    }
}
