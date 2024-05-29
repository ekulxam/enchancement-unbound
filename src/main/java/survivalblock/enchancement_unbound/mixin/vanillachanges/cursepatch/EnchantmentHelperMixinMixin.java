package survivalblock.enchancement_unbound.mixin.vanillachanges.cursepatch;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.component.type.ItemEnchantmentsComponent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.resource.featuretoggle.FeatureSet;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

import java.util.List;

@Mixin(value = EnchantmentHelper.class, priority = 1500)
public class EnchantmentHelperMixinMixin {

    /**
     * Bypasses limitCheck when calling EnchantmentHelper.generateEnchantments
     * @param original did it fail the check originally?
     * @param cir cir of the mixin
     * @param i used in the for loop to loop through the list that it generates
     * @return if it really failed the check according to cursepatch
     * @see moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.EnchantmentHelperMixin
     */
    @SuppressWarnings("LocalMayBeArgsOnly")
    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.EnchantmentHelperMixin",
            name = "Lmoriyashiine/enchancement/mixin/vanillachanges/enchantmentlimit/EnchantmentHelperMixin;enchancement$enchantmentLimit(Ljava/util/List;Lnet/minecraft/resource/featuretoggle/FeatureSet;Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;)Ljava/util/List;"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lmoriyashiine/enchancement/common/util/EnchancementUtil;limitCheck(ZZ)Z", remap = false)
    )
    private static boolean cursePatchEnchantmentHelperGenerateEnchantments(boolean original, List<EnchantmentLevelEntry> returnValue, FeatureSet enabledFeatures, Random random, ItemStack stack, @Local int i) {
        Enchantment curse = returnValue.get(i).enchantment;
        if (UnboundConfig.cursePatch && curse != null && curse.isCursed()) {
            return false;
        }
        return original;
    }



    /**
     * Bypasses limitCheck when calling EnchantmentHelper.set
     * @param original did it fail the check originally?
     * @param curse the enchantment
     * @return if it really failed the check according to cursepatch
     */
    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.EnchantmentHelperMixin",
            name = "lambda$enchancement$enchantmentLimit$0(Lnet/minecraft/component/type/ItemEnchantmentsComponent;Lnet/minecraft/registry/entry/RegistryEntry;)Z"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lmoriyashiine/enchancement/common/util/EnchancementUtil;limitCheck(ZZ)Z", remap = false)
    )
    private static boolean cursePatchEnchantmentHelperSet(boolean original, ItemEnchantmentsComponent enchantmentsComponent, RegistryEntry<Enchantment> curse) {
        if (UnboundConfig.cursePatch && curse != null && curse.value() != null && curse.value().isCursed()) {
            return true;
        }
        return original;
    }
}
