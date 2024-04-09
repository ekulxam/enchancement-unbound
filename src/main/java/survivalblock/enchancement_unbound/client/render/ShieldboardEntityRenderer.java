package survivalblock.enchancement_unbound.client.render;

import com.mojang.datafixers.util.Pair;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BannerBlockEntity;
import net.minecraft.block.entity.BannerPattern;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BannerBlockEntityRenderer;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.model.EntityModelLayers;
import net.minecraft.client.render.entity.model.ShieldEntityModel;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.ModelLoader;
import net.minecraft.client.util.SpriteIdentifier;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import survivalblock.enchancement_unbound.common.entity.ShieldboardEntity;

import java.util.List;

@Environment(value= EnvType.CLIENT)
public class ShieldboardEntityRenderer extends EntityRenderer<ShieldboardEntity> {
    public static final Identifier TEXTURE = new Identifier("textures/entity/shield_base_nopattern.png");
    private final ShieldEntityModel modelShield;

    public ShieldboardEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.modelShield = new ShieldEntityModel(context.getPart(EntityModelLayers.SHIELD));
    }

    @Override
    public void render(ShieldboardEntity shieldboardEntity, float yaw, float tickDelta, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int light) {
        ItemStack stack = shieldboardEntity.asItemStack();
        boolean bl = BlockItem.getBlockEntityNbt(stack) != null;
        int overlay = OverlayTexture.DEFAULT_UV;
        matrixStack.push();
        matrixStack.scale(1.0f, -1.0f, -1.0f);
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(yaw));
        matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(90.0F));
        matrixStack.translate(0f, 0f, 0.1f);
        SpriteIdentifier spriteIdentifier = bl ? ModelLoader.SHIELD_BASE : ModelLoader.SHIELD_BASE_NO_PATTERN;
        VertexConsumer vertexConsumer = spriteIdentifier.getSprite().getTextureSpecificVertexConsumer(ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.modelShield.getLayer(spriteIdentifier.getAtlasId()), true, stack.hasGlint()));
        // this.modelShield.getHandle().render(matrixStack, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        if (bl) {
            List<Pair<RegistryEntry<BannerPattern>, DyeColor>> list = BannerBlockEntity.getPatternsFromNbt(ShieldItem.getColor(stack), BannerBlockEntity.getPatternListNbt(stack));
            BannerBlockEntityRenderer.renderCanvas(matrixStack, vertexConsumerProvider, light, overlay, this.modelShield.getPlate(), spriteIdentifier, false, list, stack.hasGlint());
        } else {
            this.modelShield.getPlate().render(matrixStack, vertexConsumer, light, overlay, 1.0f, 1.0f, 1.0f, 1.0f);
        }
        matrixStack.pop();
        super.render(shieldboardEntity, yaw, tickDelta, matrixStack, vertexConsumerProvider, light);
    }

    @Override
    public Identifier getTexture(ShieldboardEntity shieldboardEntity) {
        return TEXTURE;
    }
}
