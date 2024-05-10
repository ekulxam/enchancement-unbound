package survivalblock.enchancement_unbound.mixin.slide;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundDamageTypes;

@Mixin(value = SlideComponent.class, priority = 1500)
public class SlideComponentMixin {
    @Shadow @Final private PlayerEntity obj;

    @Inject(method = "lambda$serverTick$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    private void plungeAtkGoBrrr(CallbackInfo ci, @Local LivingEntity living){
        if(UnboundConfig.shouldDealSlamDamage){
            living.damage(ModDamageTypes.create(this.obj.getWorld(), UnboundDamageTypes.SLAM_IMPACT, this.obj), (float) UnboundConfig.slideImpactDamage);
        }
    }


    @Inject(method = "slamTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V", shift = At.Shift.AFTER))
    private void goDamageYourself(Runnable onLand, CallbackInfo ci){
        if (UnboundConfig.slamSelfDamage) {
            PlayerEntity player = ((SlideComponentAccessor) this).getPlayer();
            player.damage(player.getDamageSources().flyIntoWall(), (float) UnboundConfig.slideImpactDamage * 0.6F);
        }
    }

    @ModifyExpressionValue(method = "slamTick", at = @At(value = "CONSTANT", args = "doubleValue=-3.0", ordinal = 0), remap = false)
    private double sinceWhenDoYouFallAtAConstantSpeed(double original){
        return UnboundConfig.evenFasterImpactFall ? Math.min(original, this.obj.getVelocity().getY() - 0.005) : original;
    }
}
