package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public interface EnchantmentContentChangedCallback {
    Event<EnchantmentContentChangedCallback> EVENT = EventFactory.createArrayBacked(EnchantmentContentChangedCallback.class,
            (listeners) -> (receiver, stack, world, pos) -> {
                for (EnchantmentContentChangedCallback listener : listeners) {
                    InteractionResult result = listener.contentChanged(receiver, stack, world, pos);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });
    InteractionResult contentChanged(EnchantmentMenu receiver, ItemStack stack, Level world, BlockPos pos);
}
