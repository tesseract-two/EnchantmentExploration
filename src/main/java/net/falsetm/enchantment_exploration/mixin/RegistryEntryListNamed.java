package net.falsetm.enchantment_exploration.mixin;

import net.falsetm.enchantment_exploration.mixin_ducks.RegistryEntryListNamedDuck;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.entry.RegistryEntryOwner;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(RegistryEntryList.Named.class)
public class RegistryEntryListNamed implements RegistryEntryListNamedDuck {

    @Shadow
    void setEntries(List<RegistryEntry<?>> entries) {

    }

    @Override
    public void falsetm$enchantmentExplorationSetEntries(List<RegistryEntry<?>> entries) {
        setEntries(entries);
    }

    @Override
    public RegistryEntryList.Named<?> falsetm$createInstance(RegistryEntryOwner<?> owner, TagKey<?> tag) {
        return invokeConstructor(owner, tag);
    }

    @Invoker("<init>")
    static RegistryEntryList.Named<?> invokeConstructor(RegistryEntryOwner<?> owner, TagKey<?> tag){
        throw new AssertionError();
    }
}
