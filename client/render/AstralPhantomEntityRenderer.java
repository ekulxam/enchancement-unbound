package survivalblock.enchancement_unbound.client.render;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.MobEntityRenderer;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;
import survivalblock.enchancement_unbound.common.entity.AstralPhantomEntity;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

public class AstralPhantomEntityRenderer extends MobEntityRenderer<AstralPhantomEntity, AstralPhantomEntityModel<AstralPhantomEntity>> {
    private static final Identifier TEXTURE = EnchancementUnbound.id("textures/entity/astral_phantom.png");

    public AstralPhantomEntityRenderer(EntityRendererFactory.Context context) {
        super(context, new AstralPhantomEntityModel<>(context.getPart(EntityModelLayers.PHANTOM)), 0.75f);
        this.addFeature(new AstralPhantomEyesFeatureRenderer<>(this));
    }

    @Override
    public boolean shouldRender(AstralPhantomEntity mobEntity, Frustum frustum, double d, double e, double f) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return super.shouldRender(mobEntity, frustum, d, e, f);
        }
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return super.shouldRender(mobEntity, frustum, d, e, f);
        }
        CurtainComponent curtainComponent = UnboundEntityComponents.CURTAIN.get(client.player);
        if (!curtainComponent.isAware()) {
            return false;
        }
        return super.shouldRender(mobEntity, frustum, d, e, f);
    }

    @Override
    public Identifier getTexture(AstralPhantomEntity astralPhantomEntity) {
        return TEXTURE;
    }

    @Override
    protected void scale(AstralPhantomEntity astralPhantomEntity, MatrixStack matrixStack, float f) {
        matrixStack.translate(0.0f, 1.3125f, 0.1875f);
    }

    @Override
    protected void setupTransforms(AstralPhantomEntity astralPhantomEntity, MatrixStack matrixStack, float f, float g, float h) {
        super.setupTransforms(astralPhantomEntity, matrixStack, f, g, h);
        matrixStack.multiply(RotationAxis.NEGATIVE_X.rotationDegrees(astralPhantomEntity.getPitch()));
    }
}
