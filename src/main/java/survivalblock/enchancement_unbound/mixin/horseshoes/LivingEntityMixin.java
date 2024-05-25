package survivalblock.enchancement_unbound.mixin.horseshoes;

import moriyashiine.enchancement.common.component.entity.BouncyComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.fluid.Fluids;
import net.minecraft.sound.SoundEvents;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "canWalkOnFluid", at = @At("HEAD"), cancellable = true)
    private void buoyHorses(FluidState state, CallbackInfoReturnable<Boolean> cir){
        if (!UnboundConfig.horseshoes) {
            return;
        }
        if (state.getFluid().equals(Fluids.EMPTY)) {
            return;
        }
        if (!((LivingEntity) (Object) this instanceof HorseEntity horseEntity)) {
            return;
        }
        if (EnchancementUtil.hasEnchantment(ModEnchantments.BUOY, horseEntity)) {
            cir.setReturnValue(true);
        }
    }
}
