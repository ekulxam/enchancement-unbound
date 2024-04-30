package survivalblock.enchancement_unbound.mixin.dash;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.DashComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(DashComponent.class)
public class DashComponentMixin {
    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;isOnGround()Z"))
    private boolean alwaysRefreshThatDash(boolean original){
        if (UnboundConfig.dashChargeInAir) {
            return true;
        }
        return original;
    }
}
