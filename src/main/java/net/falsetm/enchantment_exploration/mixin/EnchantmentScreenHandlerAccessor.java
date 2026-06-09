package net.falsetm.enchantment_exploration.mixin;

import net.minecraft.util.RandomSource;
import net.minecraft.world.inventory.EnchantmentMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(EnchantmentMenu.class)
public interface EnchantmentScreenHandlerAccessor{
    @Accessor("random")
    RandomSource getRandom();
}
