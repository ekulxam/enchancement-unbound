package survivalblock.enchancement_unbound.mixin.midastouch;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.component.MidasTouchComponent;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.init.UnboundTags;

@SuppressWarnings("UnreachableCode")
@Mixin(value = Entity.class, priority = 2000)
public abstract class EntityMixin {

    @Shadow public abstract void setVelocity(Vec3d velocity);

    @ModifyReturnValue(method = "isInvulnerableTo", at = @At("RETURN"))
    private boolean midasTouchImmune(boolean original, DamageSource source){
        if (!((Entity) (Object) this instanceof LivingEntity living)) {
            return original;
        }
        MidasTouchComponent midasTouchComponent = UnboundEntityComponents.MIDAS_TOUCH.get(living);
        if (!midasTouchComponent.isGolden()) {
            return original;
        }
        if (source.isOf(DamageTypes.DROWN)) {
            midasTouchComponent.undo();
        }
        if (!source.isIn(UnboundTags.DamageTypes.BYPASSES_MIDAS_LINK)) {
            midasTouchComponent.damageLink();
        }
        return !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY);
    }

    @Inject(method = "tickRiding", at = @At(value = "HEAD"), cancellable = true)
    private void midasTouchCancelTickRiding(CallbackInfo ci){
        if ((Entity) (Object) this instanceof LivingEntity living && UnboundEntityComponents.MIDAS_TOUCH.get(living).shouldPreventTicking()) {
            this.setVelocity(Vec3d.ZERO);
            ci.cancel();
        }
    }

    @ModifyReturnValue(method = "isCollidable", at = @At("RETURN"))
    private boolean statuesAreCollidable(boolean original){
        //noinspection ConstantValue
        return original || ((Entity) (Object) this instanceof LivingEntity living && UnboundEntityComponents.MIDAS_TOUCH.get(living).isGolden());
    }
}
