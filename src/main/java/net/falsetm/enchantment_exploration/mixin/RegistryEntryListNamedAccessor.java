package net.falsetm.enchantment_exploration.mixin;

import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(HolderSet.Named.class)
public interface RegistryEntryListNamedAccessor {
    @Accessor
    @Final
    HolderOwner<?> getOwner();
}
