package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.LootTable;
import java.util.function.Consumer;

public interface LootTableFinishGenerateUnprocessedCallback {
    Event<LootTableFinishGenerateUnprocessedCallback> EVENT = EventFactory.createArrayBacked(LootTableFinishGenerateUnprocessedCallback.class,
            (listeners) -> (receiver, context, lootConsumer) -> {
                for (LootTableFinishGenerateUnprocessedCallback listener : listeners) {
                    InteractionResult result = listener.beforeInactive(receiver, context, lootConsumer);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });
    InteractionResult beforeInactive(LootTable receiver, LootContext context, Consumer<ItemStack> lootConsumer);
}
