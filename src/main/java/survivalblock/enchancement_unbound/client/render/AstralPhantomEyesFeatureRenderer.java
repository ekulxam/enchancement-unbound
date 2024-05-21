package survivalblock.enchancement_unbound.client.render;

import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.entity.feature.EyesFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PhantomEntityModel;
import net.minecraft.entity.mob.PhantomEntity;
import net.minecraft.util.Identifier;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.entity.AstralPhantomEntity;

public class AstralPhantomEyesFeatureRenderer<T extends AstralPhantomEntity>
        extends EyesFeatureRenderer<T, AstralPhantomEntityModel<T>> {
    private static final RenderLayer SKIN = RenderLayer.getEyes(EnchancementUnbound.id("textures/entity/astral_phantom_eyes.png"));

    public AstralPhantomEyesFeatureRenderer(FeatureRendererContext<T, AstralPhantomEntityModel<T>> featureRendererContext) {
        super(featureRendererContext);
    }

    @Override
    public RenderLayer getEyesTexture() {
        return SKIN;
    }
}