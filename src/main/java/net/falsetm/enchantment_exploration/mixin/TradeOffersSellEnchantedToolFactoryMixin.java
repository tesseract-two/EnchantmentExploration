//package net.falsetm.enchantment_exploration.mixin;
//
//import net.falsetm.enchantment_exploration.events.SellEnchantedToolFactoryCreateCallback;
//import net.minecraft.world.entity.npc.villager.VillagerTrades;
//import net.minecraft.world.item.ItemStack;
//import org.jetbrains.annotations.Nullable;
//import org.spongepowered.asm.mixin.Mixin;
//import org.spongepowered.asm.mixin.injection.At;
//import org.spongepowered.asm.mixin.injection.ModifyArg;
//
//@Mixin(VillagerTrades.EnchantedItemForEmeralds.class)
//public class TradeOffersSellEnchantedToolFactoryMixin {
//    @ModifyArg(method = "getOffer", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/item/trading/MerchantOffer;<init>(Lnet/minecraft/world/item/trading/ItemCost;Lnet/minecraft/world/item/ItemStack;IIF)V"))
//    ItemStack falsetm$EnchantedToolFactoryReplacePossibleEnchants(ItemStack itemStack){
//        @Nullable ItemStack result = SellEnchantedToolFactoryCreateCallback.EVENT.invoker().sellingStack((VillagerTrades.EnchantedItemForEmeralds)((Object)this), itemStack);
//
//        if(result != null){
//            return result;
//        }
//
//        return itemStack;
//    }
//}
