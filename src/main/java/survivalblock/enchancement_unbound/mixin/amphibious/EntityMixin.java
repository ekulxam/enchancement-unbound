package survivalblock.enchancement_unbound.mixin.amphibious;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import dev.emi.stepheightentityattribute.StepHeightEntityAttributeMain;
import moriyashiine.enchancement.common.component.entity.ExtendedWaterComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.access.AirSwimmingAccess;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@SuppressWarnings({"ConstantValue", "UnreachableCode"})
@Mixin(value = Entity.class)
public abstract class EntityMixin implements AirSwimmingAccess {
    @Unique
    protected boolean isAirSwimming;
    @Shadow public abstract boolean hasVehicle();
    @Shadow public abstract boolean isSprinting();
    @Shadow public abstract void setSwimming(boolean swimming);

    @Shadow public abstract World getWorld();

    @WrapOperation(method = "updateSwimming", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;setSwimming(Z)V"))
    private void airSwimming(Entity instance, boolean swimming, Operation<Void> original){
        if(UnboundConfig.amphibiousAirSwimming && ((Entity) (Object) this) instanceof LivingEntity living &&
                EnchantmentHelper.getEquipmentLevel(ModEnchantments.AMPHIBIOUS, living) > 0 &&
                this.isSprinting() && !this.hasVehicle()) {
            boolean hasBuoy = EnchantmentHelper.getEquipmentLevel(ModEnchantments.BUOY, living) > 0;
            if (hasBuoy) {
                UnboundUtil.applyExtendedWater(living);
            } else {
                this.setSwimming(true);
            }
        } else {
            original.call(instance, swimming);
        }
    }

    @Inject(method = "updateSwimming", at = @At("HEAD"))
    private void tickInfiniteAmphibious(CallbackInfo ci){
        if (this.getWorld().isClient()) {
            return;
        }
        if (!UnboundConfig.amphibiousExtendedWaterForever) {
            return;
        }
        if (!(((Entity) (Object) this) instanceof LivingEntity living)) {
            return;
        }
        if (EnchantmentHelper.getEquipmentLevel(ModEnchantments.AMPHIBIOUS, living) <= 0) {
            return;
        }
        UnboundUtil.applyExtendedWater(living);
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
