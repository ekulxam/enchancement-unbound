package survivalblock.enchancement_unbound.mixin.warp;

import com.bawnorton.mixinsquared.TargetHandler;
import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@SuppressWarnings("UnreachableCode")
@Mixin(value = PersistentProjectileEntity.class, priority = 1500)
public abstract class PersistentProjectileEntityMixinMixin extends ProjectileEntity {

    public PersistentProjectileEntityMixinMixin(EntityType<? extends ProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.warp.PersistentProjectileEntityMixin",
            name = "Lmoriyashiine/enchancement/mixin/warp/PersistentProjectileEntityMixin;enchancement$warp(Lnet/minecraft/util/hit/BlockHitResult;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/component/entity/WarpComponent;setHasWarp(Z)V", remap = false,
            shift = At.Shift.BEFORE)
    )
    private void stopWarpGoingWeirdly(BlockHitResult blockHitResult, CallbackInfo ci, CallbackInfo myCi) {
        if (!UnboundConfig.loyaltyWarpTridentsReturnFaster) {
            return;
        }
        if (!((PersistentProjectileEntity) (Object) this instanceof TridentEntity trident)) {
            return;
        }
        if (this.dataTracker.get(TridentEntityAccessor.getLoyalty()) <= 0 && EnchantmentHelper.getLoyalty(((TridentEntityAccessor) trident).getTridentStack()) <= 0) {
            return;
        }
        if (!(this.getOwner() instanceof LivingEntity)) {
            return;
        }
        trident.setVelocity(Vec3d.ZERO);
        trident.setOnGround(true);
        trident.setNoClip(true);
        ((TridentEntityAccessor) trident).setDealtDamage(true);
    }
}
