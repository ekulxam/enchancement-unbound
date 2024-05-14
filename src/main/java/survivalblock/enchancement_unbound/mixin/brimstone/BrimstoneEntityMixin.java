package survivalblock.enchancement_unbound.mixin.brimstone;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.access.BrimstoneIgnoreDamageAccess;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = BrimstoneEntity.class, remap = false)
public class BrimstoneEntityMixin implements BrimstoneIgnoreDamageAccess {

    @Unique
    private boolean ignoresDamageLimit = false;
    @Override
    public void enchancement_unbound$setBrimstoneIgnoresDamageLimit(boolean value) {
        this.ignoresDamageLimit = value;
    }

    @ModifyExpressionValue(method = "lambda$tick$1", at = @At(value = "CONSTANT", args = "doubleValue=50"))
    private double enforceMyCapNotYours(double doubleValue, Entity entity) {
        if(this.ignoresDamageLimit){
            return doubleValue;
        }
        return Math.max(doubleValue, UnboundConfig.maxBrimstoneDamage);
    }

    @ModifyExpressionValue(method = "addParticles", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/entity/projectile/BrimstoneEntity;getDamage()D"))
    private double particleLimit(double damage) {
        return Math.max(damage, 12);
    }
}
