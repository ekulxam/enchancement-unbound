package survivalblock.enchancement_unbound.mixin.vanillachanges.cursepatch;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = EnchantedBookItem.class, priority = 1500)
public class EnchantedBookItemMixinMixin {

    /**
     *
     * @param stack unnecessary
     * @param entry The EnchantmentLevelEntry, where I get the enchantment from
     * @param ci unnecessary (the callbackinfo of the mixin)
     * @param myCi (the callbackinfo of my mixin mixin)
     * @see moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.EnchantedBookItemMixin
     */
    @SuppressWarnings("CancellableInjectionUsage")
    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.EnchantedBookItemMixin",
            name = "Lmoriyashiine/enchancement/mixin/vanillachanges/enchantmentlimit/EnchantedBookItemMixin;enchancement$enchantmentLimit(Lnet/minecraft/item/ItemStack;Lnet/minecraft/enchantment/EnchantmentLevelEntry;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "HEAD"
            ), cancellable = true
    )
    private static void cursePatchEnchantedBook(ItemStack stack, EnchantmentLevelEntry entry, CallbackInfo ci, CallbackInfo myCi) {
        if (UnboundConfig.cursePatch && entry.enchantment.isCursed()) {
            myCi.cancel(); // imagine cancelling a cancel, just could not be what this line is for
        }
    }
}
