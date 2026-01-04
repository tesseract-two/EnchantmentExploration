package net.falsetm.enchantment_exploration.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.falsetm.enchantment_exploration.EnchantmentExploration;
import net.falsetm.enchantment_exploration.mixin_ducks.RegistryEntryListNamedDuck;
import net.minecraft.registry.RegistryEntryLookup;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Mixin(RegistryEntryLookup.class)
public interface RegistryMixin{
    @ModifyExpressionValue(method = "getRandomEntry", at = @At(value = "INVOKE", target = "Lnet/minecraft/registry/RegistryEntryLookup;getOptional(Lnet/minecraft/registry/tag/TagKey;)Ljava/util/Optional;"))
    default Optional<?> falsetm$changeRandomOutput(Optional<RegistryEntryList.Named<?>> original){

        if(original.isEmpty() || EnchantmentExploration.getRegistrySkipEntrySet().get().isEmpty()){
            return original;
        }

        RegistryEntryList.Named<?> list = original.get();

        List<RegistryEntry<?>> finalEntries = new ArrayList<>();

        list.stream().forEach(entry -> {
            if(!EnchantmentExploration.getRegistrySkipEntrySet().get().contains(entry)){
                finalEntries.add(entry);
            }
        });

        RegistryEntryList.Named<?> anotherOne = ((RegistryEntryListNamedDuck)list).falsetm$createInstance(((RegistryEntryListNamedAccessor)list).getOwner(),list.getTag());
        //concerning, but I double-checked, and we should be able to edit this object without consequence
        ((RegistryEntryListNamedDuck)anotherOne).falsetm$enchantmentExplorationSetEntries(finalEntries);
        return Optional.of(anotherOne);
    }
}
