package survivalblock.enchancement_unbound.mixin.vanillachanges.cursepatch;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.EnchantCommand;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.util.math.random.Random;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

import java.util.Collection;
import java.util.List;

@Debug(export = true)
@Mixin(value = EnchantmentHelper.class, priority = 1500)
public class EnchantmentHelperMixinMixin {

    @SuppressWarnings("LocalMayBeArgsOnly")
    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.EnchantmentHelperMixin",
            name = "Lmoriyashiine/enchancement/mixin/vanillachanges/enchantmentlimit/EnchantmentHelperMixin;enchancement$enchantmentLimit(Lnet/minecraft/util/math/random/Random;Lnet/minecraft/item/ItemStack;IZLorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;)V"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lmoriyashiine/enchancement/common/util/EnchancementUtil;limitCheck(ZZ)Z")
    )
    private static boolean cursePatchEnchantmentHelper(boolean original, @Local(argsOnly = true) CallbackInfoReturnable<List<EnchantmentLevelEntry>> cir, @Local(ordinal = 1) int i) {
        Enchantment curse = ((EnchantmentLevelEntry) ((List) cir.getReturnValue()).get(i)).enchantment;
        if (UnboundConfig.cursePatch && curse != null && curse.isCursed()) {
            return false;
        }
        return original;
    }

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.EnchantmentHelperMixin",
            name = "Lmoriyashiine/enchancement/mixin/vanillachanges/enchantmentlimit/EnchantmentHelperMixin;enchancement$enchantmentLimit(Ljava/util/Map;)Ljava/util/Map;"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lmoriyashiine/enchancement/common/util/EnchancementUtil;limitCheck(ZZ)Z")
    )
    private static boolean cursePatchEnchantmentHelper(boolean original, @Local Enchantment curse) {
        if (UnboundConfig.cursePatch && curse != null && curse.isCursed()) {
            return true;
        }
        return original;
    }
}
