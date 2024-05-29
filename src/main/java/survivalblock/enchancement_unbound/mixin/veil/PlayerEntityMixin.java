package survivalblock.enchancement_unbound.mixin.veil;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.init.UnboundTags;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@SuppressWarnings("UnreachableCode")
@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {


    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "isInvulnerableTo", at = @At("HEAD"), cancellable = true)
    private void talonDust(DamageSource source, CallbackInfoReturnable<Boolean> cir){
        if (UnboundUtil.shouldPreventAction(((PlayerEntity) (Object) this)) && !source.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY)) {
            if (source.getAttacker() != null && source.getAttacker().getType().isIn(UnboundTags.EntityTypes.SHOULD_HIT_IN_VEIL)) {
                return;
            }
            if (source.getSource() != null && source.getSource().getType().isIn(UnboundTags.EntityTypes.SHOULD_HIT_IN_VEIL)) {
                return;
            }
            cir.setReturnValue(true);
        }
    }
}
