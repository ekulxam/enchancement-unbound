package survivalblock.enchancement_unbound.mixin.delay;

import moriyashiine.enchancement.common.component.entity.DelayComponent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = DelayComponent.class, priority = 1500)
public class DelayComponentMixin {

    @Unique
    private static boolean beforeIf = false;

    @Shadow(remap = false) private int ticksFloating;

    @Inject(method = "serverTick", at = @At(value = "FIELD", target = "Lmoriyashiine/enchancement/common/component/entity/DelayComponent;ticksFloating:I", ordinal = 0, opcode = Opcodes.GETFIELD, shift = At.Shift.BEFORE), remap = false)
    private void instaChargeDelay(CallbackInfo ci){
        if (UnboundConfig.instantChargeDelay && this.ticksFloating < 199) {
            this.ticksFloating = 199;
        }
    }

    @ModifyVariable(method = "serverTick", at = @At(value = "LOAD", ordinal = 0), remap = false)
    private boolean shootIfInstaCharged(boolean original){
        beforeIf = original;
        return UnboundConfig.instantChargeDelay || original;
    }

    @ModifyVariable(method = "serverTick", at = @At(value = "LOAD", ordinal = 1), remap = false)
    private boolean undoVariableChange(boolean original) {
        return beforeIf;
    }
}
