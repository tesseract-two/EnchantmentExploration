package net.falsetm.enchantment_exploration.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.falsetm.enchantment_exploration.events.EnchantBookFactoryAfterGenerateEnchantmentCallback;
import net.falsetm.enchantment_exploration.events.EnchantBookFactoryBeforeGenerateEnchantmentCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.tags.TagKey;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.enchantment.Enchantment;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(VillagerTrades.EnchantBookForEmeralds.class)
public class TradeOffersEnchantBookFactoryMixin {
    @WrapOperation(method = "getOffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/Registry;getRandomElementOf(Lnet/minecraft/tags/TagKey;Lnet/minecraft/util/RandomSource;)Ljava/util/Optional;"))
    Optional<Holder<Enchantment>> falsetm$EnchantmentBookFactoryReplacePossibleEnchants(Registry<Enchantment> instance, TagKey<Enchantment> tag, RandomSource random, Operation<Optional<Holder<Enchantment>>> original){
        //we do nothing with this because I have no idea at the moment what I would do with it
        VillagerTrades.EnchantBookForEmeralds us = (VillagerTrades.EnchantBookForEmeralds)(Object)this;
        EnchantBookFactoryBeforeGenerateEnchantmentCallback.EVENT.invoker().beforeRandomGetPossibleEnchants(us, instance, tag);

        var returnValue = original.call(instance, tag, random);

        EnchantBookFactoryAfterGenerateEnchantmentCallback.EVENT.invoker().afterRandomGetPossibleEnchants(us, instance, tag);

        return returnValue;
    }
}
