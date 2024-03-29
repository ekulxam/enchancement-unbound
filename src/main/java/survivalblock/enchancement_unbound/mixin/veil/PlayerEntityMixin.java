package survivalblock.enchancement_unbound.mixin.veil;

import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import survivalblock.enchancement_unbound.access.VeilModifyWorldAccess;

@Mixin(PlayerEntity.class)
public class PlayerEntityMixin implements VeilModifyWorldAccess {
    @Unique
    protected boolean enchancement_unbound$modifyWorldChanged = false;
    @Override
    public void enchancement_unbound$setModifyWorldChanged(boolean value) {
        enchancement_unbound$modifyWorldChanged = value;
    }

    @Override
    public boolean enchancement_unbound$getModifyWorldChanged() {
        return enchancement_unbound$modifyWorldChanged;
    }
}
