package survivalblock.enchancement_unbound.mixin.horseshoes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(EnchantmentHelper.class)
public class EnchancementHelperMixin {

    @ModifyReturnValue(method = "getEquipmentLevel", at = @At("RETURN"))
    private static int accountForHorseshoes(int original, Enchantment enchantment, LivingEntity entity) {
        if (UnboundConfig.horseshoes && entity instanceof HorseEntity horseEntity && UnboundUtil.isValidHorseshoeEnchantment(enchantment)) {
            return Math.max(UnboundUtil.getHorseshoeEnchantment(enchantment, horseEntity), original);
        }
        return original;
    }
}
