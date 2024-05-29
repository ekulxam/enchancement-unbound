package survivalblock.enchancement_unbound.common.enchantment;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;

public class UnboundHoeEnchantment extends Enchantment {

    public UnboundHoeEnchantment(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isAcceptableItem(ItemStack stack) {
        return stack.isIn(ItemTags.HOES);
    }
}
