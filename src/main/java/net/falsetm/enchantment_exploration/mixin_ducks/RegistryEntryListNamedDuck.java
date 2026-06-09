package net.falsetm.enchantment_exploration.mixin_ducks;

import java.util.List;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;

public interface RegistryEntryListNamedDuck {
    void falsetm$enchantmentExplorationSetEntries(List<Holder<?>> entries);
    HolderSet.Named<?> falsetm$createInstance(HolderOwner<?> owner, TagKey<?> tag);
}
