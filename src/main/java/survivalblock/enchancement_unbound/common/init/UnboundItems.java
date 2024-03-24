package survivalblock.enchancement_unbound.common.init;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Rarity;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.item.OrbitalStrikeCannonItem;
import survivalblock.enchancement_unbound.common.item.ShardMinigunItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UnboundItems {

    private static Map<Item, RegistryKey<ItemGroup>> map = new HashMap<>();
    public static final Item ORBITAL_STRIKE_BRIMSTONE = registerItem("orbital_strike_brimstone", new OrbitalStrikeCannonItem(new FabricItemSettings().rarity(Rarity.EPIC).maxCount(1)), null);
    public static final Item AMETHYST_SHARD_MINIGUN = registerItem("amethyst_shard_minigun", new ShardMinigunItem(new FabricItemSettings().rarity(Rarity.EPIC).maxCount(1)), ItemGroups.COMBAT);
    private static Item registerItem(String name, Item item, RegistryKey<ItemGroup> group) {
        map.put(item, group);
        return Registry.register(Registries.ITEM, EnchancementUnbound.id(name), item);
    }
    public static void init() {
        for(var entry : map.entrySet()){
            if(entry.getValue() != null){
                ItemGroupEvents.modifyEntriesEvent(entry.getValue()).register(content -> content.add(entry.getKey()));
            }
        }
    }
}
