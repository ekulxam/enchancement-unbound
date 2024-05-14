package survivalblock.enchancement_unbound.mixin.curtain.client;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRendererMixin<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    protected PlayerEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @ModifyExpressionValue(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntitySolid(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer replaceSolidWithEndTexture(RenderLayer original, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve){
        return UnboundUtil.shouldRenderWithEndShader(player) ? UnboundUtil.getEndShader() : original;
    }

    @ModifyExpressionValue(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntityTranslucent(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer replaceTranslucentWithEndTexture(RenderLayer original, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve){
        return UnboundUtil.shouldRenderWithEndShader(player) ? UnboundUtil.getEndShader() : original;
    }

    @ModifyVariable(method = "renderRightArm", at = @At("HEAD"), argsOnly = true)
    private VertexConsumerProvider renderRightArmWithShader(VertexConsumerProvider original, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player){
        return UnboundUtil.shouldRenderWithEndShader(player) ? replaceRenderLayerWithEndShader(original, player) : original;
    }

    @ModifyVariable(method = "renderLeftArm", at = @At("HEAD"), argsOnly = true)
    private VertexConsumerProvider renderLeftArmWithShader(VertexConsumerProvider original, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player){
        return UnboundUtil.shouldRenderWithEndShader(player) ? replaceRenderLayerWithEndShader(original, player) : original;
    }
}
