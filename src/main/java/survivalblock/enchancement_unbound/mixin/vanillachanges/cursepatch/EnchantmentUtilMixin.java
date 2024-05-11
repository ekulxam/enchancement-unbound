package survivalblock.enchancement_unbound.mixin.vanillachanges.cursepatch;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(EnchancementUtil.class)
public class EnchantmentUtilMixin {

    /**
     *
     * @param stack
     * @param enchantment
     * @param cir
     */
    @Inject(method = "isDefaultEnchantment", at = @At("HEAD"), cancellable = true)
    private static void applyCursePatchWhenAddingOther(ItemStack stack, Enchantment enchantment, CallbackInfoReturnable<Boolean> cir) {
        if (UnboundConfig.cursePatch && enchantment.isCursed()) {
            cir.setReturnValue(true);
        }
    }
}
