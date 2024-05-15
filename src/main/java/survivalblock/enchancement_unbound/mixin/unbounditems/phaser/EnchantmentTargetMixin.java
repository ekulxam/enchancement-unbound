package survivalblock.enchancement_unbound.mixin.unbounditems.phaser;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.item.UnboundSlingshotItem;

@Mixin(EnchantmentTarget.class)
public class EnchantmentTargetMixin {
    @Mixin(targets = "net.minecraft.enchantment.EnchantmentTarget$3")
    public static class BOWMixin {
        // I finally figured out which inner class it is
        @ModifyReturnValue(method = "isAcceptableItem", at = @At("RETURN"))
        private boolean phaserToo(boolean original, Item item){
            return original || item instanceof UnboundSlingshotItem;
        }
    }
}
