package survivalblock.enchancement_unbound.mixin.disarm;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.FishingBobberEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = FishingBobberEntity.class, priority = 1500)
public class FishingBobberEntityMixinMixin {

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.disarm.FishingBobberEntityMixin",
            name = "Lmoriyashiine/enchancement/mixin/disarm/FishingBobberEntityMixin;enchancment$disarm(Lnet/minecraft/entity/Entity;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V"
    )
    @WrapOperation(
            method = "@MixinSquared:Handler",
            constant = @Constant(classValue = PlayerEntity.class, ordinal = 0)
    )
    private boolean stealPlayerItems(Object obj, Operation<Boolean> original) {
        return original.call(obj) && !UnboundConfig.disarmStealsPlayerItems;
    }
}
