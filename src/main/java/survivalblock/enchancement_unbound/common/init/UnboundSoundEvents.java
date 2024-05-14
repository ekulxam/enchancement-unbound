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

    public static void init(){
        Registry.register(Registries.SOUND_EVENT, ITEM_SHARD_USE.getId(), ITEM_SHARD_USE);
        Registry.register(Registries.SOUND_EVENT, ITEM_SHARD_CHIME.getId(), ITEM_SHARD_CHIME);
        Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_MIDAS_TOUCHED.getId(), ENTITY_GENERIC_MIDAS_TOUCHED);
        Registry.register(Registries.SOUND_EVENT, ENTITY_GENERIC_STATUE_UNDONE.getId(), ENTITY_GENERIC_STATUE_UNDONE);
    }
}
