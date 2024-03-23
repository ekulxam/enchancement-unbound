package survivalblock.enchancement_unbound.mixin.slide;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Debug(export = true)
@Mixin(value = LivingEntity.class, priority = 1500)
public class LivingEntityMixinMixin {

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.slide.LivingEntityMixin",
            name = "Lmoriyashiine/enchancement/mixin/slide/LivingEntityMixin;enchancement$slide(FLnet/minecraft/entity/damage/DamageSource;)F"
    )
    @ModifyReturnValue(
            method = "@MixinSquared:Handler",
            at = @At(value = "RETURN", ordinal = 0)
    )
    private float slideOP(float value) {
        return UnboundConfig.sustainExtraDamageWhileSliding ? value : (value / 1.5F);
    }
}
