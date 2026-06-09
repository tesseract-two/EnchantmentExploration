package net.falsetm.enchantment_exploration.events;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import org.jetbrains.annotations.Nullable;

import java.util.function.BiFunction;
import java.util.function.Consumer;

public interface LootTableApplyFunctionsCallback {
    Event<LootTableApplyFunctionsCallback> EVENT = EventFactory.createArrayBacked(LootTableApplyFunctionsCallback.class,
            (listeners) -> (receiver, itemApplier, lootConsumer, context, original) -> {
                for (LootTableApplyFunctionsCallback listener : listeners) {
                    @Nullable Consumer<ItemStack> result = listener.onApply(receiver, itemApplier, lootConsumer, context, original);

                    if (result != null) {
                        return result;
                    }
                }

                return null;
            });
    @Nullable Consumer<ItemStack> onApply(LootTable receiver, BiFunction<ItemStack, LootContext, ItemStack> itemApplier, Consumer<ItemStack> lootConsumer, LootContext context, Operation<Consumer<ItemStack>> original);
}
