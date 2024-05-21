package survivalblock.enchancement_unbound.common.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

public class UnboundSoundEvents {
    public static final SoundEvent ITEM_SHARD_USE = SoundEvent.of(EnchancementUnbound.id("item.shard_minigun.use"));
    public static final SoundEvent ITEM_SHARD_CHIME = SoundEvent.of(EnchancementUnbound.id("item.shard_minigun.chime"));
    public static final SoundEvent ENTITY_GENERIC_MIDAS_TOUCHED = SoundEvent.of(EnchancementUnbound.id("entity.generic.midas_touched"));
    public static final SoundEvent ENTITY_GENERIC_STATUE_UNDONE = SoundEvent.of(EnchancementUnbound.id("entity.generic.statue_undone"));
    public static final SoundEvent ENTITY_ASTRAL_PHANTOM_FLAP = SoundEvent.of(EnchancementUnbound.id("entity.astral_phantom.flap"));
    public static final SoundEvent ENTITY_ASTRAL_PHANTOM_AMBIENT = SoundEvent.of(EnchancementUnbound.id("entity.astral_phantom.ambient"));
    public static final SoundEvent ENTITY_ASTRAL_PHANTOM_TELEPORT = SoundEvent.of(EnchancementUnbound.id("entity.astral_phantom.teleport"));
    public static final SoundEvent ENTITY_ASTRAL_PHANTOM_BITE = SoundEvent.of(EnchancementUnbound.id("entity.astral_phantom.bite"));
    public static final SoundEvent ENTITY_ASTRAL_PHANTOM_HURT = SoundEvent.of(EnchancementUnbound.id("entity.astral_phantom.hurt"));
    public static final SoundEvent ENTITY_ASTRAL_PHANTOM_DEATH = SoundEvent.of(EnchancementUnbound.id("entity.astral_phantom.death"));

    public static void init(){
        Registry.register(Registries.SOUND_EVENT, ITEM_SHARD_USE.getId(), ITEM_SHARD_USE);
        Registry.register(Registries.SOUND_EVENT, ITEM_SHARD_CHIME.getId(), ITEM_SHARD_CHIME);
        Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_MIDAS_TOUCHED.getId(), ENTITY_GENERIC_MIDAS_TOUCHED);
        Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_STATUE_UNDONE.getId(), ENTITY_GENERIC_STATUE_UNDONE);
        Registry.register(Registries.SOUND_EVENT, ENTITY_ASTRAL_PHANTOM_FLAP.getId(), ENTITY_ASTRAL_PHANTOM_FLAP);
        Registry.register(Registries.SOUND_EVENT, ENTITY_ASTRAL_PHANTOM_AMBIENT.getId(), ENTITY_ASTRAL_PHANTOM_AMBIENT);
        Registry.register(Registries.SOUND_EVENT, ENTITY_ASTRAL_PHANTOM_TELEPORT.getId(), ENTITY_ASTRAL_PHANTOM_TELEPORT);
        Registry.register(Registries.SOUND_EVENT, ENTITY_ASTRAL_PHANTOM_BITE.getId(), ENTITY_ASTRAL_PHANTOM_BITE);
        Registry.register(Registries.SOUND_EVENT, ENTITY_ASTRAL_PHANTOM_HURT.getId(), ENTITY_ASTRAL_PHANTOM_HURT);
        Registry.register(Registries.SOUND_EVENT, ENTITY_ASTRAL_PHANTOM_DEATH.getId(), ENTITY_ASTRAL_PHANTOM_DEATH);
    }
}
