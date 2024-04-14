package survivalblock.enchancement_unbound.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;

public class ExpulsionEnchantment extends UnboundShieldEnchantment {

    public ExpulsionEnchantment(int maxLevel, Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(maxLevel, weight, type, slotTypes);
    }
}
