package survivalblock.enchancement_unbound.mixin.midastouch;

import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Mixin(ProjectileEntity.class)
public class ProjectileEntityMixin {

    @Inject(method = {"canHit"}, at = {@At("HEAD")}, cancellable = true)
    private void midasStatueHitProjectile(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (entity instanceof LivingEntity living && UnboundEntityComponents.MIDAS_TOUCH.get(living).isGolden()) {
            cir.setReturnValue(true);
        }
    }
}
