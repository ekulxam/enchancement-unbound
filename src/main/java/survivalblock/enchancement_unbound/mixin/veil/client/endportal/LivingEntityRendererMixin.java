package survivalblock.enchancement_unbound.mixin.veil.client.endportal;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(LivingEntityRenderer.class)
public abstract class LivingEntityRendererMixin<T extends LivingEntity, M extends EntityModel<T>>
        extends EntityRenderer<T> {

    // huge thanks to @papierkorb2292 for helping me figure this out
    // and also to @sciencemind11 for giving me the idea
    protected LivingEntityRendererMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    @ModifyExpressionValue(method = "render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/entity/LivingEntityRenderer;getRenderLayer(Lnet/minecraft/entity/LivingEntity;ZZZ)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer changeModelLayerToEnd(@Nullable RenderLayer layer, @Local(argsOnly = true) LivingEntity livingEntity){
        if (!UnboundUtil.shouldRenderWithEndShader(livingEntity) || layer == RenderLayer.getGlint() || layer == RenderLayer.getGlintTranslucent() || layer == RenderLayer.getDirectGlint() || layer == RenderLayer.getEntityGlint() || layer == RenderLayer.getDirectEntityGlint() || layer == RenderLayer.getArmorEntityGlint() || layer == RenderLayer.getArmorGlint()) {
            return layer;
        }
        return UnboundUtil.getEndShader();
    }

    @Unique
    protected VertexConsumerProvider replaceRenderLayerWithEndShader(VertexConsumerProvider vertexConsumerProvider, T entity){
        return (layer) -> {
            Identifier identifier = this.getTexture(entity);
            if (layer == RenderLayer.getGlint() || layer == RenderLayer.getGlintTranslucent() || layer == RenderLayer.getDirectGlint() || layer == RenderLayer.getEntityGlint() || layer == RenderLayer.getDirectEntityGlint() || layer == RenderLayer.getArmorEntityGlint() || layer == RenderLayer.getArmorGlint() || layer == RenderLayer.getEntityTranslucentCull(identifier) || layer == RenderLayer.getItemEntityTranslucentCull(identifier)) {
                return vertexConsumerProvider.getBuffer(layer);
            }
            return vertexConsumerProvider.getBuffer(UnboundUtil.getEndShader());
        };
    }
}
