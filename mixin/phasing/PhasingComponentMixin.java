package survivalblock.enchancement_unbound.mixin.phasing;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.PhasingComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = PhasingComponent.class, priority = 1500)
public class PhasingComponentMixin {

    @ModifyExpressionValue(method = "tick", at = @At(value = "CONSTANT", args = "doubleValue=0.5", ordinal = 0), remap = false)
    private double increaseIncrement(double original) {
        double nextValue = UnboundConfig.homingRadius;
        return nextValue == original ? original : nextValue;
    }
}
