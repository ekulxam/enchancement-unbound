package survivalblock.enchancement_unbound.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;

public class AegisEnchantment extends UnboundShieldEnchantment {
    public AegisEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }
}
