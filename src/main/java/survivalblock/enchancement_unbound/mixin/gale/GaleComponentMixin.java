package survivalblock.enchancement_unbound.mixin.gale;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.component.entity.GaleComponent;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = GaleComponent.class, remap = false)
public class GaleComponentMixin {

    @WrapWithCondition(method = "handle", at = @At(value = "FIELD", target = "Lmoriyashiine/enchancement/common/component/entity/GaleComponent;jumpsLeft:I", opcode = Opcodes.PUTFIELD))
    private static boolean whoSaidToDecrement(GaleComponent instance, int value){
        return !UnboundConfig.infiniteGale;
    }
}
