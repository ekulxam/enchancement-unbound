package survivalblock.enchancement_unbound.mixin.slide;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageSources;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Debug(export = true)
@Mixin(value = SlideComponent.class, priority = 1500)
public class SlideComponentMixin {

    @Inject(method = "lambda$serverTick$1", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;takeKnockback(DDD)V"))
    private void goDamageYourself(CallbackInfo ci, @Local LivingEntity living){
        PlayerEntity player = ((SlideComponentAccessor) this).getPlayer();
        living.damage(living.getDamageSources().playerAttack(player), (float) UnboundConfig.slideImpactDamage);
        player.damage(player.getDamageSources().flyIntoWall(), (float) UnboundConfig.slideImpactDamage / 4);
    }
}
