package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffers;
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
    @Nullable ItemStack sellingStack(TradeOffers.SellEnchantedToolFactory receiver, ItemStack original);
}
