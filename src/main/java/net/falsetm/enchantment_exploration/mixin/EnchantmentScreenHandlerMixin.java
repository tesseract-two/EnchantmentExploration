package net.falsetm.enchantment_exploration.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.falsetm.enchantment_exploration.events.EnchantmentScreenHandlerApplyCostCallback;
import net.falsetm.enchantment_exploration.events.GenerateEnchantCallback;
import net.falsetm.enchantment_exploration.events.EnchantmentContentChangedCallback;
import net.falsetm.enchantment_exploration.mixin_ducks.EnchantmentHandlerDuck;
import net.minecraft.core.BlockPos;
import net.minecraft.core.RegistryAccess;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArgs;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.invoke.arg.Args;

import java.util.List;

@Mixin(EnchantmentMenu.class)
public class EnchantmentScreenHandlerMixin implements EnchantmentHandlerDuck {

    @Unique
    private List<EnchantmentInstance> possibleEnchants;

    @Override
    public List<EnchantmentInstance> falsetm$GetPossibleEnchants() {
        return possibleEnchants;
    }

    @Override
    public void falsetm$SetPossibleEnchants(List<EnchantmentInstance> possibleEnchants) {
        this.possibleEnchants = possibleEnchants;
    }

    @Inject(method = "method_17411", at= @At("HEAD"), cancellable = true)
    public void falsetm$EnchantmentContentChanged(ItemStack itemStack, Level world, BlockPos pos, CallbackInfo ci){
        InteractionResult result = EnchantmentContentChangedCallback.EVENT.invoker().contentChanged((EnchantmentMenu) ((Object)this), itemStack, world, pos);
        if(result == InteractionResult.FAIL){
            ci.cancel();
        }
    }

    @WrapOperation(method = "method_17410", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/EnchantmentMenu;getEnchantmentList(Lnet/minecraft/core/RegistryAccess;Lnet/minecraft/world/item/ItemStack;II)Ljava/util/List;"))
    public List<EnchantmentInstance> falsetm$ButtonClickedGenerateEnchantment(EnchantmentMenu instance, RegistryAccess registryManager, ItemStack stack, int slot, int level, Operation<List<EnchantmentInstance>> original){
        List <EnchantmentInstance> result = GenerateEnchantCallback.EVENT.invoker().generateEnchantment(instance, registryManager, stack, slot, level);
        if(result != null){
            return result;
        }
        return original.call(instance, registryManager, stack, slot, level);
    }

//    @ModifyArgs(method = "method_17410", at= @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;applyEnchantmentCosts(Lnet/minecraft/item/ItemStack;I)V"))
//    public void falsetm$modifyApplyEnchantmentCost(Args args){
//        @Nullable Integer output = EnchantmentScreenHandlerApplyCostCallback.EVENT.invoker().applyCost((EnchantmentMenu) ((Object)this), args.get(0), args.get(1));
//        if(output != null){
//            args.set(1, output);
//        }
//    }

    @ModifyArgs(method = "clickMenuButton", at= @At(value = "INVOKE", target = "Lnet/minecraft/world/inventory/ContainerLevelAccess;execute(Ljava/util/function/BiConsumer;)V"))
    public void falsetm$modifyApplyEnchantment(Args args) {
        @Nullable Integer output = EnchantmentScreenHandlerApplyCostCallback.EVENT.invoker().applyCost((EnchantmentMenu) ((Object)this), args.get(0), args.get(1));
        if(output != null){
            args.set(1, output);
        }
    }
}
