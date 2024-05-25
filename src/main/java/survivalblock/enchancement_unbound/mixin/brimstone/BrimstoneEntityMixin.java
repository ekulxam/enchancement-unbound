package survivalblock.enchancement_unbound.mixin.brimstone;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Mixin(BrimstoneEntity.class)
public abstract class BrimstoneEntityMixin extends PersistentProjectileEntity {

    @Shadow protected abstract void addParticles(double x, double y, double z);

    @Unique
    private boolean getIgnoresDamageLimit() {
        return UnboundEntityComponents.BRIMSTONE_BYPASS.get((BrimstoneEntity) (Object) this).getIgnoresDamageLimit();
    }

    protected BrimstoneEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyExpressionValue(method = "lambda$tick$1", at = @At(value = "CONSTANT", args = "doubleValue=50"))
    private double enforceMyCapNotYours(double doubleValue, Entity entity) {
        if(this.getIgnoresDamageLimit()){
            return doubleValue;
        }
        return Math.max(doubleValue, UnboundConfig.maxBrimstoneDamage);
    }

    @ModifyExpressionValue(method = "addParticles", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/entity/projectile/BrimstoneEntity;getDamage()D"))
    private double particleLimit(double damage) {
        return Math.max(damage, 12);
    }

    @ModifyExpressionValue(method = "tick", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/hit/BlockHitResult;getType()Lnet/minecraft/util/hit/HitResult$Type;"))
    private HitResult.Type dontCallTheBreak(HitResult.Type original, @Local BlockHitResult hitResult) {
        if (!(this.getIgnoresDamageLimit() && UnboundConfig.orbitalBrimstoneGoesThroughWalls)) {
            return original;
        }
        if (original.equals(HitResult.Type.BLOCK)) {
            if (this.getWorld().isClient) {
                this.addParticles(hitResult.getPos().getX(), hitResult.getPos().getY(), hitResult.getPos().getZ());
            }
            return HitResult.Type.MISS;
        }
        return original;
    }
}
