package survivalblock.enchancement_unbound.mixin.horseshoes.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.feature.HorseArmorFeatureRenderer;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.HorseEntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(HorseArmorFeatureRenderer.class)
@SuppressWarnings("JavadocReference")
public abstract class HorseArmorFeatureRendererMixin extends FeatureRenderer<HorseEntity, HorseEntityModel<HorseEntity>> {

    @Shadow @Final private HorseEntityModel<HorseEntity> model;

    public HorseArmorFeatureRendererMixin(FeatureRendererContext<HorseEntity, HorseEntityModel<HorseEntity>> context) {
        super(context);
    }

    /**
     * @see net.minecraft.client.render.entity.feature.ArmorFeatureRenderer#renderArmorParts(MatrixStack, VertexConsumerProvider, int, ArmorItem, BipedEntityModel, boolean, float, float, float, String)
     */
    @WrapOperation(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/render/RenderLayer;getEntityCutoutNoCull(Lnet/minecraft/util/Identifier;)Lnet/minecraft/client/render/RenderLayer;"))
    private RenderLayer renderOriginalProperly(Identifier texture, Operation<RenderLayer> original, @Local ItemStack stack){
        return UnboundConfig.horseshoes && stack.hasGlint() ? RenderLayer.getArmorCutoutNoCull(texture) : original.call(texture);
    }

    /**
     * @see net.minecraft.client.render.entity.feature.ArmorFeatureRenderer#renderGlint(MatrixStack, VertexConsumerProvider, int, BipedEntityModel)
     */
    @Inject(method = "render(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;ILnet/minecraft/entity/passive/HorseEntity;FFFFFF)V", at = @At("RETURN"))
    private void renderHorseArmorGlint(MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light, HorseEntity horseEntity, float f, float g, float h, float j, float k, float l, CallbackInfo ci, @Local ItemStack stack){
        if (UnboundConfig.horseshoes && stack.hasGlint()) {
            this.model.render(matrixStack, vertexConsumerProvider.getBuffer(RenderLayer.getArmorEntityGlint()), light, OverlayTexture.DEFAULT_UV, 1.0f, 1.0f, 1.0f, 1.0f);
        }
    }
}
