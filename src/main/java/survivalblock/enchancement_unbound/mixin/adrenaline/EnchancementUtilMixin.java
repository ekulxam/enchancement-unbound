package survivalblock.enchancement_unbound.mixin.adrenaline;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = EnchancementUtil.class, remap = false)
public class EnchancementUtilMixin {

    @ModifyReturnValue(method = "capMovementMultiplier", at = @At("RETURN"), remap = false)
    private static float adrenalineSupremacy(float original, @Local(argsOnly = true) float multiplier){
        return UnboundConfig.removeAdrenalineMovementBoostLimit ? multiplier : original;
    }
}
