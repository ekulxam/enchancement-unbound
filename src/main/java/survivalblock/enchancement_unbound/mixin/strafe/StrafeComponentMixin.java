package survivalblock.enchancement_unbound.mixin.strafe;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.component.entity.GaleComponent;
import moriyashiine.enchancement.common.component.entity.StrafeComponent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = StrafeComponent.class, remap = false)
public class StrafeComponentMixin {

    @WrapWithCondition(method = "handle", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/component/entity/StrafeComponent;setStrafeCooldown(I)V"))
    private static boolean alwaysRefreshThatStrafe(StrafeComponent instance, int strafeCooldown){
        return !UnboundConfig.infiniteStrafe;
    }
}
