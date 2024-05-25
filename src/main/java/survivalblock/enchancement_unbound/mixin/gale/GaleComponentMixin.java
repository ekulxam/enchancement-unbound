package survivalblock.enchancement_unbound.mixin.gale;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.component.entity.GaleComponent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(value = GaleComponent.class, remap = false)
public class GaleComponentMixin {

    @WrapWithCondition(method = "use", at = @At(value = "FIELD", target = "Lmoriyashiine/enchancement/common/component/entity/GaleComponent;jumpsLeft:I", opcode = Opcodes.PUTFIELD))
    private boolean whoSaidToDecrement(GaleComponent instance, int value){
        return !UnboundConfig.infiniteGale;
    }

    @WrapWithCondition(method = "use", at = @At(value = "FIELD", target = "Lmoriyashiine/enchancement/common/component/entity/GaleComponent;jumpCooldown:I", opcode = Opcodes.PUTFIELD))
    private boolean noJumpCooldown(GaleComponent instance, int value) {
        return !UnboundUtil.galeAirbending();
    }
}
