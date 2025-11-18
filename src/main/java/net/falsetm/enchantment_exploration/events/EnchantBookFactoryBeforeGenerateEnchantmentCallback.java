package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.village.TradeOffers;

public interface EnchantBookFactoryBeforeGenerateEnchantmentCallback {
    Event<EnchantBookFactoryBeforeGenerateEnchantmentCallback> EVENT = EventFactory.createArrayBacked(EnchantBookFactoryBeforeGenerateEnchantmentCallback.class,
            (listeners) -> (receiver, registry, tagKey) -> {
                for (EnchantBookFactoryBeforeGenerateEnchantmentCallback listener : listeners) {
                    ActionResult result = listener.beforeRandomGetPossibleEnchants(receiver, registry, tagKey);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult beforeRandomGetPossibleEnchants(TradeOffers.EnchantBookFactory receiver, Registry<Enchantment> registry, TagKey<Enchantment> currentPossibleEnchants);
}
