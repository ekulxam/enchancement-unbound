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

    public AnvilScreenHandlerMixinMixin(@Nullable ScreenHandlerType<?> type, int syncId, PlayerInventory playerInventory, ScreenHandlerContext context) {
        super(type, syncId, playerInventory, context);
    }

    /**
     * Combines all the enchantments in the two inputs and sees if the output conforms to cursepatch's standards
     * @param cir SETRETURNVALUE
     */
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
        if (!UnboundConfig.cursePatch || ModConfig.enchantmentLimit == 0) {
            return;
        }
        ItemStack inputStack = this.input.getStack(0);
        ItemStack enchantedBookStack = this.input.getStack(1); // doesn't actually have to be an enchanted book
        ItemStack outputStack = inputStack.copy();
        for (var entry : EnchantmentHelper.get(enchantedBookStack).entrySet()) {
            outputStack.addEnchantment(entry.getKey(), entry.getValue());
        }
        cir.setReturnValue(!(EnchancementUtil.getNonDefaultEnchantmentsSize(outputStack, outputStack.getEnchantments().size()) > ModConfig.enchantmentLimit));
    }
}
