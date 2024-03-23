package survivalblock.enchancement_unbound.common.init;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.*;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Rarity;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.item.OrbitalStrikeCannonItem;
import survivalblock.enchancement_unbound.common.item.ShardMinigunItem;

import java.util.ArrayList;
import java.util.List;

public class UnboundItems {

    public static List<Item> unboundCombatItemList = new ArrayList<>();
    public static final Item ORBITAL_STRIKE_BRIMSTONE = registerItem("orbital_strike_brimstone", new OrbitalStrikeCannonItem(5, new FabricItemSettings().rarity(Rarity.EPIC).maxCount(1)));
    public static final Item AMETHYST_SHARD_MINIGUN = registerItem("amethyst_shard_minigun", new ShardMinigunItem(new FabricItemSettings().rarity(Rarity.EPIC).maxCount(1)));
    private static Item registerItem(String name, Item item) {
        unboundCombatItemList.add(item);
        return Registry.register(Registries.ITEM, EnchancementUnbound.id(name), item);
    }
    public static void init() {
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.COMBAT).register(content -> unboundCombatItemList.forEach(content::add));
    }
}
