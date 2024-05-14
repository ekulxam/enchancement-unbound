package survivalblock.enchancement_unbound.mixin.midastouch;

import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Mixin(value = ServerWorld.class, priority = 2000)
public class ServerWorldMixin {

    @Inject(method = "tickEntity", at = @At("HEAD"), cancellable = true)
    private void midasTouchCancelClientTickEntity(Entity entity, CallbackInfo ci){
        if (entity instanceof LivingEntity living && UnboundEntityComponents.MIDAS_TOUCH.get(living).shouldPreventTicking()) {
            ((ComponentProvider) living).getComponentContainer().tickServerComponents();
            for (Entity passenger : entity.getPassengerList()) {
                ((ComponentProvider) passenger).getComponentContainer().tickServerComponents();
            }
            ci.cancel();
        }
    }
}
