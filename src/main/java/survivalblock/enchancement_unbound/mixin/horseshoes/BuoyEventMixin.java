package survivalblock.enchancement_unbound.mixin.horseshoes;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.event.BuoyEvent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Debug(export = true)
@Mixin(value = BuoyEvent.class, remap = false)
public class BuoyEventMixin {

    @ModifyExpressionValue(method = "multiply", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getEquipmentLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/entity/LivingEntity;)I", remap = true))
    private int applyBuoyToHorses(int original, @Local(argsOnly = true) LivingEntity living){
        if (!UnboundConfig.horseshoes) {
            return original;
        }
        if (!(living instanceof HorseEntity horseEntity)) {
            return original;
        }
        return Math.max(original, EnchantmentHelper.getEquipmentLevel(ModEnchantments.BUOY, horseEntity));
    }
}
