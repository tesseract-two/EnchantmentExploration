//package net.falsetm.enchantment_exploration.events;
//
//import net.fabricmc.fabric.api.event.Event;
//import net.fabricmc.fabric.api.event.EventFactory;
//import net.minecraft.core.Registry;
//import net.minecraft.tags.TagKey;
//import net.minecraft.world.InteractionResult;
//import net.minecraft.world.entity.npc.villager.VillagerTrades;
//import net.minecraft.world.item.enchantment.Enchantment;
//
//public interface EnchantBookFactoryAfterGenerateEnchantmentCallback {
//    Event<EnchantBookFactoryAfterGenerateEnchantmentCallback> EVENT = EventFactory.createArrayBacked(EnchantBookFactoryAfterGenerateEnchantmentCallback.class,
//            (listeners) -> (receiver, registry, tagKey) -> {
//                for (EnchantBookFactoryAfterGenerateEnchantmentCallback listener : listeners) {
//                    InteractionResult result = listener.afterRandomGetPossibleEnchants(receiver, registry, tagKey);
//
//                    if (result != InteractionResult.PASS) {
//                        return result;
//                    }
//                }
//
//                return InteractionResult.PASS;
//            });
//
//    InteractionResult afterRandomGetPossibleEnchants(VillagerTrades.EnchantBookForEmeralds receiver, Registry<Enchantment> registry, TagKey<Enchantment> currentPossibleEnchants);
//}
