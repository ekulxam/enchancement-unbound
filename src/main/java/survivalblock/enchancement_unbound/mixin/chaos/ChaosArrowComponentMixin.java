package survivalblock.enchancement_unbound.mixin.chaos;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.ChaosArrowComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.access.PhaserChaosAccess;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = ChaosArrowComponent.class, remap = false)
public class ChaosArrowComponentMixin implements PhaserChaosAccess {

    @ModifyExpressionValue(method = "applyChaos", at = @At(value = "CONSTANT", args = "intValue=100"))
    private static int doubleDuration(int constant){
        return UnboundConfig.strongerChaosEffects ? constant * 3 : constant;
    }

    /**
     * Increases the instant effect amplifier
     * @param constant
     * @return
     */
    @ModifyExpressionValue(method = "applyChaos", at = @At(value = "CONSTANT", args = "intValue=1", ordinal = 2))
    private static int chaosAmplifier(int constant){
        return UnboundConfig.strongerChaosEffects ? -2 : constant;
    }


    @ModifyExpressionValue(method = "applyChaos", at = @At(value = "CONSTANT", args = "intValue=0", ordinal = 1))
    private static int chaosAmplifier(int constant, @Local(ordinal = 0) int level){
        return UnboundConfig.strongerChaosEffects ? level : constant;
    }

    @ModifyExpressionValue(method = "applyChaos", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I", ordinal = 0))
    private static int bypassCheck(int original){
        if (doNotCheckLevel) {
            doNotCheckLevel = false;
            return ModEnchantments.CHAOS.getMaxLevel();
        }
        return original;
    }

    @Unique
    private static boolean doNotCheckLevel;

    @Override
    public void enchancement_unbound$setLevelCheckBypass(boolean value) {
        doNotCheckLevel = value;
    }
    @Override
    public boolean enchancement_unbound$getLevelCheckBypass(){
        return doNotCheckLevel;
    }
}
