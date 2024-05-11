package survivalblock.enchancement_unbound.mixin.frostbite;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.access.TridentWalkerAccess;

@Mixin(FrostWalkerEnchantment.class)
public class FrostWalkerEnchantmentMixin implements TridentWalkerAccess {

    @Unique
    private static boolean didComeFromTrident = false;
    @Unique
    private static int divergence = 0;
    @Nullable
    @Unique
    private static TridentEntity walker = null;


    @Override
    public void enchancement_unbound$setEntityCheckBypass(boolean newValue) {
        didComeFromTrident = newValue;
    }

    @Override
    public void enchancement_unbound$setTridentEntityWalker(TridentEntity tridentEntity) {
        walker = tridentEntity;
    }

    @Override
    public void enchancement_unbound$setFrostYOffset(int offset) {
        divergence = offset;
    }

    @ModifyExpressionValue(method = "freezeWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isOnGround()Z"))
    private static boolean yeetOwner(boolean original){
        return original || didComeFromTrident;
    }

    @ModifyExpressionValue(method = "freezeWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getPos()Lnet/minecraft/util/math/Vec3d;"))
    private static Vec3d getTridentPos(Vec3d original){
        return didComeFromTrident && walker != null ? walker.getPos() : original;
    }

    @ModifyExpressionValue(method = "freezeWater", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getRandom()Lnet/minecraft/util/math/random/Random;"))
    private static Random getTridentRandom(Random original){
        return didComeFromTrident && walker != null ? walker.getWorld().getRandom() : original;
    }

    @ModifyExpressionValue(method = "freezeWater", at = @At(value = "CONSTANT", args = "intValue=-1"))
    private static int applyDivergence(int original){
        return didComeFromTrident ? divergence : original;
    }
}
