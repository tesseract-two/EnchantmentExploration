package net.falsetm.enchantment_exploration.util;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public class EnchantmentHelper {
    public static List<EnchantmentInstance> legalEnchantments(ItemStack stack, List<EnchantmentInstance> inputEnchantments){
        List<EnchantmentInstance> enchantments = new ArrayList<>();
        for(EnchantmentInstance enchantment : inputEnchantments){
            if(enchantment.enchantment().value().canEnchant(stack)){
                enchantments.add(enchantment);
            }
        }
        return enchantments;
    }
}
