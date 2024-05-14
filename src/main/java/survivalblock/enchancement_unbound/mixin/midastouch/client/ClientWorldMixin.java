package survivalblock.enchancement_unbound.mixin.midastouch.client;

import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Mixin(value = ClientWorld.class, priority = 2000)
public class ClientWorldMixin {

    @Inject(method = "tickEntity", at = @At("HEAD"), cancellable = true)
    private void midasTouchCancelClientTickEntity(Entity entity, CallbackInfo ci){
        if (entity instanceof LivingEntity living && UnboundEntityComponents.MIDAS_TOUCH.get(living).shouldPreventTicking()) {
            ((ComponentProvider) entity).getComponentContainer().tickClientComponents();
            for (Entity passenger : entity.getPassengerList()) {
                ((ComponentProvider) passenger).getComponentContainer().tickClientComponents();
            }
            ci.cancel();
        }
    }
}
