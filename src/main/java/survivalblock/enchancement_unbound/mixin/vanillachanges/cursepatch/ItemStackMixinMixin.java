package survivalblock.enchancement_unbound.mixin.vanillachanges.cursepatch;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = ItemStack.class, priority = 1500)
public class ItemStackMixinMixin {

    /**
     * Prevents the mixin into ItemStack enchanting logic from cancelling by cancelling it
     * @param enchantment the (curse) enchantment
     * @param level the level of the enchantment (probably)
     * @param ci callbackinfo of the mixin
     * @param myCi callbackinfo of my mixin mixin
     * @see moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.ItemStackMixin
     */
    @SuppressWarnings("CancellableInjectionUsage") // goofy double ci
    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.ItemStackMixin",
            name = "Lmoriyashiine/enchancement/mixin/vanillachanges/enchantmentlimit/ItemStackMixin;enchancement$enchantmentLimit(Lnet/minecraft/enchantment/Enchantment;ILorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "HEAD"
            ), cancellable = true
    )
    private void cursePatchItemStack(Enchantment enchantment, int level, CallbackInfo ci, CallbackInfo myCi) {
        // :mixinthink: I love having two callbackinfo parameters
        if (UnboundConfig.cursePatch && enchantment.isCursed()) {
            myCi.cancel();
        }
    }
}
