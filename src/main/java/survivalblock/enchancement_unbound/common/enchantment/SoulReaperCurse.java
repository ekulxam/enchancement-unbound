package survivalblock.enchancement_unbound.common.enchantment;

import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SoulReaperCurse extends UnboundHoeEnchantment {
    public SoulReaperCurse(Rarity weight, EnchantmentTarget type, EquipmentSlot... slotTypes) {
        super(weight, type, slotTypes);
    }

    @Override
    public boolean isCursed() {
        return true;
    }

    @Override
    public Text getName(int level) {
        Text text = super.getName(level);
        return text.copy().formatted(Formatting.AQUA);
    }
}
