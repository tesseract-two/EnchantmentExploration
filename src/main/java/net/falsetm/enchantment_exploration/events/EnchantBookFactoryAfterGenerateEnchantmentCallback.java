package net.falsetm.enchantment_exploration.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.ActionResult;
import net.minecraft.village.TradeOffers;

public interface EnchantBookFactoryAfterGenerateEnchantmentCallback {
    Event<EnchantBookFactoryAfterGenerateEnchantmentCallback> EVENT = EventFactory.createArrayBacked(EnchantBookFactoryAfterGenerateEnchantmentCallback.class,
            (listeners) -> (receiver, registry, tagKey) -> {
                for (EnchantBookFactoryAfterGenerateEnchantmentCallback listener : listeners) {
                    ActionResult result = listener.afterRandomGetPossibleEnchants(receiver, registry, tagKey);

                    if (result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult afterRandomGetPossibleEnchants(TradeOffers.EnchantBookFactory receiver, Registry<Enchantment> registry, TagKey<Enchantment> currentPossibleEnchants);
}
