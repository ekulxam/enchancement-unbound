package survivalblock.enchancement_unbound.mixin.strafe;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.component.entity.GaleComponent;
import moriyashiine.enchancement.common.component.entity.StrafeComponent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(value = StrafeComponent.class, remap = false)
public class StrafeComponentMixin {

    @WrapWithCondition(method = "use", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/component/entity/StrafeComponent;setStrafeCooldown(I)V"))
    private boolean alwaysRefreshThatStrafe(StrafeComponent instance, int strafeCooldown){
        return !UnboundConfig.infiniteStrafe;
    }

    @ModifyExpressionValue(method = "clientTick", at = @At(value = "FIELD", target = "Lmoriyashiine/enchancement/common/component/entity/StrafeComponent;wasPressingStrafeKey:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
    private boolean noIWasNotPressingTheDashKey(boolean original) {
        return !UnboundUtil.strafeAirbending() && original;
    }
}
