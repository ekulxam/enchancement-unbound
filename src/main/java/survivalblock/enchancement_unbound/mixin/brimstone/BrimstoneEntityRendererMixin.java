package survivalblock.enchancement_unbound.mixin.brimstone;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.client.render.entity.BrimstoneEntityRenderer;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = BrimstoneEntityRenderer.class, priority = 1500)
public class BrimstoneEntityRendererMixin {

    @ModifyExpressionValue(method = "render(Lmoriyashiine/enchancement/common/entity/projectile/BrimstoneEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/entity/projectile/BrimstoneEntity;getDamage()D"))
    private double enforceSizeLimit(double damage) {
        return Math.min(damage, UnboundConfig.maxBrimstoneSize);
    }
}
