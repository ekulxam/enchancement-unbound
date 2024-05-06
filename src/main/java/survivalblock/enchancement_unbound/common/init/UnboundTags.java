package survivalblock.enchancement_unbound.common.init;

import moriyashiine.enchancement.common.Enchancement;
import net.minecraft.entity.EntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.tag.TagKey;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

public class UnboundTags {
    public static final TagKey<EntityType<?>> CANNOT_EXECUTE = TagKey.of(Registries.ENTITY_TYPE.getKey(), EnchancementUnbound.id("cannot_execute"));
}
