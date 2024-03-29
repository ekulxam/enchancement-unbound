package survivalblock.enchancement_unbound.mixin.veil;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @WrapOperation(method = "canTakeDamage", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;isInvulnerable()Z"))
    private boolean setInvulnerableIfHasVeil(LivingEntity instance, Operation<Boolean> original){
        if (UnboundConfig.veilEqualsGhost && instance instanceof PlayerEntity) {
            return original.call(instance) || EnchantmentHelper.getEquipmentLevel(ModEnchantments.VEIL, instance) > 0;
        }
        return original.call(instance);
    }
}
