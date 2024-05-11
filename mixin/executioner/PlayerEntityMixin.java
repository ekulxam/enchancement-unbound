package survivalblock.enchancement_unbound.mixin.executioner;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Debug(export = true)
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    @Shadow public abstract float getAttackCooldownProgress(float baseTime);

    @Unique
    private float prevCooldown;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;getAttackCooldownProgress(F)F", ordinal = 0))
    private void saveBeforeRefresh(Entity target, CallbackInfo ci){
        this.prevCooldown = this.getAttackCooldownProgress(0.0f);
    }

    @ModifyArg(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", ordinal = 0), index = 1)
    private float guillotine(float value, @Local(ordinal = 0, argsOnly = true) Entity target){
        return UnboundUtil.execute(value, this, target, this.prevCooldown);
    }
}
