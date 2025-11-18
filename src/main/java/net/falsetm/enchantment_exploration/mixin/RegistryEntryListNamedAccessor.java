package net.falsetm.enchantment_exploration.mixin;

import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryOwner;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(RegistryEntryList.Named.class)
public interface RegistryEntryListNamedAccessor {
    @Accessor
    @Final
    RegistryEntryOwner<?> getOwner();
}
