package survivalblock.enchancement_unbound.common.init;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

public class UnboundTags {
    public static final TagKey<EntityType<?>> CANNOT_EXECUTE = TagKey.of(Registries.ENTITY_TYPE.getKey(), EnchancementUnbound.id("cannot_execute"));
    public static final TagKey<DamageType> BYPASSES_MIDAS_LINK = TagKey.of(RegistryKeys.DAMAGE_TYPE, EnchancementUnbound.id("bypasses_midas_link"));
}
