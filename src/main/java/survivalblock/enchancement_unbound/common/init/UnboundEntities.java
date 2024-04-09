package survivalblock.enchancement_unbound.common.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.entity.ShieldboardEntity;

public class UnboundEntities {

    public static final EntityType<ShieldboardEntity> SHIELDBOARD = registerEntity("shieldboard", SpawnGroup.MISC, ShieldboardEntity::new, EntityDimensions.changing(1.3f, 0.1f));

    private static <T extends Entity> EntityType<T> registerEntity(String name, SpawnGroup group, EntityType.EntityFactory<T> factory , EntityDimensions dimensions) {
        return Registry.register(Registries.ENTITY_TYPE, EnchancementUnbound.id(name),
                FabricEntityTypeBuilder.create(group, factory).dimensions(dimensions).build());
    }

    public static void init(){

    }
}
