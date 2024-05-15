package survivalblock.enchancement_unbound.mixin.vanillachanges.allcrossbowshavemultishot;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Debug(export = true)
@Mixin(value = CrossbowItem.class)
public class CrossbowItemMixin {

    @ModifyExpressionValue(method = "loadProjectiles", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/EnchantmentHelper;getLevel(Lnet/minecraft/enchantment/Enchantment;Lnet/minecraft/item/ItemStack;)I"))
    private static int shareInteger(int i, @Share("i") LocalIntRef iRef){
        iRef.set(i);
        return i;
    }

    @ModifyVariable(method = "loadProjectiles",
            at = @At(value = "STORE", ordinal = 1), ordinal = 0)
    private static int multishotEnabled(int j, @Share("i") LocalIntRef iRef) {
        int i = iRef.get();
        int correctlyCalculateMultishot = i > 0 ? (1 + (2 * i)) : 0;
        return UnboundConfig.allCrossbowsHaveMultishot ? Math.max(3, correctlyCalculateMultishot) : j;
    }
}
