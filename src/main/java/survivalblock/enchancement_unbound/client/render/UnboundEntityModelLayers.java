package survivalblock.enchancement_unbound.client.render;

import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.minecraft.client.model.Dilation;
import net.minecraft.client.model.TexturedModelData;
import net.minecraft.client.render.entity.model.ArmorEntityModel;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.EntityModelLayer;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

public class UnboundEntityModelLayers {

    public static final EntityModelLayer SENTIENT_PANTS = new EntityModelLayer(EnchancementUnbound.id("sentient_pants"), "main");
    public static final EntityModelLayer SENTIENT_PANTS_INNER_ARMOR = new EntityModelLayer(EnchancementUnbound.id("sentient_pants"), "inner_armor");

    public static void init() {
        EntityModelLayerRegistry.registerModelLayer(SENTIENT_PANTS, UnboundEntityModelLayers::getDefault);
        EntityModelLayerRegistry.registerModelLayer(SENTIENT_PANTS_INNER_ARMOR, UnboundEntityModelLayers::getInnerArmor);
    }

    private static TexturedModelData getDefault() {
        return TexturedModelData.of(BipedEntityModel.getModelData(Dilation.NONE, 0.0f), 64, 64);
    }

    private static TexturedModelData getInnerArmor() {
        return TexturedModelData.of(ArmorEntityModel.getModelData(new Dilation(0.5f)), 64, 32);
    }
}
