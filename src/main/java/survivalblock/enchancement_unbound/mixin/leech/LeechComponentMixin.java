package survivalblock.enchancement_unbound.mixin.leech;

import moriyashiine.enchancement.common.component.entity.LeechComponent;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(LeechComponent.class)
public class LeechComponentMixin {
    // I didn't think that leech could get any more OP, but I guess I was wrong
    @ModifyConstant(method = "tick", constant = @Constant(intValue = 120), remap = false)
    private int infiniteLeech(int constant){
        return UnboundConfig.leechForever ? Integer.MAX_VALUE : constant;
    }

    @ModifyConstant(method = "serverTick", constant = @Constant(intValue = 20), remap = false)
    private int leechTick(int constant){
        return UnboundConfig.leechInterval == 20 ? constant : UnboundConfig.leechInterval;
    }
}
