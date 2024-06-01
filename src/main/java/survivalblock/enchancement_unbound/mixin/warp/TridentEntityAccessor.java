package survivalblock.enchancement_unbound.mixin.warp;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.TridentEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TridentEntity.class)
public interface TridentEntityAccessor {

    @Accessor("LOYALTY")
    static TrackedData<Byte> enchancement_unbound$getLoyalty() {
        throw new UnsupportedOperationException();
    }

    @Accessor("dealtDamage")
    void enchancement_unbound$setDealtDamage(boolean dealtDamage);
}
