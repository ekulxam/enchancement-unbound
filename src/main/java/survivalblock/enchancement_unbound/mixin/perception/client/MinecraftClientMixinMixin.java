package survivalblock.enchancement_unbound.mixin.perception.client;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.MinecraftClient;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = MinecraftClient.class, priority = 1500)
public class MinecraftClientMixinMixin {
    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.perception.client.MinecraftClientMixin",
            name = "enchancement$perception"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;canSee(Lnet/minecraft/entity/Entity;)Z"
            )
    )
    private boolean nowIJustNeedAimbot(boolean canSee) {
        return !(UnboundConfig.perceptionUsersGetESP || canSee); // logically equivalent to !(UnboundConfig.perceptionUsersGetESP ? true : canSee)
        // inverse because the canSee is in an inverse
    }
}
