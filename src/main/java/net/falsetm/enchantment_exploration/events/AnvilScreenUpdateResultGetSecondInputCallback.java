package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.Container;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;

public interface AnvilScreenUpdateResultGetSecondInputCallback {
    Event<AnvilScreenUpdateResultGetSecondInputCallback> EVENT = EventFactory.createArrayBacked(AnvilScreenUpdateResultGetSecondInputCallback.class,
            (listeners) -> (receiver, inventory) -> {
                for (AnvilScreenUpdateResultGetSecondInputCallback listener : listeners) {
                    @Nullable ItemStack result = listener.getStack(receiver, inventory);

                    if (result != null) {
                        return result;
                    }
                }

                return null;
            });
    @Nullable ItemStack getStack(AnvilMenu receiver, Container inventory);
}
