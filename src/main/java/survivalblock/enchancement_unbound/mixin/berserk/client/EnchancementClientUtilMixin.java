package survivalblock.enchancement_unbound.mixin.berserk.client;


import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.util.EnchancementClientUtil;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(EnchancementClientUtil.class)
public class EnchancementClientUtilMixin {

    @ModifyExpressionValue(method = "getBerserkColor", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/util/EnchancementUtil;getBonusBerserkDamage(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/item/ItemStack;)F"))
    private static float berserkHasADamageCap(float damageBonus, LivingEntity living, ItemStack stack) {
        if(UnboundConfig.noBerserkDamageCap){
            return Math.min(damageBonus, EnchancementUtil.getMaxBonusBerserkDamage(stack, EnchantmentHelper.getLevel(ModEnchantments.BERSERK, stack)));
        }
        return damageBonus;
    }
}
