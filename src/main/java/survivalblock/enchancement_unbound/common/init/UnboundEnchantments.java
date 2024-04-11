package survivalblock.enchancement_unbound.common.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentTarget;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.enchantment.LifelineEnchantment;
import survivalblock.enchancement_unbound.common.enchantment.IncinerateEnchantment;
import survivalblock.enchancement_unbound.common.enchantment.ShieldSurfEnchantment;

public class UnboundEnchantments {
    public static final Enchantment SHIELD_SURF = new ShieldSurfEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
    public static final Enchantment AEGIS = new LifelineEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);
    public static final Enchantment INCINERATE = new IncinerateEnchantment(Enchantment.Rarity.RARE, EnchantmentTarget.FISHING_ROD, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND);

    public static void init() {
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("shield_surf"), SHIELD_SURF);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("aegis"), AEGIS);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("incinerate"), INCINERATE);
    }
}
