package survivalblock.enchancement_unbound.mixin.horseshoes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.enchantment.EmptyEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Shadow @Final public EnchantmentTarget target;

    @SuppressWarnings("UnreachableCode")
    @ModifyReturnValue(method = "isAcceptableItem", at = @At("RETURN"))
    private boolean horseshoes(boolean original, ItemStack stack){
        if (!UnboundConfig.horseshoes) {
            return original;
        }
        if (UnboundUtil.isValidHorseshoeEnchantment((Enchantment) (Object) this) && this.target.equals(EnchantmentTarget.ARMOR_FEET)) {
            return original || stack.getItem() instanceof HorseArmorItem;
        }
        return original;
    }


}
