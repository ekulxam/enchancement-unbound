package survivalblock.enchancement_unbound.common.enchantment;

import moriyashiine.enchancement.common.enchantment.EmptyEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

public class CurtainEnchantment extends UnboundHoeEnchantment {
    public CurtainEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }
}
