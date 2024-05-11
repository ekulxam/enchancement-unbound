package survivalblock.enchancement_unbound.mixin.unbounditems.phaser;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.item.HeldItemRenderer;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import survivalblock.enchancement_unbound.common.init.UnboundItems;

@Mixin(HeldItemRenderer.class)
public class HeldItemRendererMixin {

    @ModifyVariable(method = "renderFirstPersonItem", at = @At(value = "CONSTANT", args = "floatValue=0.1", ordinal = 3, shift = At.Shift.BEFORE), index = 16)
    private float cancelPhaserPullback(float original, @Local(argsOnly = true) ItemStack stack){
        return stack.isOf(UnboundItems.SLINGSHOT) ? Math.min(original + 0.8f, 1.0f) : original;
    }
}
