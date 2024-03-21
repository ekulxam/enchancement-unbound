package survivalblock.enchancement_unbound.mixin.brimstone;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = BrimstoneEntity.class, priority = 1500)
public class BrimstoneEntityMixin {

    @ModifyExpressionValue(method = "lambda$tick$1", at = @At(value = "CONSTANT", args = "doubleValue=50"))
    private double enforceMyCapNotYours(double doubleValue) {
        return Math.max(doubleValue, UnboundConfig.maxBrimstoneDamage);
    }
}
