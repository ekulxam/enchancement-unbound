package survivalblock.enchancement_unbound.mixin.util.init;

import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.registry.tag.TagKey;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ModEnchantments.class)
public interface ModEnchantmentsAccessor {

    @Invoker(value = "properties")
    static Enchantment.Properties invokeProperties(TagKey<Item> supportedItems, int maxLevel, EquipmentSlot... slots) {
        throw new UnsupportedOperationException();
    }
}
