package survivalblock.enchancement_unbound.mixin.bouncy;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(value = LivingEntity.class, priority = 1500)
public class LivingEntityMixinMixin {

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.bouncy.LivingEntityMixin",
            name = "Lmoriyashiine/enchancement/mixin/bouncy/LivingEntityMixin;enchancement$bouncy(D)D"
    )
    @ModifyReturnValue(
            method = "@MixinSquared:Handler",
            at = @At(value = "RETURN", ordinal = 0)
    )
    private double amplifyBouncy(double original) {
        if (UnboundUtil.isBasicallyOriginal(UnboundConfig.bouncyJumpMultiplier, 1)) {
            return original;
        }
        return original * UnboundConfig.bouncyJumpMultiplier;
    }
}
