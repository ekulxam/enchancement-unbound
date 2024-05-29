package survivalblock.enchancement_unbound.client.event;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModDataComponentTypes;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.client.item.TooltipType;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import survivalblock.enchancement_unbound.common.UnboundConfig;

import java.util.List;

public class EnchantedCrossbowsHaveMultishotEvent implements ItemTooltipCallback {

    @Override
    public void getTooltip(ItemStack stack, Item.TooltipContext tooltipContext, TooltipType tooltipType, List<Text> lines) {
        if (UnboundConfig.allCrossbowsHaveMultishot && stack.hasEnchantments() && stack.isIn(ItemTags.CROSSBOW_ENCHANTABLE) && stack.contains(ModDataComponentTypes.TOGGLEABLE_PASSIVE)) {
            MutableText icon = Text.literal("× ");
            Formatting formatting = Formatting.DARK_RED;
            if (stack.get(ModDataComponentTypes.TOGGLEABLE_PASSIVE)) {
                icon = Text.literal("✔ ");
                formatting = Formatting.DARK_GREEN;
            }
            lines.add(1, icon.append(Text.translatable("tooltip.enchancement_unbound.has_multishot")).formatted(formatting));
        }
    }
}