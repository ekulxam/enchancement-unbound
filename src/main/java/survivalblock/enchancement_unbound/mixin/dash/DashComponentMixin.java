package survivalblock.enchancement_unbound.mixin.dash;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.component.entity.DashComponent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(value = DashComponent.class, remap = false)
public class DashComponentMixin {
    @WrapWithCondition(method = "use", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/component/entity/DashComponent;setDashCooldown(I)V"))
    private boolean alwaysRefreshThatDash(DashComponent instance, int dashCooldown){
        return !UnboundConfig.infiniteDash;
    }

    @ModifyExpressionValue(method = "clientTick", at = @At(value = "FIELD", target = "Lmoriyashiine/enchancement/common/component/entity/DashComponent;wasPressingDashKey:Z", opcode = Opcodes.GETFIELD, ordinal = 0))
    private boolean noIWasNotPressingTheDashKey(boolean original) {
        return !UnboundUtil.dashAirbending() && original;
    }
}
