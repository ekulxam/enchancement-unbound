package survivalblock.enchancement_unbound.common.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.entity.AstralPhantomEntity;
import survivalblock.enchancement_unbound.common.entity.SentientPantsEntity;

public class UnboundEntityTypes {

    public static final EntityType<AstralPhantomEntity> ASTRAL_PHANTOM = registerEntity("astral_phantom", SpawnGroup.MONSTER, AstralPhantomEntity::new, EntityType.PHANTOM.getDimensions(), false);
    public static final EntityType<SentientPantsEntity> SENTIENT_PANTS = registerEntity("sentient_pants", SpawnGroup.MISC, SentientPantsEntity::new, EntityDimensions.changing(0.6f, 1f), false);

    @SuppressWarnings("SameParameterValue")
    private static <T extends Entity> EntityType<T> registerEntity(String name, SpawnGroup group, EntityType.EntityFactory<T> factory , EntityDimensions dimensions, boolean fireImmune) {
        EntityType.Builder<T> builder = EntityType.Builder.create(factory, group).dimensions(dimensions.width(), dimensions.height());
        if (fireImmune) {
            builder = builder.makeFireImmune();
        }
        return Registry.register(Registries.ENTITY_TYPE, EnchancementUnbound.id(name),
                builder.build());
    }

    public static void init() {
        FabricDefaultAttributeRegistry.register(ASTRAL_PHANTOM, AstralPhantomEntity.createAstralPhantomAttributes());
        FabricDefaultAttributeRegistry.register(SENTIENT_PANTS, SentientPantsEntity.createPantsAttributes());
    }
}
