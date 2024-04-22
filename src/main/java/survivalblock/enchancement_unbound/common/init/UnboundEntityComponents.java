package survivalblock.enchancement_unbound.common.init;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.component.AscensionComponent;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;
import survivalblock.enchancement_unbound.common.component.ShieldStackComponent;
import survivalblock.enchancement_unbound.common.entity.ProjectedShieldEntity;
import survivalblock.enchancement_unbound.common.entity.ShieldboardEntity;

public class UnboundEntityComponents implements EntityComponentInitializer {
    public static final ComponentKey<ShieldStackComponent> SHIELD_STACK = ComponentRegistry.getOrCreate(EnchancementUnbound.id("shield_stack"), ShieldStackComponent.class);
    public static final ComponentKey<CurtainComponent> CURTAIN = ComponentRegistry.getOrCreate(EnchancementUnbound.id("curtain"), CurtainComponent.class);
    public static final ComponentKey<AscensionComponent> ASCENSION = ComponentRegistry.getOrCreate(EnchancementUnbound.id("ascension"), AscensionComponent.class);
    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(ShieldboardEntity.class, SHIELD_STACK, ShieldStackComponent::new);
        registry.registerFor(ProjectedShieldEntity.class, SHIELD_STACK, ShieldStackComponent::new);
        registry.registerFor(LivingEntity.class, CURTAIN, CurtainComponent::new);
        registry.registerFor(PlayerEntity.class, ASCENSION, AscensionComponent::new);
    }
}
