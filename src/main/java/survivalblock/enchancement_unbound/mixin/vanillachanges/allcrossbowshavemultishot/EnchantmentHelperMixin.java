package survivalblock.enchancement_unbound.mixin.vanillachanges.allcrossbowshavemultishot;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(EnchantmentHelper.class)
public class EnchantmentHelperMixin {

    @ModifyReturnValue(method = "getLevel", at = @At("RETURN"))
    private static int enchantedCrossbowsHaveMultishot(int original, Enchantment enchantment, ItemStack stack) {
        if (UnboundConfig.allCrossbowsHaveMultishot && enchantment.equals(Enchantments.MULTISHOT) && stack.isIn(ItemTags.CROSSBOW_ENCHANTABLE) && stack.getOrDefault(ModDataComponentTypes.TOGGLEABLE_PASSIVE, false)) {
            if (!stack.hasEnchantments()) {
                stack.remove(ModDataComponentTypes.TOGGLEABLE_PASSIVE);
                return original;
            }
            return EnchancementUtil.alterLevel(stack, enchantment);
        }
        return original;
    }
}
