package survivalblock.enchancement_unbound.mixin.horseshoes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.item.HorseArmorItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(Item.class)
public class ItemMixin {

    @SuppressWarnings("ConstantValue")
    @ModifyReturnValue(method = "getEnchantability", at = @At("RETURN"))
    private int horseArmorEnchantable(int original){
        if (UnboundConfig.horseshoes && (Item) (Object) this instanceof HorseArmorItem) {
            return Math.max(original, 1);
        }
        return original;
    }
}
