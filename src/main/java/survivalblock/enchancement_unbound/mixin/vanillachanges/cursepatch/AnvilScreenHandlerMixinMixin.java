package survivalblock.enchancement_unbound.mixin.vanillachanges.cursepatch;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtList;
import net.minecraft.screen.AnvilScreenHandler;
import net.minecraft.screen.ForgingScreenHandler;
import net.minecraft.screen.ScreenHandlerContext;
import net.minecraft.screen.ScreenHandlerType;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

import java.util.Map;

@Debug(export = true)
@Mixin(value = AnvilScreenHandler.class, priority = 1500)
public abstract class AnvilScreenHandlerMixinMixin extends ForgingScreenHandler {

    @Unique
    private Enchantment curse = null;

    public AnvilScreenHandlerMixinMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    @Inject(method = "updateResult", at = @At(value = "INVOKE", target = "Lnet/minecraft/enchantment/Enchantment;canCombine(Lnet/minecraft/enchantment/Enchantment;)Z", shift = At.Shift.BEFORE))
    private void getThatCurse(CallbackInfo ci, @Local(ordinal = 1) Enchantment enchantment2){
        curse = enchantment2;
    }

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.enchantmentlimit.AnvilScreenHandlerMixin",
            name = "Lmoriyashiine/enchancement/mixin/vanillachanges/enchantmentlimit/AnvilScreenHandlerMixin;enchancement$enchantmentLimit(Z)Z"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "HEAD"
            ), cancellable = true
    )
    private void cursePatchAnvil(CallbackInfoReturnable<Boolean> cir) {
        if (!UnboundConfig.cursePatch) {
            return;
        }
        if (curse != null && curse.isCursed()) {
            cir.setReturnValue(true);
            return;
        }
        ItemStack stack = this.input.getStack(0);
        ItemStack otherStack = this.input.getStack(1);
        if (EnchancementUtil.limitCheck(false,
                (EnchancementUtil.getNonDefaultEnchantmentsSize(otherStack, otherStack.getEnchantments().size() + 1))
                        > ModConfig.enchantmentLimit) || (EnchancementUtil.getNonDefaultEnchantmentsSize(stack, stack.getEnchantments().size()))
                > ModConfig.enchantmentLimit) {
            cir.setReturnValue(false);
        } else {
            cir.setReturnValue(true);
        }
    }
}
