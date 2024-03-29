package survivalblock.enchancement_unbound.mixin.veil;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Debug(export = true)
@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyReturnValue(method = "isInvisible", at = @At(value = "RETURN"))
    private boolean arathainMoment(boolean mouthpiece){
        if(UnboundConfig.veilUsersAlwaysInvisible){
            return ((Entity) (Object) this instanceof LivingEntity living && EnchantmentHelper.getEquipmentLevel(ModEnchantments.VEIL, living) > 0) || mouthpiece;
        }
        // this should work for both of them, I think (didn't specify ordinal)
        return mouthpiece;
    }
}
