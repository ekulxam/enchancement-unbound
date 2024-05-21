package survivalblock.enchancement_unbound.common.init;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

public class UnboundDamageTypes {
    public static final RegistryKey<DamageType> SLAM_IMPACT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, EnchancementUnbound.id("slam_impact"));
    public static final RegistryKey<DamageType> MIDAS_LINK = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, EnchancementUnbound.id("midas_link"));
    public static final RegistryKey<DamageType> FORCED_EXPERIENCE = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, EnchancementUnbound.id("forced_experience"));
}
