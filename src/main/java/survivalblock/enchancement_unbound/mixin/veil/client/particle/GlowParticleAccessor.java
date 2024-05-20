package survivalblock.enchancement_unbound.mixin.veil.client.particle;

import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.world.ClientWorld;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(GlowParticle.class)
public interface GlowParticleAccessor {

    @Invoker("<init>")
    static GlowParticle invokeGlowParticleConstructor(ClientWorld world, double x, double y, double z, double velocityX, double velocityY, double velocityZ, SpriteProvider spriteProvider) {
        throw new UnsupportedOperationException();
    }
}
