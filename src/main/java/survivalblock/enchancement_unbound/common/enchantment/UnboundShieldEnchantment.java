package survivalblock.enchancement_unbound.common.enchantment;

import moriyashiine.enchancement.common.enchantment.EmptyEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

public class UnboundShieldEnchantment extends EmptyEnchantment {
    public UnboundShieldEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    public UnboundShieldEnchantment(int maxLevel, Enchantment.Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(maxLevel, weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return UnboundUtil.isAShield(stack);
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return super.canAccept(other) && UnboundUtil.cancelShieldEnchantments(this, other);
    }
}
