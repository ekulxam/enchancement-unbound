package survivalblock.enchancement_unbound.mixin.midastouch;

import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Entity.class)
public interface EntityAccessor {
    
    @Invoker
    void invokeCheckWaterState();
}
