package net.falsetm.enchantment_exploration.mixin;

import net.falsetm.enchantment_exploration.mixin_ducks.RegistryEntryListNamedDuck;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderOwner;
import net.minecraft.core.HolderSet;
import net.minecraft.tags.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.gen.Invoker;

import java.util.List;

@Mixin(HolderSet.Named.class)
public class RegistryEntryListNamed implements RegistryEntryListNamedDuck {

    @Shadow
    void bind(List<Holder<?>> entries) {

    }

    @Override
    public void falsetm$enchantmentExplorationSetEntries(List<Holder<?>> entries) {
        bind(entries);
    }

    @Override
    public HolderSet.Named<?> falsetm$createInstance(HolderOwner<?> owner, TagKey<?> tag) {
        return invokeConstructor(owner, tag);
    }

    @Invoker("<init>")
    static HolderSet.Named<?> invokeConstructor(HolderOwner<?> owner, TagKey<?> tag){
        throw new AssertionError();
    }
}
