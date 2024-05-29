package survivalblock.enchancement_unbound.mixin.descriptions.client;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.client.event.EnchantmentDescriptionsEvent;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;

import java.util.List;

@Mixin(value = EnchantmentDescriptionsEvent.class, remap = false)
public class EnchantmentDescriptionsEventMixin {

    @Inject(method = "lambda$getTooltip$0", at = @At(value = "INVOKE", target = "Ljava/util/List;add(ILjava/lang/Object;)V", shift = At.Shift.AFTER))
    private static void addMidasTouchLore(List<Text> lines, RegistryEntry<Enchantment> enchantment, CallbackInfo ci, @Local int i){
        if (!UnboundConfig.unboundEnchantments) {
            return;
        }
        if (!enchantment.value().equals(UnboundEnchantments.MIDAS_TOUCH)) {
            return;
        }
        lines.add(i + 1, Text.translatable("enchantment.enchancement_unbound.midas_touch.lore").formatted(Formatting.YELLOW));
    }
}
