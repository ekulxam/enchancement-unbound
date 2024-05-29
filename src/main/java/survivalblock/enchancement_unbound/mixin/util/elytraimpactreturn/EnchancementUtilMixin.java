package survivalblock.enchancement_unbound.mixin.util.elytraimpactreturn;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(EnchancementUtil.class)
public class EnchancementUtilMixin {

    @ModifyExpressionValue(method = "isGroundedOrAirborne(Lnet/minecraft/entity/LivingEntity;Z)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isFallFlying()Z"))
    private static boolean weDoNotCareAboutFallFlying(boolean original) {
        // effectively undoes https://github.com/MoriyaShiine/enchancement/commit/d65e4a08452c39f5bdfe9398dfc014a467e8840e#diff-b32d05c756ddd0d189e2de14ee645ec7fba30426f952092b70f8e7fd2141daf9
        // I'm sorry, moriya
        return !UnboundConfig.airMobilityEnchantsWorkWhenUsingElytra && original;
    }
}
