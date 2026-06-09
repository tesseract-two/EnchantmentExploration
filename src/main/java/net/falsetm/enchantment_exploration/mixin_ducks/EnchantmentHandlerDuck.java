package net.falsetm.enchantment_exploration.mixin_ducks;

import java.util.List;
import net.minecraft.world.item.enchantment.EnchantmentInstance;

public interface EnchantmentHandlerDuck {
    List<EnchantmentInstance> falsetm$GetPossibleEnchants();
    void falsetm$SetPossibleEnchants(List<EnchantmentInstance> possibleEnchants);
}
