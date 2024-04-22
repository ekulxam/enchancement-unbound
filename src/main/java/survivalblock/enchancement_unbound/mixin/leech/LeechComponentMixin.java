package survivalblock.enchancement_unbound.mixin.leech;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import moriyashiine.enchancement.common.component.entity.LeechComponent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = LeechComponent.class, priority = 1500)
public class LeechComponentMixin {
    // I didn't think that leech could get any more OP, but I guess I was wrong
    @ModifyExpressionValue(method = "tick", at = @At(value = "CONSTANT", args = "intValue=120"), remap = false)
    private int infiniteLeech(int constant){
        return UnboundConfig.leechForever ? Integer.MAX_VALUE : constant;
    }

    @ModifyExpressionValue(method = "serverTick", at = @At(value = "CONSTANT", args = "intValue=20"), remap = false)
    private int leechTick(int constant){
        return UnboundConfig.leechInterval == 20 ? constant : UnboundConfig.leechInterval;
    }
}
