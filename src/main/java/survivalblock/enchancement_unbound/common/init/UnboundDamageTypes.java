package survivalblock.enchancement_unbound.common.init;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

public class UnboundDamageTypes {
    public static final RegistryKey<DamageType> SHIELDBOARD_COLLISION = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, EnchancementUnbound.id("shieldboard_collision"));
}
