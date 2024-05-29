package survivalblock.enchancement_unbound.mixin.vanillachanges.superquickcharge;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = CrossbowItem.class, priority = 2000)
public class CrossbowItemMixin {

    @Inject(
            method = {"getPullTime"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private static void theLagIsNotReal(ItemStack stack, CallbackInfoReturnable<Integer> cir) {
        if (UnboundConfig.superQuickChargeCrossbow) {
            cir.setReturnValue(5);
        }
    }

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.brimstone.CrossbowItemMixin",
            name = "Lmoriyashiine/enchancement/mixin/brimstone/CrossbowItemMixin;enchancement$brimstone(ILnet/minecraft/item/ItemStack;)I"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/lang/Math;max(FF)F",
                    remap = false
            )
    )
    private static float rightClickToObliterate(float original) {
        return UnboundConfig.superQuickChargeCrossbow ? 6 : original;
    }
}
