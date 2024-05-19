package survivalblock.enchancement_unbound.mixin.phasing;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.PhasingComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(value = PhasingComponent.class, remap = false)
public class PhasingComponentMixin {

    @ModifyExpressionValue(method = "tick", at = @At(value = "CONSTANT", args = "doubleValue=0.5", ordinal = 0))
    private double increaseIncrement(double original) {
        if (UnboundUtil.isBasicallyOriginal(UnboundConfig.homingRadius, 0.5)) {
            return original;
        }
        return UnboundConfig.homingRadius;
    }
}
