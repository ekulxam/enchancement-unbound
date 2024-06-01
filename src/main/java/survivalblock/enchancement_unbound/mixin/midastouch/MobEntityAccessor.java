package survivalblock.enchancement_unbound.mixin.midastouch;

import net.minecraft.entity.mob.MobEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(MobEntity.class)
public interface MobEntityAccessor {

    @Accessor("persistent")
    void enchancement_unbound$setPersistent(boolean newValue);
}
