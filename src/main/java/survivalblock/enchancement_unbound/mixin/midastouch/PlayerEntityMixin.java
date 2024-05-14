package survivalblock.enchancement_unbound.mixin.midastouch;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.component.MidasTouchComponent;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Mixin(value = PlayerEntity.class, priority = 2000)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "attack", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", ordinal = 0, shift = At.Shift.BEFORE))
    private void goldenBoy(Entity target, CallbackInfo ci){
        if (!UnboundConfig.unboundEnchantments) {
            return;
        }
        if (this.getWorld().isClient()) return;
        if (!(target instanceof LivingEntity living)) {
            return;
        }
        if (!EnchancementUtil.hasEnchantment(UnboundEnchantments.MIDAS_TOUCH, this)) {
            return;
        }
        UnboundEntityComponents.MIDAS_TOUCH.get(this).accumulateKarma();
        MidasTouchComponent midasTouchComponent = UnboundEntityComponents.MIDAS_TOUCH.get(living);
        if (midasTouchComponent.shouldUndo() ||midasTouchComponent.isGolden()) {
            return;
        }
        midasTouchComponent.setGolden(true);
        midasTouchComponent.setOneWhoWronged((PlayerEntity) (Object) this);
    }
}
