package survivalblock.enchancement_unbound.common.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.enchantment.AegisEnchantment;
import survivalblock.enchancement_unbound.common.enchantment.ExpulsionEnchantment;
import survivalblock.enchancement_unbound.common.enchantment.RapidEnchantment;
import survivalblock.enchancement_unbound.common.enchantment.ShieldSurfEnchantment;

public class UnboundEnchantments {
    public static final Enchantment SHIELD_SURF = new ShieldSurfEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
    public static final Enchantment AEGIS = new AegisEnchantment(Enchantment.Rarity.VERY_RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
    public static final Enchantment RAPID = new RapidEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
    public static final Enchantment EXPULSION = new ExpulsionEnchantment(6, Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);

    public static void init() {
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("shield_surf"), SHIELD_SURF);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("aegis"), AEGIS);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("rapid"), RAPID);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("expulsion"), EXPULSION);
    }
}
