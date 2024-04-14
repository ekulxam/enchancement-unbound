package survivalblock.enchancement_unbound.mixin.shieldsurf.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.client.input.Input;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.Constant;
import survivalblock.enchancement_unbound.common.entity.ShieldboardEntity;

@Debug(export = true)
@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {

    @Shadow private boolean riding;

    @Shadow public Input input;

    @WrapOperation(method = "tickRiding", constant = @Constant(classValue = BoatEntity.class, ordinal = 0))
    private boolean updateRidingShieldboard(Object obj, Operation<Boolean> original){
        if (obj instanceof ShieldboardEntity shieldboard) {
            shieldboard.setInputs(this.input.pressingForward);
        }
        return original.call(obj);
    }
}
