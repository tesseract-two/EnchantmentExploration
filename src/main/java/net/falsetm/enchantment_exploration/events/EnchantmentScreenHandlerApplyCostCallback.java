package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface EnchantmentScreenHandlerApplyCostCallback {
    Event<EnchantmentScreenHandlerApplyCostCallback> EVENT = EventFactory.createArrayBacked(EnchantmentScreenHandlerApplyCostCallback.class,
            (listeners) -> (receiver, stack, inputLevels) -> {
                for (EnchantmentScreenHandlerApplyCostCallback listener : listeners) {
                    @Nullable Integer result = listener.applyCost(receiver, stack, inputLevels);

                    if (result != null) {
                        return result;
                    }
                }

                return null;
            });
    @Nullable Integer applyCost(EnchantmentMenu receiver, ItemStack stack, int inputLevels);
}

