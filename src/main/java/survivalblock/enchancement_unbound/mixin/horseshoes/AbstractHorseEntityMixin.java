package survivalblock.enchancement_unbound.mixin.horseshoes;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import moriyashiine.enchancement.common.util.SubmersionGate;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.passive.AbstractHorseEntity;
import net.minecraft.entity.passive.AnimalEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(AbstractHorseEntity.class)
public abstract class AbstractHorseEntityMixin extends AnimalEntity {


    @Shadow protected float jumpStrength;
    @Shadow protected boolean jumping;

    @Shadow
    public abstract @Nullable LivingEntity getControllingPassenger();
    @Shadow public abstract void setJumpStrength(int strength);
    @Unique
    private static final BlockStateParticleEffect SLIME_PARTICLE = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.SLIME_BLOCK.getDefaultState());
    @Unique
    private float boost = 0.0F;
    @Unique
    private boolean shouldBoost = false;
    @Unique
    private boolean bounce = false;
    @Unique
    private float realFallDistance = 0.0f;

    protected AbstractHorseEntityMixin(EntityType<? extends AnimalEntity> entityType, World world) {
        super(entityType, world);
    }

    // BOUNCY
    @Inject(
            method = "handleFallDamage",
            at = @At("HEAD"),
            cancellable = true
    )
    private void bouncyHorse(float fallDistance, float damageMultiplier, DamageSource damageSource, CallbackInfoReturnable<Boolean> cir) {
        if (!UnboundConfig.horseshoes) {
            return;
        }
        if (!damageSource.isOf(DamageTypes.STALAGMITE) && fallDistance > ((float) this.getSafeFallDistance() + this.getHeight() + (this.getControllingPassenger() != null ? this.getControllingPassenger().getHeight() : 0)) && EnchancementUtil.hasEnchantment(ModEnchantments.BOUNCY, this)) {
            this.getWorld().playSoundFromEntity(null, this, SoundEvents.BLOCK_SLIME_BLOCK_FALL, this.getSoundCategory(), 1.0F, 1.0F);
            if (!this.bypassesLandingEffects() && this.isOnGround()) {
                this.bounce = true;
                this.realFallDistance = this.fallDistance;
            }
            cir.setReturnValue(false);
        }
    }

    // BOUNCY
    @ModifyArg(
            method = {"jump"},
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/passive/AbstractHorseEntity;setVelocity(DDD)V"
            ),
            index = 1
    )
    private double bouncyHorse(double value, @Local(argsOnly = true) float strength) {
        if (UnboundConfig.horseshoes) {
            if (!this.getWorld().isClient) {
                this.getWorld().playSoundFromEntity(null, this, SoundEvents.BLOCK_SLIME_BLOCK_FALL, this.getSoundCategory(), 1.0F, 1.0F);
                ((ServerWorld)this.getWorld()).spawnParticles(SLIME_PARTICLE, this.getX(), this.getY(), this.getZ(), 32, 0.0, 0.0, 0.0, 0.15000000596046448);
            }
            return value + (strength > 1.0f ? 1.0f : strength < 0.0f ? 0.0f : strength) * (double) EnchantmentHelper.getEquipmentLevel(ModEnchantments.BOUNCY, this);
        }
        return value;
    }

    // BOUNCY AND BUOY
    @Inject(method = "tick", at = @At("HEAD"))
    private void wackyUpwardsMovement(CallbackInfo ci){
        if (this.isLogicalSideForUpdatingMovement()) {
            World world = this.getWorld();
            if (bounce) {
                double bounceStrength = Math.log(realFallDistance / 7.0F + 1.0F) / Math.log(1.05) / 16.0;
                if (!world.isClient()) {
                    bounceStrength *= 0.9;
                }
                this.setVelocity(this.getVelocity().getX(), bounceStrength, this.getVelocity().getZ());
                if (this.jumpStrength > 0) {
                    this.setJumpStrength(0);
                    this.setJumping(false);
                }
                realFallDistance = fallDistance;
            }
            this.shouldBoost = EnchancementUtil.isSubmerged(this, SubmersionGate.ALL) && EnchancementUtil.isGroundedOrAirborne(this, true) && this.jumpStrength > 0 && this.jumping;
            if (shouldBoost) {
                int buoyLevel = EnchantmentHelper.getEquipmentLevel(ModEnchantments.BUOY, this);
                this.boost = (float) MathHelper.clamp((double) this.boost + 0.0025, (double)buoyLevel * 0.075, buoyLevel);
                this.addVelocity(0.0, this.boost, 0.0);
            }
            if (shouldBoost || bounce) {
                if (bounce) {
                    bounce = false;
                }
                this.move(world.isClient() ? MovementType.PLAYER : MovementType.SELF, this.getVelocity());
            }
        }
    }

    @Inject(method = "writeCustomDataToNbt", at = @At("RETURN"))
    private void writeBoostToNbt(NbtCompound nbt, CallbackInfo ci) {
        nbt.putBoolean("ShouldBoost", this.shouldBoost);
        nbt.putFloat("Boost", this.boost);
    }

    @Inject(method = "readCustomDataFromNbt", at = @At("RETURN"))
    private void readBoostFromNbt(NbtCompound nbt, CallbackInfo ci) {
        this.shouldBoost = nbt.getBoolean("ShouldBoost");
        this.boost = nbt.getFloat("Boost");
        this.realFallDistance = fallDistance;
    }
}
