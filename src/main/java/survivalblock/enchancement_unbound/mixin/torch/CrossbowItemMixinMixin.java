package survivalblock.enchancement_unbound.mixin.torch;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = CrossbowItem.class, priority = 1500)
public class CrossbowItemMixinMixin {

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.torch.CrossbowItemMixin",
            name = "Lmoriyashiine/enchancement/mixin/torch/CrossbowItemMixin;enchancement$torch(Lnet/minecraft/item/ItemStack;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;)V"
    )
    @ModifyArg(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfoReturnable;setReturnValue(Ljava/lang/Object;)V",
                    remap = false
            )
    )
    private static Object iAmBecomeTorchBurnerOfWorlds(Object original) {
        return UnboundConfig.noCrossbowCooldown || UnboundConfig.superQuickChargeCrossbow ? 1 : original;
    }
}
