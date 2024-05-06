package survivalblock.enchancement_unbound.common.init;

import moriyashiine.enchancement.common.enchantment.AxeEnchantment;
import moriyashiine.enchancement.common.enchantment.EmptyEnchantment;
import moriyashiine.enchancement.common.enchantment.ShovelEnchantment;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.enchantment.*;

public class UnboundEnchantments {
    public static final Enchantment CURTAIN = new CurtainEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND);
    public static final Enchantment ASCENSION = new ShovelEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
    public static final Enchantment EXECUTIONER = new AxeEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.DIGGER, EquipmentSlot.MAINHAND);

    public static void init() {
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("curtain"), CURTAIN);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("ascension"), ASCENSION);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("executioner"), EXECUTIONER);
    }
}
