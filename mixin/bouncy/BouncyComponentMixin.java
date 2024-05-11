package survivalblock.enchancement_unbound.mixin.bouncy;

import moriyashiine.enchancement.common.component.entity.BouncyComponent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(BouncyComponent.class)
public class BouncyComponentMixin {

    @Shadow(remap = false) public int bounceStrength;

    @Inject(method = "tick", at = @At(value = "FIELD", target = "Lmoriyashiine/enchancement/common/component/entity/BouncyComponent;bounceStrength:I", ordinal = 0, opcode = Opcodes.PUTFIELD, shift = At.Shift.AFTER, remap = false), remap = false)
    private void instaChargeBouncy(CallbackInfo ci) {
        // just ignore the @At
        this.bounceStrength += switch (UnboundConfig.bouncyChargeSpeed) {
            case DEFAULT -> 0;
            case FAST -> 1;
            case VERY_FAST -> 2;
            case VERY_VERY_FAST -> 4;
            case INSTANT -> 29;
        };
    }
}
