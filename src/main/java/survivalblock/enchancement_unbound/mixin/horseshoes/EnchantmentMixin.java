package survivalblock.enchancement_unbound.mixin.horseshoes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

import java.util.Map;
import java.util.Optional;

@Mixin(Enchantment.class)
public class EnchantmentMixin {

    @Shadow @Final private Enchantment.Properties properties;

    @SuppressWarnings("UnreachableCode")
    @ModifyReturnValue(method = "isAcceptableItem", at = @At("RETURN"))
    private boolean horseshoes(boolean original, ItemStack stack){
        if (!UnboundConfig.horseshoes) {
            return original;
        }
        if (UnboundUtil.isValidHorseshoeEnchantment((Enchantment) (Object) this)) {
            return original || UnboundUtil.isHorseArmor(stack.getItem());
        }
        return original;
    }

}
