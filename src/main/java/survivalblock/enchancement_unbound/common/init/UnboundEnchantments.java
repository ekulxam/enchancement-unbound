package survivalblock.enchancement_unbound.common.init;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.enchantment.*;
import survivalblock.enchancement_unbound.mixin.util.init.ModEnchantmentsAccessor;

public class UnboundEnchantments {
    public static final Enchantment APPLE_SAUCE = new UnboundHoeEnchantment(properties(ItemTags.HOES, 3, EquipmentSlot.MAINHAND));
    public static final Enchantment SOUL_REAPER = new SoulReaperCurse(properties(ItemTags.HOES, 1, EquipmentSlot.MAINHAND));
    public static final Enchantment ASCENSION = new Enchantment(properties(ItemTags.SHOVELS, 1, EquipmentSlot.MAINHAND, EquipmentSlot.OFFHAND));
    public static final Enchantment EXECUTIONER = new Enchantment(properties(ItemTags.AXES, 1, EquipmentSlot.MAINHAND));
    public static final Enchantment MIDAS_TOUCH = new MidasTouchCurse(properties(ItemTags.WEAPON_ENCHANTABLE, 1, EquipmentSlot.MAINHAND));
    public static final Enchantment SENTIENT_PANTS = new Enchantment(properties(ItemTags.LEG_ARMOR_ENCHANTABLE, 1, EquipmentSlot.LEGS));

    public static Enchantment.Properties properties(TagKey<Item> supportedItems, int maxLevel, EquipmentSlot... slots) {
        return ModEnchantmentsAccessor.enchancement_unbound$invokeProperties(supportedItems, maxLevel, slots);
    }

    public static void init() {
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("ascension"), ASCENSION);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("executioner"), EXECUTIONER);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("midas_touch"), MIDAS_TOUCH);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("apple_sauce"), APPLE_SAUCE);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("soul_reaper"), SOUL_REAPER);
        Registry.register(Registries.ENCHANTMENT, EnchancementUnbound.id("sentient_pants"), SENTIENT_PANTS);
    }
}
