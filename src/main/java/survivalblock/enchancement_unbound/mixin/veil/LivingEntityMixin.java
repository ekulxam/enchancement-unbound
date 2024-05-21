package survivalblock.enchancement_unbound.mixin.veil;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.init.UnboundTags;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Inject(method = "canSee", at = @At("HEAD"), cancellable = true)
    private void cantSeeInAstralPlane(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        if (!(entity instanceof PlayerEntity player)) {
            return;
        }
        if (UnboundEntityComponents.CURTAIN.get(player).isInCurtain() && !this.getType().isIn(UnboundTags.EntityTypes.SHOULD_HIT_IN_VEIL)) {
            cir.setReturnValue(false);
        }
    }

    @Inject(method = "canTarget(Lnet/minecraft/entity/LivingEntity;)Z", at = @At("HEAD"), cancellable = true)
    private void cantTargetEither(LivingEntity target, CallbackInfoReturnable<Boolean> cir) {
        if (!(target instanceof PlayerEntity player)) {
            return;
        }
        if (UnboundEntityComponents.CURTAIN.get(player).isInCurtain() && !this.getType().isIn(UnboundTags.EntityTypes.SHOULD_HIT_IN_VEIL)) {
            cir.setReturnValue(false);
        }
    }
}
