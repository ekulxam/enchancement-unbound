package survivalblock.enchancement_unbound.mixin.warp;

import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(TridentEntity.class)
public interface TridentEntityAccessor {

    @Accessor("LOYALTY")
    static TrackedData<Byte> getLoyalty() {
        throw new UnsupportedOperationException();
    }

    @Accessor
    ItemStack getTridentStack();

    @Accessor("dealtDamage")
    void setDealtDamage(boolean dealtDamage);
}
