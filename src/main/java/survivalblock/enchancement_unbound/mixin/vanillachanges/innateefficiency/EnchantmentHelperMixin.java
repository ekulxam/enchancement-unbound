package survivalblock.enchancement_unbound.mixin.vanillachanges.innateefficiency;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyReturnValue(method = "getEfficiency", at = @At("RETURN"))
    private static int applyInnateEfficiency(int original){
        if (!UnboundConfig.innateEfficiency) {
            return original;
        }
        return Math.max(Enchantments.EFFICIENCY.getMaxLevel(), original);
    }
}
