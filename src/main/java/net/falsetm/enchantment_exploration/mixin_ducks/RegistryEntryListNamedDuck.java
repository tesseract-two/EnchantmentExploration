package net.falsetm.enchantment_exploration.mixin_ducks;

import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.TagKey;

import java.util.List;

public interface RegistryEntryListNamedDuck {
    void falsetm$enchantmentExplorationSetEntries(List<RegistryEntry<?>> entries);
    RegistryEntryList.Named<?> falsetm$createInstance(RegistryEntryOwner<?> owner, TagKey<?> tag);
}
