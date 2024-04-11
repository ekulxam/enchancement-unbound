package survivalblock.enchancement_unbound.common.enchantment;

import moriyashiine.enchancement.common.enchantment.EmptyEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;

public class LifelineEnchantment extends EmptyEnchantment {
    public LifelineEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.getItem() instanceof ShieldItem;
    }

    @Override
    protected boolean canAccept(Enchantment other) {
        return super.canAccept(other) && other != UnboundEnchantments.SHIELD_SURF && other != UnboundEnchantments.INCINERATE;
    }
}
