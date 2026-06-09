package net.falsetm.enchantment_exploration.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.falsetm.enchantment_exploration.events.AnvilScreenHandlerUpdateResultCallback;
import net.falsetm.enchantment_exploration.events.AnvilScreenTakeOutputNoRepairClearSecondCallback;
import net.falsetm.enchantment_exploration.events.AnvilScreenUpdateResultGetSecondInputCallback;
import net.minecraft.world.Container;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AnvilMenu.class)
public class AnvilScreenHandlerMixin {
    @Inject(method = "createResult", at = @At("HEAD"), cancellable = true)
    public void falsetm$UpdateResult(CallbackInfo ci){
        InteractionResult result = AnvilScreenHandlerUpdateResultCallback.EVENT.invoker().updateResult((AnvilMenu)(Object)this);
        if(result == InteractionResult.FAIL){
            ci.cancel();
        }
    }

    //skipping the 1 because we only are doing this for the getting of the second slot, as per WrapOperation headers
    @WrapOperation(method = "createResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;getItem(I)Lnet/minecraft/world/item/ItemStack;", ordinal = 1))
    ItemStack falsetm$updateResultGetSecond(Container instance, int i, Operation<ItemStack> original){
        @Nullable ItemStack result = AnvilScreenUpdateResultGetSecondInputCallback.EVENT.invoker().getStack((AnvilMenu)((Object)this), instance);
        if(result != null){
            return result;
        }
        return original.call(instance, i);
    }

    //why am I even making this an event, such overkill. Actually so stupid it clears instead of reduce
    @WrapOperation(method = "onTake", at = @At(value = "INVOKE", target = "Lnet/minecraft/world/Container;setItem(ILnet/minecraft/world/item/ItemStack;)V", ordinal = 2))
    void falsetm$takeOutputNoRepairSetSecond(Container instance, int i, ItemStack itemStack, Operation<Void> original){
        @Nullable InteractionResult result = AnvilScreenTakeOutputNoRepairClearSecondCallback.EVENT.invoker().clearSlot((AnvilMenu)((Object)this), instance);

        if(result != InteractionResult.FAIL){
            original.call(instance, i, itemStack);
        }
    }

}
