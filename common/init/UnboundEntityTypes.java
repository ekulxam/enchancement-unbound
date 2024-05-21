package survivalblock.enchancement_unbound.common.init;

import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.entity.AstralPhantomEntity;

public class UnboundEntityTypes {

    public static final EntityType<AstralPhantomEntity> ASTRAL_PHANTOM = FabricEntityTypeBuilder.create(SpawnGroup.MONSTER, AstralPhantomEntity::new).dimensions(EntityType.PHANTOM.getDimensions()).build();

    public static void init() {
        Registry.register(Registries.ENTITY_TYPE, EnchancementUnbound.id("astral_phantom"), ASTRAL_PHANTOM);
        FabricDefaultAttributeRegistry.register(ASTRAL_PHANTOM, AstralPhantomEntity.createAstralPhantomAttributes());
    }
}
