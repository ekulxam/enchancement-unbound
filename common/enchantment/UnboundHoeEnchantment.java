package survivalblock.enchancement_unbound.common.enchantment;

import moriyashiine.enchancement.common.enchantment.EmptyEnchantment;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

public class UnboundHoeEnchantment extends EmptyEnchantment {
    public UnboundHoeEnchantment(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    public UnboundHoeEnchantment(int maxLevel, Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(maxLevel, weight, type, slotTypes);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isIn(ItemTags.HOES);
    }
}
