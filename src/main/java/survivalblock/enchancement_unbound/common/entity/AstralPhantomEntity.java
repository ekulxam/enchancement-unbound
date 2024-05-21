package survivalblock.enchancement_unbound.common.entity;

import java.util.List;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.control.BodyControl;
import net.minecraft.entity.ai.control.LookControl;
import net.minecraft.entity.ai.control.MoveControl;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.mob.FlyingEntity;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.mob.Monster;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import net.minecraft.world.event.GameEvent;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.init.UnboundEntityTypes;
import survivalblock.enchancement_unbound.common.init.UnboundSoundEvents;

public class AstralPhantomEntity
        extends FlyingEntity
        implements Monster {
    public static final int WING_FLAP_TICKS = MathHelper.ceil(24.166098f);
    private final TargetPredicate PLAYERS_IN_RANGE_PREDICATE = TargetPredicate.createAttackable().setBaseMaxDistance(64.0);
    protected final float MAX_LOOK_DIRECTION_CHANGE = 5f;

    public AstralPhantomEntity(EntityType<? extends AstralPhantomEntity> entityType, World world) {
        super(entityType, world);
        this.experiencePoints = 5;
        this.moveControl = new AstralPhantomMoveControl(this);
        this.lookControl = new AstralPhantomLookControl(this);
    }

    public static AstralPhantomEntity of(ServerWorld world, PlayerEntity player) {
        if (player.isCreative() || player.isSpectator()) {
            return null;
        }
        AstralPhantomEntity astralPhantom = UnboundEntityTypes.ASTRAL_PHANTOM.create(world);
        if (astralPhantom == null) {
            return null;
        }
        astralPhantom.setPosition(player.getPos());
        astralPhantom.setTarget(player);
        astralPhantom.teleport(4, 20);
        astralPhantom.lookAtEntity(player, 360, 360);
        return astralPhantom;
    }

    public static DefaultAttributeContainer.Builder createAstralPhantomAttributes() {
        return HostileEntity.createHostileAttributes().add(EntityAttributes.GENERIC_ATTACK_DAMAGE, 3.0);
    }

    @Override
    public boolean tryAttack(Entity target) {
        if (target.getWorld().isClient()) {
            return super.tryAttack(target);
        }
        if (target instanceof PlayerEntity player) {
             CurtainComponent curtainComponent = UnboundEntityComponents.CURTAIN.get(player);
             curtainComponent.setInCurtain(true, true);
             curtainComponent.sync();
        }
        return super.tryAttack(target);
    }

    @Override
    public boolean isFlappingWings() {
        return (this.getWingFlapTickOffset() + this.age) % WING_FLAP_TICKS == 0;
    }

    @Override
    protected BodyControl createBodyControl() {
        return new PhantomBodyControl(this);
    }

    @Override
    protected float getActiveEyeHeight(EntityPose pose, EntityDimensions dimensions) {
        return dimensions.height * 0.35f;
    }

    public int getWingFlapTickOffset() {
        return this.getId() * 3;
    }

    @SuppressWarnings("RedundantMethodOverride")
    @Override
    protected boolean isDisallowedInPeaceful() {
        return false;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (source.isOf(DamageTypes.IN_WALL)) {
            return false;
        }
        if (source.isOf(DamageTypes.MAGIC) || source.isOf(DamageTypes.INDIRECT_MAGIC)) {
            amount *= 0.7f;
        }
        if (source.isIn(DamageTypeTags.IS_PROJECTILE)) {
            amount *= 1.25f;
        }
        boolean bl = super.damage(source, amount);
        if (this.isAlive()) this.teleport();
        return bl;
    }

    @Override
    public void tick() {
        this.noClip = true;
        super.tick();
        World world = this.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            this.findTarget();
            if (this.getTarget() instanceof PlayerEntity player && !player.isCreative() && !player.isSpectator()) {
                if (this.getBoundingBox().expand(0.2f).intersects(player.getBoundingBox())) {
                    if (this.tryAttack(player)) {
                        if (MathHelper.nextInt(this.random, 0, 999) == 0) {
                            AstralPhantomEntity astralPhantomEntity = AstralPhantomEntity.of(serverWorld, player);
                            if (astralPhantomEntity != null) serverWorld.spawnEntity(astralPhantomEntity);
                        }
                        if (!this.isSilent()) {
                            this.playSound(UnboundSoundEvents.ENTITY_ASTRAL_PHANTOM_BITE);
                        }
                        this.teleport();
                    }
                } else {
                    if (this.age % 80 == 0 || this.getPos().squaredDistanceTo(player.getPos()) > 58) {
                        if (this.getRandom().nextBoolean() && this.getRandom().nextInt() == 1) {
                            this.setPosition(player.getPos());
                            this.teleport(3, 12);
                        }
                    }
                }
            } else {
                this.discard();
            }
        } else {
            float f = MathHelper.cos((float)(this.getWingFlapTickOffset() + this.age) * 7.448451f * ((float)Math.PI / 180) + (float)Math.PI);
            float g = MathHelper.cos((float)(this.getWingFlapTickOffset() + this.age + 1) * 7.448451f * ((float)Math.PI / 180) + (float)Math.PI);
            if (f > 0.0f && g <= 0.0f) {
                this.getWorld().playSound(this.getX(), this.getY(), this.getZ(), UnboundSoundEvents.ENTITY_ASTRAL_PHANTOM_FLAP, this.getSoundCategory(), 0.95f + this.random.nextFloat() * 0.05f, 0.95f + this.random.nextFloat() * 0.05f, false);
            }
        }
        this.noClip = false;
    }

    @Override
    public boolean shouldRender(double distance) {
        return true;
    }

    @Override
    public SoundCategory getSoundCategory() {
        return SoundCategory.HOSTILE;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return UnboundSoundEvents.ENTITY_ASTRAL_PHANTOM_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource source) {
        return UnboundSoundEvents.ENTITY_ASTRAL_PHANTOM_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return UnboundSoundEvents.ENTITY_ASTRAL_PHANTOM_DEATH;
    }

    @Override
    public boolean canTarget(EntityType<?> type) {
        return true;
    }

    @Override
    public double getMountedHeightOffset() {
        return this.getStandingEyeHeight();
    }

    class AstralPhantomMoveControl
            extends MoveControl {

        public AstralPhantomMoveControl(AstralPhantomEntity owner) {
            super(owner);
        }

        @Override
        public void tick() {
            LivingEntity target = AstralPhantomEntity.this.getTarget();
            if (target == null) {
                return;
            }
            Vec3d targetPos = target.getPos();
            Vec3d pos = AstralPhantomEntity.this.getPos();
            AstralPhantomEntity.this.setVelocity(targetPos.add(0, target.getHeight() / 2d, 0).subtract(pos).normalize().multiply(0.15));
            AstralPhantomEntity.this.lookAtEntity(target, MAX_LOOK_DIRECTION_CHANGE, MAX_LOOK_DIRECTION_CHANGE);
        }
    }

    static class AstralPhantomLookControl
            extends LookControl {
        public AstralPhantomLookControl(MobEntity entity) {
            super(entity);
        }

        @Override
        public void tick() {

        }
    }

    class PhantomBodyControl
            extends BodyControl {
        public PhantomBodyControl(MobEntity entity) {
            super(entity);
        }

        @Override
        public void tick() {
            AstralPhantomEntity.this.headYaw = AstralPhantomEntity.this.bodyYaw;
            AstralPhantomEntity.this.bodyYaw = AstralPhantomEntity.this.getYaw();
        }
    }

    private void findTarget() {
        LivingEntity target = this.getTarget();
        if (target instanceof PlayerEntity player) {
            if (player.isAlive() && !player.isRemoved()) {
                return;
            }
        }
        List<PlayerEntity> list = AstralPhantomEntity.this.getWorld().getPlayers(this.PLAYERS_IN_RANGE_PREDICATE, AstralPhantomEntity.this, AstralPhantomEntity.this.getBoundingBox().expand(16.0, 64.0, 16.0));
        if (!list.isEmpty()) {
            for (PlayerEntity playerEntity : list) {
                if (!AstralPhantomEntity.this.isTarget(playerEntity, TargetPredicate.DEFAULT)) continue;
                if (!UnboundEntityComponents.CURTAIN.get(playerEntity).isAware()) {
                    continue;
                }
                AstralPhantomEntity.this.setTarget(playerEntity);
                return;
            }
        }
    }

    private void playSound(SoundEvent soundEvent) {
        this.getWorld().playSound(null, this.getBlockPos(), soundEvent, this.getSoundCategory(), 1.0f, 1.0f);
    }

    public void teleport(double min, double max) {
        if (min > max) {
            return;
        }
        World world = this.getWorld();
        if (world.isClient()) {
            return;
        }
        Random random = world.getRandom();
        double xOffset = MathHelper.nextDouble(random, -max, max);
        double yOffset = MathHelper.nextDouble(random, -max, max);
        double zOffset = MathHelper.nextDouble(random, -max, max);
        if (Math.abs(xOffset) - min < 0 && Math.abs(zOffset) - min < 0) {
            this.teleport();
            return;
        }
        this.teleport(this.getX() + xOffset, this.getY() + yOffset, this.getZ() + zOffset, false);
        this.getWorld().emitGameEvent(GameEvent.TELEPORT, this.getPos(), GameEvent.Emitter.of(this));
        if (!this.isSilent()) {
            this.playSound(UnboundSoundEvents.ENTITY_ASTRAL_PHANTOM_TELEPORT);
        }
        this.addStatusEffect(new StatusEffectInstance(StatusEffects.INVISIBILITY, 40, 0, true, true), this);
        if (this.isOnFire()) {
            this.setOnFire(false);
        }
    }

    public void teleport() {
        this.teleport(2, 8);
    }
}