package survivalblock.enchancement_unbound.mixin.vanillachanges.cursepatch;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.Entity;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.command.EnchantCommand;
import net.minecraft.server.command.ServerCommandSource;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

import java.util.Collection;

@Debug(export = true)
@Mixin(value = EnchantCommand.class, priority = 1500)
public class EnchantCommandMixinMixin {

    // why moriyaaaa
    @Unique
    private static Enchantment curse = null;

    /**
     * Assigns the enchantment being applied to a static threadlocal
     * @param source unnecessary
     * @param targets unnecessary
     * @param enchantment unnecessary
     * @param level unnecessary
     * @param cir unnecessary
     * @param enchantment2 The curse enchantment, captured with @Local
     */
    @Inject(method = "execute", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;getMaxLevel()I", shift = At.Shift.BEFORE))
    private static void getAppliedEnchantment(ServerCommandSource source, Collection<? extends Entity> targets, RegistryEntry<Enchantment> enchantment, int level, CallbackInfoReturnable<Integer> cir, @Local(ordinal = 0) Enchantment enchantment2){
        curse = enchantment2;
    }

    /**
     * Overrides the other mixin to return true if the enchantment being applied is a curse
     * @see moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.EnchantCommandMixin
     * @param cir self-explanatory
     */
    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.EnchantCommandMixin",
            name = "Lmoriyashiine/enchancement/mixin/vanillachanges/enchantmentlimit/EnchantCommandMixin;enchancement$enchantmentLimit(Z)Z"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "HEAD"
            ), cancellable = true
    )
    private static void cursePatchEnchantCommand(CallbackInfoReturnable<Boolean> cir) {
        if (UnboundConfig.cursePatch && curse != null && curse.isCursed()) {
            cir.setReturnValue(true);
        }
    }
}
