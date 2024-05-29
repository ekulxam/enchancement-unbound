package survivalblock.enchancement_unbound.common.enchantment;

import moriyashiine.enchancement.common.ModConfig;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;

public class SoulReaperCurse extends UnboundHoeEnchantment {

    public SoulReaperCurse(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isCursed() {
        return true;
    }

    @Override
    public Text getName(int level) {
        Text text = super.getName(level);
        return ModConfig.coloredEnchantmentNames ? text.copy().formatted(Formatting.AQUA) : text;
    }
}
