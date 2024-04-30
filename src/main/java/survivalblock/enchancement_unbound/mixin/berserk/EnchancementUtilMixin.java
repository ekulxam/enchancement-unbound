package survivalblock.enchancement_unbound.mixin.berserk;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(EnchancementUtil.class)
public class EnchancementUtilMixin {

    @ModifyExpressionValue(method = "getBonusBerserkDamage", at = @At(value = "INVOKE", target = "Ljava/lang/Math;min(FF)F"))
    private static float berserkHasADamageCap(float original, @Local(ordinal = 1) float bonus) {
        if(UnboundConfig.noBerserkDamageCap){
            return bonus;
        }
        return original;
    }
}
