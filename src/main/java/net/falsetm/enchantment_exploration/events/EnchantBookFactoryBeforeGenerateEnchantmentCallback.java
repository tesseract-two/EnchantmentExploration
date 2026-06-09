package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.enchantment.Enchantment;

public interface EnchantBookFactoryBeforeGenerateEnchantmentCallback {
    Event<EnchantBookFactoryBeforeGenerateEnchantmentCallback> EVENT = EventFactory.createArrayBacked(EnchantBookFactoryBeforeGenerateEnchantmentCallback.class,
            (listeners) -> (receiver, registry, tagKey) -> {
                for (EnchantBookFactoryBeforeGenerateEnchantmentCallback listener : listeners) {
                    InteractionResult result = listener.beforeRandomGetPossibleEnchants(receiver, registry, tagKey);

                    if (result != InteractionResult.PASS) {
                        return result;
                    }
                }

                return InteractionResult.PASS;
            });

    InteractionResult beforeRandomGetPossibleEnchants(VillagerTrades.EnchantBookForEmeralds receiver, Registry<Enchantment> registry, TagKey<Enchantment> currentPossibleEnchants);
}
