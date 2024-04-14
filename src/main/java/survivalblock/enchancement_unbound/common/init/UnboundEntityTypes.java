package survivalblock.enchancement_unbound.common.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.entity.ProjectedShieldEntity;
import survivalblock.enchancement_unbound.common.entity.ShieldboardEntity;

public class UnboundEntityTypes {

    public static final EntityType<ShieldboardEntity> SHIELDBOARD = registerEntity("shieldboard", SpawnGroup.MISC, ShieldboardEntity::new, EntityDimensions.fixed(0.85f, 0.1f), true);

    public static final EntityType<ProjectedShieldEntity> PROJECTED_SHIELD = registerEntity("projected_shield", SpawnGroup.MISC, ProjectedShieldEntity::new, EntityDimensions.fixed(0.5f, 1.3f), false);

    private static <T extends Entity> EntityType<T> registerEntity(String name, SpawnGroup group, EntityType.EntityFactory<T> factory , EntityDimensions dimensions, boolean fireImmune) {
        if (fireImmune) {
            return Registry.register(Registries.ENTITY_TYPE, EnchancementUnbound.id(name),
                    FabricEntityTypeBuilder.create(group, factory).dimensions(dimensions).fireImmune().build());
        }
        return Registry.register(Registries.ENTITY_TYPE, EnchancementUnbound.id(name),
                FabricEntityTypeBuilder.create(group, factory).dimensions(dimensions).build());
    }

    public static void init(){

    }
}
