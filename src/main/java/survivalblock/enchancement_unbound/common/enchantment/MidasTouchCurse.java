package survivalblock.enchancement_unbound.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ToolItem;
import net.minecraft.item.ToolMaterials;

public class MidasTouchCurse extends Enchantment {

    public static boolean golden;

    public MidasTouchCurse(Properties properties) {
        super(properties);
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