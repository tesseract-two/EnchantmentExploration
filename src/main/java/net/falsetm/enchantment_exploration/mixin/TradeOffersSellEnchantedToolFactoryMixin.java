package net.falsetm.enchantment_exploration.mixin;

import net.falsetm.enchantment_exploration.events.SellEnchantedToolFactoryCreateCallback;
import net.minecraft.item.ItemStack;
import net.minecraft.village.TradeOffers;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Mixin(TradeOffers.SellEnchantedToolFactory.class)
public class TradeOffersSellEnchantedToolFactoryMixin {
    @ModifyArg(method = "create", at = @At(value = "INVOKE", target = "Lnet/minecraft/village/TradeOffer;<init>(Lnet/minecraft/village/TradedItem;Lnet/minecraft/item/ItemStack;IIF)V"))
    ItemStack falsetm$EnchantedToolFactoryReplacePossibleEnchants(ItemStack itemStack){
        @Nullable ItemStack result = SellEnchantedToolFactoryCreateCallback.EVENT.invoker().sellingStack((TradeOffers.SellEnchantedToolFactory)((Object)this), itemStack);

        if(result != null){
            return result;
        }

        return itemStack;
    }
}
