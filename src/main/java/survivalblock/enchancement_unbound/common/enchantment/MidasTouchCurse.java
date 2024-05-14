package survivalblock.enchancement_unbound.common.enchantment;

import moriyashiine.enchancement.common.enchantment.EmptyEnchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;

public class MidasTouchCurse extends EmptyEnchantment {

    public static boolean golden;

    public MidasTouchCurse(Rarity weight, EnchantmentTarget target, EquipmentSlot... slotTypes) {
        super(weight, target, slotTypes);
    }

    @Override
    public boolean isCursed() {
        return true;
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        if (stack.getItem() instanceof ToolItem toolItem) {
            return toolItem.getMaterial().equals(ToolMaterials.GOLD) || toolItem.getMaterial().equals(ToolMaterials.NETHERITE);
        }
        return false;
    }

    @Override
    public boolean isTreasure() {
        return true;
    }
}