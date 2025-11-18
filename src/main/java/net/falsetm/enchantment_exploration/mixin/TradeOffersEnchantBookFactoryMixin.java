package net.falsetm.enchantment_exploration.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.falsetm.enchantment_exploration.events.EnchantBookFactoryAfterGenerateEnchantmentCallback;
import net.falsetm.enchantment_exploration.events.EnchantBookFactoryBeforeGenerateEnchantmentCallback;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.Registry;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.math.random.Random;
import net.minecraft.village.TradeOffers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.Optional;

@Mixin(TradeOffers.EnchantBookFactory.class)
public class TradeOffersEnchantBookFactoryMixin {
    @WrapOperation(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/Registry;getRandomEntry(Lnet/minecraft/registry/tag/TagKey;Lnet/minecraft/util/math/random/Random;)Ljava/util/Optional;"))
    Optional<RegistryEntry<Enchantment>> falsetm$EnchantmentBookFactoryReplacePossibleEnchants(Registry<Enchantment> instance, TagKey<Enchantment> tag, Random random, Operation<Optional<RegistryEntry<Enchantment>>> original){
        //we do nothing with this because I have no idea at the moment what I would do with it
        TradeOffers.EnchantBookFactory us = (TradeOffers.EnchantBookFactory)(Object)this;
        EnchantBookFactoryBeforeGenerateEnchantmentCallback.EVENT.invoker().beforeRandomGetPossibleEnchants(us, instance, tag);

        var returnValue = original.call(instance, tag, random);

        EnchantBookFactoryAfterGenerateEnchantmentCallback.EVENT.invoker().afterRandomGetPossibleEnchants(us, instance, tag);

        return returnValue;
    }
}
