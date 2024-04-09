package survivalblock.enchancement_unbound.mixin.wardenspine;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.CrossbowItem;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Debug(export = true)
@Mixin(value = LivingEntity.class, priority = 1500)
public abstract class LivingEntityMixinMixin extends Entity {

    public LivingEntityMixinMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.wardenspine.LivingEntityMixin",
            name = "Lmoriyashiine/enchancement/mixin/wardenspine/LivingEntityMixin;enchancement$wardenspine(FLnet/minecraft/entity/damage/DamageSource;)F"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;)Z"
            )
    )
    private void weDoALittleKnockback(CallbackInfoReturnable<Float> cir, @Local LivingEntity living) {
        if (!UnboundConfig.wardenspineYeets) {
            return;
        }
        living.velocityDirty = true;
        living.setVelocity(Vec3d.fromPolar(living.getPitch(), living.getYaw()).multiply(-1.5));
        living.velocityModified = true;
    }
}
