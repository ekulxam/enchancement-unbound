package survivalblock.enchancement_unbound.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.BuiltinModelItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import survivalblock.enchancement_unbound.access.RenderHandleSometimesAccess;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.entity.ProjectedShieldEntity;
import survivalblock.enchancement_unbound.common.entity.ShieldboardEntity;
import survivalblock.enchancement_unbound.mixin.shieldsurf.client.ItemRendererAccessor;

public class ProjectedShieldEntityRenderer extends EntityRenderer<ProjectedShieldEntity> {

    public static final Identifier TEXTURE = new Identifier("textures/entity/shield_base_nopattern.png");

    public ProjectedShieldEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
    }

    @Override
    public void render(ProjectedShieldEntity projectedShield, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        ItemStack stack = projectedShield.asItemStack();
        int overlay = OverlayTexture.DEFAULT_UV;
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw + (UnboundConfig.projectedShieldsRenderOutwards ? 0 : 180)));
        matrixStack.translate(0.0f, 0.6f, 0.0f);
        BuiltinModelItemRenderer builtinModelItemRenderer = ((ItemRendererAccessor) MinecraftClient.getInstance().getItemRenderer()).getBuiltinModelItemRenderer();
        builtinModelItemRenderer.render(stack, ModelTransformationMode.NONE, matrixStack, vertexConsumerProvider, light, overlay);
        matrixStack.pop();
        super.render(projectedShield, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }

    @Override
    public Identifier getTexture(ProjectedShieldEntity projectedShield) {
        return TEXTURE;
    }
}
