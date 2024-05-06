package survivalblock.enchancement_unbound.mixin.executioner;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundTags;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "damage", at = @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;despawnCounter:I", ordinal = 0, opcode = Opcodes.PUTFIELD), argsOnly = true, index = 2)
    private float fivePercentChance(float value, DamageSource source){
        if (!(source.getSource() instanceof LivingEntity living) || !EnchancementUtil.hasEnchantment(UnboundEnchantments.EXECUTIONER, living)) {
            return value;
        }
        Random random = this.getWorld().getRandom();
        if (random.nextFloat() > 0.95f) {
            World world = this.getWorld();
            if (world instanceof ServerWorld serverWorld) {
                serverWorld.spawnParticles(ParticleTypes.EXPLOSION, this.getPos().x, this.getEyeY(), this.getPos().z, 4, 0.5, 0.5, 0.5, 1);
                if (source.getSource() instanceof PlayerEntity) {
                    serverWorld.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1.0f, 1.0f);
                    serverWorld.playSound(null, this.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
                }
            }
            //noinspection ConstantValue
            if (this.getType().isIn(UnboundTags.CANNOT_EXECUTE) || (LivingEntity) (Object) this instanceof PlayerEntity) return value * 10;
            return Float.MAX_VALUE;
        }
        return value;
    }
}
