package survivalblock.enchancement_unbound.mixin.slide;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Debug(export = true)
@Mixin(value = SlideComponent.class, priority = 1500)
public class SlideComponentMixin {

    @Inject(method = "lambda$serverTick$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    private void plungeAtkGoBrrr(CallbackInfo ci, @Local LivingEntity living){
        if(UnboundConfig.shouldDealSlamDamage){
            PlayerEntity player = ((SlideComponentAccessor) this).getPlayer();
            living.damage(living.getDamageSources().stalagmite(), (float) UnboundConfig.slideImpactDamage);
        }
    }


    @Inject(method = "slamTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;playSound(Lnet/minecraft/sound/SoundEvent;FF)V", shift = At.Shift.AFTER))
    private void goDamageYourself(Runnable onLand, CallbackInfo ci){
        if (UnboundConfig.shouldDealSlamDamage) {
            PlayerEntity player = ((SlideComponentAccessor) this).getPlayer();
            player.damage(player.getDamageSources().flyIntoWall(), (float) UnboundConfig.slideImpactDamage * 0.6F);
        }
    }

}
