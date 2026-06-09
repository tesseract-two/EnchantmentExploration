package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.AnvilMenu;

public interface AnvilScreenTakeOutputNoRepairClearSecondCallback {
    Event<AnvilScreenTakeOutputNoRepairClearSecondCallback> EVENT = EventFactory.createArrayBacked(AnvilScreenTakeOutputNoRepairClearSecondCallback.class,
            (listeners) -> (receiver, inventory) -> {
                for (AnvilScreenTakeOutputNoRepairClearSecondCallback listener : listeners) {
                    InteractionResult result = listener.clearSlot(receiver, inventory);
                    
                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });
    InteractionResult clearSlot(AnvilMenu receiver, Container inventory);
}
