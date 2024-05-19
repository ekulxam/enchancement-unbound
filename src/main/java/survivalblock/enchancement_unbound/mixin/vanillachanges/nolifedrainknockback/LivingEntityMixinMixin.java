package survivalblock.enchancement_unbound.mixin.vanillachanges.nolifedrainknockback;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.init.UnboundDamageTypes;

@Mixin(value = LivingEntity.class, priority = 1500)
public class LivingEntityMixinMixin {

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.nolifedrainknockback.LivingEntityMixin",
            name = "Lmoriyashiine/enchancement/mixin/vanillachanges/nolifedrainknockback/LivingEntityMixin;enchancement$cancelLifeDrainKnockbackBefore(Lnet/minecraft/entity/damage/DamageSource;FLorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;)V"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/damage/DamageSource;isOf(Lnet/minecraft/registry/RegistryKey;)Z")
    )
    private boolean noMidasTouchKnockbackEither(boolean original, @Local(argsOnly = true) DamageSource source) {
        return original || source.isOf(UnboundDamageTypes.MIDAS_LINK);
    }
}
