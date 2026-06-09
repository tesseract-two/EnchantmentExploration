package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface SellEnchantedToolFactoryCreateCallback {
    Event<SellEnchantedToolFactoryCreateCallback> EVENT = EventFactory.createArrayBacked(SellEnchantedToolFactoryCreateCallback.class,
            (listeners) -> (receiver, inventory) -> {
                for (SellEnchantedToolFactoryCreateCallback listener : listeners) {
                    @Nullable ItemStack result = listener.sellingStack(receiver, inventory);

                    if (result != null) {
                        return result;
                    }
                }

                return null;
            });
    @Nullable ItemStack sellingStack(VillagerTrades.EnchantedItemForEmeralds receiver, ItemStack original);
}
