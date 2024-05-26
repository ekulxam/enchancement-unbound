package survivalblock.enchancement_unbound.client.render;

import com.google.common.collect.ImmutableList;
import net.minecraft.client.model.*;
import net.minecraft.client.render.entity.model.*;
import net.minecraft.entity.LivingEntity;

public class SentientPantsEntityModel<T extends LivingEntity>
        extends BipedEntityModel<T> {

    public SentientPantsEntityModel(ModelPart root) {
        super(root);
    }

    @Override
    protected Iterable<ModelPart> getHeadParts() {
        return ImmutableList.of();
    }

    @Override
    protected Iterable<ModelPart> getBodyParts() {
        return ImmutableList.of(this.body, this.leftLeg, this.rightLeg);
    }
}