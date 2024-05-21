package survivalblock.enchancement_unbound.mixin.veil.client.endportal;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.client.util.UnboundClientUtil;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin extends LivingEntityRendererMixin<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {
    protected PlayerEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @Inject(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At("HEAD"), cancellable = true)
    private void astralPlane(LivingEntity livingEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        if (EnchancementUtil.hasEnchantment(ModEnchantments.PERCEPTION, player)) {
            return;
        }
        if (player.equals(livingEntity)) {
            return;
        }
        if (!(livingEntity instanceof PlayerEntity oneBeingRendered)) {
            return;
        }
        if (UnboundEntityComponents.CURTAIN.get(oneBeingRendered).isInCurtain()) {
            ci.cancel();
        }
    }

    @ModifyExpressionValue(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntitySolid(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer replaceSolidWithEndTexture(RenderLayer original, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve){
        return UnboundClientUtil.shouldRenderWithEndShader(player) ? UnboundClientUtil.getEndShader() : original;
    }

    @ModifyExpressionValue(method = "renderArm", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntityTranslucent(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer replaceTranslucentWithEndTexture(RenderLayer original, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, ModelPart arm, ModelPart sleeve){
        return UnboundClientUtil.shouldRenderWithEndShader(player) ? UnboundClientUtil.getEndShader() : original;
    }

    @ModifyVariable(method = "renderRightArm", at = @At("HEAD"), argsOnly = true)
    private VertexConsumerProvider renderRightArmWithShader(VertexConsumerProvider original, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player){
        return UnboundClientUtil.shouldRenderWithEndShader(player) ? replaceRenderLayerWithEndShader(original, player) : original;
    }

    @ModifyVariable(method = "renderLeftArm", at = @At("HEAD"), argsOnly = true)
    private VertexConsumerProvider renderLeftArmWithShader(VertexConsumerProvider original, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player){
        return UnboundClientUtil.shouldRenderWithEndShader(player) ? replaceRenderLayerWithEndShader(original, player) : original;
    }
}
