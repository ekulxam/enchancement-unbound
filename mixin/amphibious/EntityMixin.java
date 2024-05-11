package survivalblock.enchancement_unbound.mixin.amphibious;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.access.AirSwimmingAccess;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = Entity.class)
public abstract class EntityMixin implements AirSwimmingAccess {
    @Shadow
    protected boolean touchingWater;
    @Unique
    protected boolean isAirSwimming;
    @Shadow public abstract boolean hasVehicle();
    @Shadow public abstract boolean isSprinting();
    @Shadow public abstract void setSwimming(boolean swimming);

    @WrapOperation(method = "updateSwimming", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setSwimming(Z)V"))
    private void airSwimming(Entity instance, boolean swimming, Operation<Void> original){
        if(UnboundConfig.amphibiousAirSwimming && ((Entity) (Object) this) instanceof LivingEntity living &&
                EnchantmentHelper.getEquipmentLevel(ModEnchantments.AMPHIBIOUS, living) > 0 &&
                this.isSprinting() && !this.hasVehicle()){
            if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.BUOY, living) <= 0) {
                this.touchingWater = true;
            }
            this.setSwimming(true);
        } else {
            original.call(instance, swimming);
        }
    }

    @Override
    public void enchancement_unbound$setAirSwimming(boolean value) {
        this.isAirSwimming = value;
    }

    @Override
    public boolean enchancement_unbound$isAirSwimming() {
        return this.isAirSwimming;
    }
}
