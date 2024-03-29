package survivalblock.enchancement_unbound.mixin.brimstone;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.access.BrimstoneIgnoreDamageAccess;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = BrimstoneEntity.class, priority = 1500)
public class BrimstoneEntityMixin implements BrimstoneIgnoreDamageAccess {

    @Unique
    private boolean enchancement_unbound$ignoresDamage = false;
    @Override
    public void enchancement_unbound$setBrimstoneIgnoresDamage(boolean value) {
        this.enchancement_unbound$ignoresDamage = value;
    }

    @ModifyExpressionValue(method = "lambda$tick$1", at = @At(value = "CONSTANT", args = "doubleValue=50"))
    private double enforceMyCapNotYours(double doubleValue) {
        if(this.enchancement_unbound$ignoresDamage){
            return doubleValue;
        }
        return Math.max(doubleValue, UnboundConfig.maxBrimstoneDamage);
    }

    @ModifyExpressionValue(method = "addParticles", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/entity/projectile/BrimstoneEntity;getDamage()D"))
    private double particleLimit(double damage) {
        return Math.max(damage, 12);
    }
}
