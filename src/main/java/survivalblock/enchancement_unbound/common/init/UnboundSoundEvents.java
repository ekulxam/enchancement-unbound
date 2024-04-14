package survivalblock.enchancement_unbound.common.init;

import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.SoundEvent;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

public class UnboundSoundEvents {

    public static final SoundEvent ITEM_BOW_HEX = SoundEvent.of(EnchancementUnbound.id("item.bow.hex"));
    public static final SoundEvent ENTITY_PROJECTED_SHIELD_HIT = SoundEvent.of(EnchancementUnbound.id("entity.projected_shield.hit"));

    public static void init(){
        Registry.register(Registries.SOUND_EVENT, ITEM_BOW_HEX.getId(), ITEM_BOW_HEX);
        Registry.register(Registries.SOUND_EVENT, ENTITY_PROJECTED_SHIELD_HIT.getId(), ENTITY_PROJECTED_SHIELD_HIT);
    }
}
