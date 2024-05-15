package survivalblock.enchancement_unbound.mixin.frostbite;

import moriyashiine.enchancement.common.enchantment.FrostbiteEnchantment;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = FrostbiteEnchantment.class)
public class FrostbiteEnchantmentMixin {

    @ModifyVariable(method = "onTargetDamaged", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;getFrozenTicks()I", shift = At.Shift.BEFORE), index = 3, ordinal = 1)
    private int freezesToDeath(int frozenTicks){
        return UnboundConfig.infiniteFrostbite ? Integer.MAX_VALUE : frozenTicks;
    }
}
