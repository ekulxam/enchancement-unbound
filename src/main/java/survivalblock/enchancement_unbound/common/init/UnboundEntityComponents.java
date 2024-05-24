package survivalblock.enchancement_unbound.common.init;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import dev.onyxstudios.cca.api.v3.entity.RespawnCopyStrategy;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.component.AscensionComponent;
import survivalblock.enchancement_unbound.common.component.BrimstoneBypassComponent;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;
import survivalblock.enchancement_unbound.common.component.MidasTouchComponent;

public class UnboundEntityComponents implements EntityComponentInitializer {

    public static final ComponentKey<CurtainComponent> CURTAIN = ComponentRegistry.getOrCreate(EnchancementUnbound.id("curtain"), CurtainComponent.class);

    public static final ComponentKey<AscensionComponent> ASCENSION = ComponentRegistry.getOrCreate(EnchancementUnbound.id("ascension"), AscensionComponent.class);

    public static final ComponentKey<MidasTouchComponent> MIDAS_TOUCH = ComponentRegistry.getOrCreate(EnchancementUnbound.id("midas_touch"), MidasTouchComponent.class);

    public static final ComponentKey<BrimstoneBypassComponent> BRIMSTONE_BYPASS = ComponentRegistry.getOrCreate(EnchancementUnbound.id("brimstone_bypass"), BrimstoneBypassComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(BrimstoneEntity.class, BRIMSTONE_BYPASS, BrimstoneBypassComponent::new);
        registry.registerFor(LivingEntity.class, MIDAS_TOUCH, MidasTouchComponent::new);
        registry.registerForPlayers(MIDAS_TOUCH, MidasTouchComponent::new, RespawnCopyStrategy.CHARACTER);
        registry.registerForPlayers(ASCENSION, AscensionComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
        registry.registerForPlayers(CURTAIN, CurtainComponent::new, RespawnCopyStrategy.LOSSLESS_ONLY);
    }
}
