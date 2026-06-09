package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.AnvilMenu;

public interface AnvilScreenHandlerUpdateResultCallback {
    Event<AnvilScreenHandlerUpdateResultCallback> EVENT = EventFactory.createArrayBacked(AnvilScreenHandlerUpdateResultCallback.class,
            (listeners) -> (receiver) -> {
                for (AnvilScreenHandlerUpdateResultCallback listener : listeners) {
                    InteractionResult result = listener.updateResult(receiver);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });
    InteractionResult updateResult(AnvilMenu receiver);
}
