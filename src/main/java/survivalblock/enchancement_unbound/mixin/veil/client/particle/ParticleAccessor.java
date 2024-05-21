package survivalblock.enchancement_unbound.mixin.veil.client.particle;

import net.minecraft.client.particle.Particle;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Particle.class)
public interface ParticleAccessor {

    @Accessor
    double getVelocityX();
    @Accessor
    double getVelocityY();
    @Accessor
    double getVelocityZ();

    @Accessor
    void setVelocityX(double velocityX);
    @Accessor
    void setVelocityY(double velocityY);
    @Accessor
    void setVelocityZ(double velocityZ);
}
