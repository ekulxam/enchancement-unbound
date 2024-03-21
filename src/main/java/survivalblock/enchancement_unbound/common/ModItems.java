package survivalblock.enchancement_unbound.common;

import net.fabricmc.fabric.api.entity.event.v1.FabricElytraItem;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.block.Block;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.minecraft.util.Rarity;

public class ModItems {

    public static final Item ORBITAL_STRIKE_BRIMSTONE = registerItem("orbital_strike_brimstone", new OrbitalStrikeBrimstoneCannonItem(new FabricItemSettings().rarity(Rarity.EPIC)));

    private static Item registerItem(String name, Item item) {
        return Registry.register(Registries.ITEM, EnchancementUnbound.id(name), item);
    }
    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> {
            content.add(ORBITAL_STRIKE_BRIMSTONE);
        });
    }
}
