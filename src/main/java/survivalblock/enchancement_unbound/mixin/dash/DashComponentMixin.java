package survivalblock.enchancement_unbound.mixin.dash;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.component.entity.DashComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = DashComponent.class, remap = false)
public class DashComponentMixin {
    @WrapWithCondition(method = "handle", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/component/entity/DashComponent;setDashCooldown(I)V"))
    private static boolean alwaysRefreshThatDash(DashComponent instance, int dashCooldown){
        return !UnboundConfig.infiniteDash;
    }
}
