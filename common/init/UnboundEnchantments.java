package survivalblock.enchancement_unbound.common.init;

import moriyashiine.enchancement.common.enchantment.AxeEnchantment;
import moriyashiine.enchancement.common.enchantment.EmptyEnchantment;
import moriyashiine.enchancement.common.enchantment.ShovelEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.enchantment.*;

public class UnboundEnchantments {
    public static final Enchantment APPLE_SAUCE = new UnboundHoeEnchantment(3, Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND);
    public static final Enchantment SOUL_REAPER = new SoulReaperCurse(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND);
    public static final Enchantment ASCENSION = new ShovelEnchantment(Enchantment.Rarity.UNCOMMON, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
    public static final Enchantment EXECUTIONER = new AxeEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);
    public static final Enchantment MIDAS_TOUCH = new MidasTouchCurse(Enchantment.Rarity.RARE, EnchantmentTarget.WEAPON, EquipmentSlot.MAINHAND);

    public static void init() {
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("ascension"), ASCENSION);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("executioner"), EXECUTIONER);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("midas_touch"), MIDAS_TOUCH);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("apple_sauce"), APPLE_SAUCE);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("soul_reaper"), SOUL_REAPER);
    }
}
