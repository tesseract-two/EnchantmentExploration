package net.falsetm.enchantment_exploration.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.falsetm.enchantment_exploration.EnchantmentExploration;
import net.falsetm.enchantment_exploration.mixin_ducks.RegistryEntryListNamedDuck;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderGetter;
import net.minecraft.core.HolderSet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(HolderGetter.class)
public interface RegistryMixin{
    @ModifyExpressionValue(method = "getRandomElementOf", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/HolderGetter;get(Lnet/minecraft/tags/TagKey;)Ljava/util/Optional;"))
    default Optional<?> falsetm$changeRandomOutput(Optional<HolderSet.Named<?>> original){

        if(original.isEmpty() || EnchantmentExploration.getRegistrySkipEntrySet().get().isEmpty()){
            return original;
        }

        HolderSet.Named<?> list = original.get();

        List<Holder<?>> finalEntries = new ArrayList<>();

        list.stream().forEach(entry -> {
            if(!EnchantmentExploration.getRegistrySkipEntrySet().get().contains(entry)){
                finalEntries.add(entry);
            }
        });

        HolderSet.Named<?> anotherOne = ((RegistryEntryListNamedDuck)list).falsetm$createInstance(((RegistryEntryListNamedAccessor)list).getOwner(),list.key());
        //concerning, but I double-checked, and we should be able to edit this object without consequence
        ((RegistryEntryListNamedDuck)anotherOne).falsetm$enchantmentExplorationSetEntries(finalEntries);
        return Optional.of(anotherOne);
    }
}
