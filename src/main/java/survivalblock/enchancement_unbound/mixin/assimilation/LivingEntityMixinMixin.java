package survivalblock.enchancement_unbound.mixin.assimilation;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = LivingEntity.class, priority = 1500)
public class LivingEntityMixinMixin {

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.assimilation.LivingEntityMixin",
            name = "Lmoriyashiine/enchancement/mixin/assimilation/LivingEntityMixin;modifyTimeLeft(I)I"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;max(II)I"
            )
    )
    private int instantEatingMaybe(int original) {
        return UnboundConfig.assimilationInhaleFood ? 1 : original;
    }
}
