package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public interface GenerateEnchantCallback {
    Event<GenerateEnchantCallback> EVENT = EventFactory.createArrayBacked(GenerateEnchantCallback.class,
            (listeners) -> (receiver, registryManager, stack, slot, level) -> {
                for (GenerateEnchantCallback listener : listeners) {
                    @Nullable
                    List<EnchantmentInstance> result = listener.generateEnchantment(receiver, registryManager, stack, slot, level);
                    if (result != null) {
                        return result;
                    }
                }

                return null;
            });

    @Nullable
    List<EnchantmentInstance> generateEnchantment(EnchantmentMenu receiver, RegistryAccess registryManager, ItemStack stack, int slot, int level);
}
