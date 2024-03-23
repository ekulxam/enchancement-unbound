package survivalblock.enchancement_unbound.mixin.brimstone;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.client.render.entity.BrimstoneEntityRenderer;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.Slice;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = BrimstoneEntityRenderer.class, priority = 1500)
public class BrimstoneEntityRendererMixin {

    @ModifyVariable(method = "render(Lmoriyashiine/enchancement/common/entity/projectile/BrimstoneEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            slice = @Slice(from = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lmoriyashiine/enchancement/client/render/entity/BrimstoneEntityRenderer;drawPlane(Lorg/joml/Matrix4f;Lorg/joml/Matrix3f;Lnet/minecraft/client/render/VertexConsumer;FF)V")), at = @At(shift = At.Shift.AFTER, value = "INVOKE", target = "Lnet/minecraft/client/util/math/MatrixStack;multiply(Lorg/joml/Quaternionf;)V"),
            name = "i")
    private int addMoreBrimstoneVisuals(int i) {
        int whatYouShouldReallyStepBy = switch (UnboundConfig.brimstoneVisuals) {
            case VERY_VERY_LOW -> 45;
            case VERY_LOW -> 30;
            case LOW -> 20;
            case DEFAULT -> 15;
            case HIGH -> 10;
            case VERY_HIGH -> 8;
            case VERY_VERY_HIGH -> 5;
        };
        return i + (whatYouShouldReallyStepBy - 15);
    }


    @ModifyExpressionValue(method = "render(Lmoriyashiine/enchancement/common/entity/projectile/BrimstoneEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V",
            at = @At(value = "INVOKE", target = "Lmoriyashiine/enchancement/common/entity/projectile/BrimstoneEntity;getDamage()D"))
    private double enforceSizeLimit(double damage) {
        return Math.min(damage, UnboundConfig.maxBrimstoneSize);
    }
}
