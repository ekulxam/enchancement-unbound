package survivalblock.enchancement_unbound.mixin.grapple;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.entity.projectile.GrappleFishingBobberEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(GrappleFishingBobberEntity.class)
public class GrappleFishingBobberEntityMixin {

    @ModifyArg(method = "pullHookedEntity", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"), index = 0)
    private Vec3d multiplyPullEntityStrength(Vec3d velocity){
        return velocity.multiply(UnboundConfig.grapplePullEntityMultiplier);
    }

    @ModifyArg(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(Lnet/minecraft/util/math/Vec3d;)V"), index = 0)
    private Vec3d multiplyUseStrengthWhenYLessThanZero(Vec3d par1){
        return par1.multiply(UnboundConfig.grapplePullUserMultiplier);
    }

    @WrapOperation(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setVelocity(DDD)V"))
    private void multiplyUseStrengthWhenYGreaterThanZero(PlayerEntity instance, double x, double y, double z, Operation<Void> original){
        original.call(instance, x * UnboundConfig.grapplePullUserMultiplier, y, z * UnboundConfig.grapplePullUserMultiplier);
    }
}
