package survivalblock.enchancement_unbound.mixin.adrenaline;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.event.AdrenalineEvent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Debug(export = true)
@Mixin(AdrenalineEvent.class)
public abstract class AdrenalineEventMixin {

    @Shadow
    public static float getSpeedMultiplier(LivingEntity living, int level) {
        throw new UnsupportedOperationException();
    }

    @ModifyExpressionValue(method = "getSpeedMultiplier", at = @At(value = "CONSTANT", args = "floatValue=0.05"))
    private static float multiplicationication(float original){
        if (UnboundUtil.isBasicallyOriginal(UnboundConfig.adrenalineSpeedMultiplier, 0.05f)) {
            return original;
        }
        return UnboundConfig.adrenalineSpeedMultiplier;
    }

    @ModifyExpressionValue(method = "multiply", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/util/EnchancementUtil;capMovementMultiplier(F)F"))
    private float noCapping(float original, float currentMultiplier, World world, LivingEntity living, @Local int level){
        return UnboundConfig.removeAdrenalineMovementBoostLimit ? currentMultiplier * getSpeedMultiplier(living, level)  : original;
    }
}
