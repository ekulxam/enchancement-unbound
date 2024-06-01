package survivalblock.enchancement_unbound.mixin.veil.client.particle;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {

    @Accessor("velocityX")
    double enchancement_unbound$getVelocityX();
    @Accessor("velocityY")
    double enchancement_unbound$getVelocityY();
    @Accessor("velocityZ")
    double enchancement_unbound$getVelocityZ();

    @Accessor("velocityX")
    void enchancement_unbound$setVelocityX(double velocityX);
    @Accessor("velocityY")
    void enchancement_unbound$setVelocityY(double velocityY);
    @Accessor("velocityZ")
    void enchancement_unbound$setVelocityZ(double velocityZ);
}
