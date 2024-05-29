package survivalblock.enchancement_unbound.mixin.unbounditems.shardminigun.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.item.ShardMinigunItem;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @ModifyExpressionValue(method = "getUsingItemHandRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 1))
    private static boolean usingMinigunRenderType(boolean original, @Local ItemStack stack){
        return original || stack.getItem() instanceof ShardMinigunItem;
    }

    @ModifyExpressionValue(method = "getHandRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 3))
    private static boolean minigunIsACrossbowToo(boolean original, @Local(ordinal = 0) ItemStack itemStack, @Local(ordinal = 1) ItemStack itemStack2){
        return original || itemStack.getItem() instanceof ShardMinigunItem || itemStack2.getItem() instanceof ShardMinigunItem;
    }

    @ModifyExpressionValue(method = "getHandRenderType", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/item/HeldItemRenderer;isChargedCrossbow(Lnet/minecraft/item/ItemStack;)Z"))
    private static boolean mainHandMinigun(boolean original, @Local(ordinal = 0) ItemStack itemStack){
        return original || itemStack.getItem() instanceof ShardMinigunItem;
    }

    @ModifyExpressionValue(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isOf(Lnet/minecraft/item/Item;)Z", ordinal = 1))
    private boolean renderFirstPersonMinigun(boolean original, @Local(argsOnly = true) ItemStack stack){
        return stack.getItem() instanceof ShardMinigunItem || original;
    }

    @ModifyExpressionValue(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/CrossbowItem;isCharged(Lnet/minecraft/item/ItemStack;)Z"))
    private boolean renderMinigunInMiddle(boolean original, @Local(argsOnly = true) ItemStack stack){
        return stack.getItem() instanceof ShardMinigunItem || original;
    }

    @ModifyExpressionValue(method = "renderFirstPersonItem", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/network/AbstractClientPlayerEntity;isUsingItem()Z", ordinal = 0))
    private boolean noDrawbackAnimation(boolean original, @Local(argsOnly = true) ItemStack stack){
        return !(stack.getItem() instanceof ShardMinigunItem) && original;
    }
}
