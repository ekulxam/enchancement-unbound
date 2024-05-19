package survivalblock.enchancement_unbound.mixin.horseshoes;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.HorseEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@SuppressWarnings({"ConstantValue", "UnreachableCode"})
@Mixin(Entity.class)
public class EntityMixin {

    @ModifyReturnValue(method = "shouldDismountUnderwater", at = @At("RETURN"))
    private boolean noBuoyDismount(boolean original){
        if (UnboundConfig.horseshoes) {
            return original && !((Entity) (Object) this instanceof HorseEntity horse && EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, horse));
        }
        return original;
    }
}
