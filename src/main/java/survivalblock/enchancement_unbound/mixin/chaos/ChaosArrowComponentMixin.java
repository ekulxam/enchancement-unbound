package survivalblock.enchancement_unbound.mixin.chaos;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.ChaosArrowComponent;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Debug(export = true)
@Mixin(ChaosArrowComponent.class)
public class ChaosArrowComponentMixin {

    @ModifyConstant(method = "applyChaos", constant = @Constant(intValue = 0, ordinal = 1))
    private static int chaosAmplifier(int constant, @Local(ordinal = 0) int level){
        return UnboundConfig.strongerChaosEffects ? level : constant;
    }

    @ModifyConstant(method = "applyChaos", constant = @Constant(intValue = 1, ordinal = 2))
    private static int chaosAmplifier(int constant){
        return UnboundConfig.strongerChaosEffects ? -2 : constant;
    }

    @ModifyConstant(method = "applyChaos", constant = @Constant(intValue = 100))
    private static int doubleDuration(int constant){
        return UnboundConfig.strongerChaosEffects ? constant * 3 : constant;
    }
}
