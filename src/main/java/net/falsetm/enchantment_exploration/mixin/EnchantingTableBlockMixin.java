package net.falsetm.enchantment_exploration.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.falsetm.enchantment_exploration.events.IsAccessPowerProviderCallback;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.EnchantingTableBlock;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
@Mixin(EnchantingTableBlock.class)
public class EnchantingTableBlockMixin {
//    @WrapOperation(method = "isValidBookShelf", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;isIn(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
//    private static boolean falsetm$isPowerProvider(BlockState instance, TagKey tagKey, Operation<Boolean> original){
//        @Nullable Boolean result = IsAccessPowerProviderCallback.EVENT.invoker().isPowerProvider(instance, tagKey);
//        if(result != null){
//            return result;
//        }
//        return original.call(instance, tagKey);
//    }

    @WrapOperation(method = "isValidBookShelf", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/state/BlockState;is(Lnet/minecraft/tags/TagKey;)Z", ordinal = 0))
    private static boolean falsetm$isPowerProvider(BlockState instance, TagKey tagKey, Operation<Boolean> original) {
        @Nullable Boolean result = IsAccessPowerProviderCallback.EVENT.invoker().isPowerProvider(instance, tagKey);
        if(result != null){
            return result;
        }
        return original.call(instance, tagKey);
    }
}
