package survivalblock.enchancement_unbound.mixin.delay;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.component.entity.DelayComponent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = DelayComponent.class, remap = false)
public class DelayComponentMixin {

    @ModifyReturnValue(method = "shouldChangeParticles", at = @At("RETURN"), remap = false)
    private boolean instaChargeVisuals(boolean original){
        return UnboundConfig.instantChargeDelay || original;
    }

    @ModifyExpressionValue(method = "serverTick", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"), remap = false)
    private float instaChargeLerp(float original){
        return UnboundConfig.instantChargeDelay ? 1.0f : original;
    }

    @ModifyExpressionValue(method = "serverTick", at = @At(value = "CONSTANT", args = "intValue=200"), remap = false)
    private int changeDelayFloatTime(int original){
        return UnboundConfig.delayFloatTime == original ? original : UnboundConfig.delayFloatTime;
    }
}
