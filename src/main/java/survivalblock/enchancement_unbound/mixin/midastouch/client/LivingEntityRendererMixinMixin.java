package survivalblock.enchancement_unbound.mixin.midastouch.client;

import com.bawnorton.mixinsquared.TargetHandler;
import moriyashiine.enchancement.client.reloadlisteners.FrozenReloadListener;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.model.EntityModel;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.LivingEntity;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.component.MidasTouchComponent;
import survivalblock.enchancement_unbound.common.enchantment.MidasTouchCurse;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

import java.util.List;


@Debug(export = true)
@Mixin(value = LivingEntityRenderer.class, priority = 1500)
public abstract class LivingEntityRendererMixinMixin<T extends LivingEntity, M extends EntityModel<T>> extends EntityRenderer<T> {

    @Shadow protected M model;

    @Shadow protected abstract float getHandSwingProgress(T entity, float tickDelta);

    @Shadow protected abstract float getAnimationProgress(T entity, float tickDelta);

    @Shadow protected abstract void setupTransforms(T entity, MatrixStack matrices, float animationProgress, float bodyYaw, float tickDelta);

    @Shadow protected abstract void scale(T entity, MatrixStack matrices, float amount);

    @Shadow protected abstract boolean isVisible(T entity);

    @Shadow protected abstract @Nullable RenderLayer getRenderLayer(T entity, boolean showBody, boolean translucent, boolean showOutline);

    @Shadow @Final protected List<FeatureRenderer<T, M>> features;

    @Shadow protected abstract float getAnimationCounter(T entity, float tickDelta);

    protected LivingEntityRendererMixinMixin(EntityRendererFactory.Context ctx) {
        super(ctx);
    }

    /**
     * @see moriyashiine.enchancement.mixin.frostbite.client.LivingEntityRendererMixin
     */
    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.frostbite.client.LivingEntityRendererMixin",
            name = "Lmoriyashiine/enchancement/mixin/frostbite/client/LivingEntityRendererMixin;enchancement$frostbite(Lnet/minecraft/util/Identifier;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/util/Identifier;"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(value = "HEAD"), cancellable = true
    )
    private void midasTouchRender(Identifier value, LivingEntity living, CallbackInfoReturnable<Identifier> cir) {
        if (ModEntityComponents.FROZEN.get(living).isFrozen()) {
            MidasTouchCurse.golden = false;
            return;
        }
        if (UnboundEntityComponents.MIDAS_TOUCH.get(living).isGolden()) {
            if (value != null && value.getPath().contains("generated/golden_")) {
                return;
            }
            MidasTouchCurse.golden = true;
            Identifier texture = FrozenReloadListener.INSTANCE.getTexture(value);
            MidasTouchCurse.golden = false;
            cir.setReturnValue(texture);
        }
    }

    @Inject(
            method = {"render(Lnet/minecraft/entity/LivingEntity;FFLnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/client/render/VertexConsumerProvider;I)V"},
            at = {@At("HEAD")},
            cancellable = true
    )
    private void renderStatueLimbsAndAngles(T livingEntity, float yaw, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, CallbackInfo ci) {
        MidasTouchComponent midasTouchComponent = UnboundEntityComponents.MIDAS_TOUCH.get(livingEntity);
        if (midasTouchComponent.isGolden()) {
            MinecraftClient client = MinecraftClient.getInstance();
            matrices.push();
            livingEntity.setPose(midasTouchComponent.getForcedPose());
            model.handSwingProgress = getHandSwingProgress(livingEntity, tickDelta);
            model.riding = livingEntity.hasVehicle();
            model.child = livingEntity.isBaby();
            float bodyYaw = midasTouchComponent.getForcedBodyYaw();
            float pitch = midasTouchComponent.getForcedPitch();
            float headYawMinusBodyYaw = midasTouchComponent.getForcedHeadYaw() - bodyYaw;
            float animationProgress = getAnimationProgress(livingEntity, tickDelta);
            if (LivingEntityRenderer.shouldFlipUpsideDown(livingEntity)) {
                pitch *= -1;
                headYawMinusBodyYaw *= -1;
            }
            float limbAngle = 0.0f;
            float limbDistance = 0.0f;
            setupTransforms(livingEntity, matrices, animationProgress, bodyYaw, tickDelta);
            matrices.scale(-1, -1, 1);
            scale(livingEntity, matrices, tickDelta);
            matrices.translate(0, -1.501F, 0);
            model.animateModel(livingEntity, limbAngle, limbDistance, tickDelta);
            model.setAngles(livingEntity, limbAngle, limbDistance, 0, headYawMinusBodyYaw, pitch);
            boolean visible = isVisible(livingEntity);
            boolean translucent = !visible && !livingEntity.isInvisibleTo(client.player);
            RenderLayer renderLayer = getRenderLayer(livingEntity, visible, translucent, client.hasOutline(livingEntity));
            if (renderLayer != null) {
                model.render(matrices, vertexConsumers.getBuffer(renderLayer), light, LivingEntityRenderer.getOverlay(livingEntity, getAnimationCounter(livingEntity, tickDelta)), 1, 1, 1, translucent ? 0.15F : 1);
            }
            if (!livingEntity.isSpectator()) {
                for (FeatureRenderer<T, M> featureRenderer : features) {
                    featureRenderer.render(matrices, vertexConsumers, light, livingEntity, limbAngle, limbDistance, tickDelta, animationProgress, headYawMinusBodyYaw, pitch);
                }
            }
            matrices.pop();
            super.render(livingEntity, yaw, tickDelta, matrices, vertexConsumers, light);
            ci.cancel();
        }
    }
}
