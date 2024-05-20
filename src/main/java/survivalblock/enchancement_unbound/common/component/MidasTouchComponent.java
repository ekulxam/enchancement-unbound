package survivalblock.enchancement_unbound.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.init.ModDamageTypes;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.minecraft.block.Blocks;
import net.minecraft.entity.*;
import net.minecraft.entity.ai.brain.MemoryModuleType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.*;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.BlockStateParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Unit;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.enchancement_unbound.common.init.UnboundDamageTypes;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.init.UnboundSoundEvents;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;
import survivalblock.enchancement_unbound.mixin.midastouch.EntityAccessor;
import survivalblock.enchancement_unbound.mixin.midastouch.MobEntityAccessor;

import java.util.UUID;

public class MidasTouchComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final LivingEntity obj;
    private boolean isGolden;
    private int karma;
    @Nullable
    private UUID oneWhoWrongedUuid;
    @Nullable
    private PlayerEntity oneWhoWronged;
    private boolean hadNoAI;
    private boolean wasPersistent;
    private boolean wasSprinting;
    private int statueTicks;
    private static final int KARMA_LIMIT = 20;
    private Vec3d position;
    private EntityPose forcedPose;
    private float forcedHeadYaw;
    private float forcedBodyYaw;
    private float forcedPitch;
    private float forcedLimbAngle;
    private float forcedLimbDistance;

    public MidasTouchComponent(LivingEntity living){
        this.obj = living;
        this.isGolden = false;
        this.karma = 0;
        this.oneWhoWronged = null;
        this.oneWhoWrongedUuid = null;
        this.hadNoAI = false;
        this.wasPersistent = false;
        this.wasSprinting = false;
        this.statueTicks = 0;
        this.position = Vec3d.ZERO;
        this.forcedPose = EntityPose.STANDING;
        this.forcedHeadYaw = 0.0F;
        this.forcedBodyYaw = 0.0F;
        this.forcedPitch = 0.0F;
        this.forcedLimbAngle = 0.0F;
        this.forcedLimbDistance = 0.0F;
    }

    public boolean shouldUndo(){
        return this.isWet() || ModEntityComponents.EXTENDED_WATER.get(this.obj).getTicksWet() > 0 || this.obj.getWorld().getDimension().ultrawarm() || expired() || this.obj.getHealth() <= 0 || ModEntityComponents.FROZEN.get(this.obj).isFrozen();
    }

    private boolean isWet(){
        return this.obj.isWet() || this.obj.isUsingRiptide();
    }

    private boolean expired(){
        return this.statueTicks >= 12000 || (this.statueTicks >= 40 && UnboundUtil.cannotExecute(this.obj, false)) || (this.statueTicks >= 100 && this.obj instanceof PlayerEntity);
    }

    public void undo(){
        if (!(this.obj.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        this.setGolden(false);
        if (this.obj instanceof PathAwareEntity pathAwareEntity) {
            pathAwareEntity.getNavigation().stop();
        }
        Box box = this.obj.getBoundingBox().expand(0.1);
        BlockStateParticleEffect blockParticle = new BlockStateParticleEffect(ParticleTypes.BLOCK, Blocks.GOLD_BLOCK.getDefaultState());
        Random random = serverWorld.getRandom();
        for (float i = 0; i < box.getAverageSideLength() + 1; i += 0.1F) {
            serverWorld.spawnParticles(blockParticle, MathHelper.nextDouble(random, box.minX - 1, box.maxX + 1), MathHelper.nextDouble(random, box.minY, box.maxY), MathHelper.nextDouble(random, box.minZ - 1, box.maxZ + 1), 1, 0, 0 ,0, 0.1);
        }
    }

    private boolean isPlayerImmuneToKarma(){
        return this.obj instanceof PlayerEntity player && (player.isCreative() || player.isSpectator());
    }

    @Override
    public void tick() {
        World world = this.obj.getWorld();
        if (this.isGolden) {
            this.obj.getBrain().forgetAll();
            if (!shouldUndo()) {
                if (!world.isClient() && world.getTime() % 40 == 0) {
                    updateOneWhoWronged();
                }
                this.obj.setVelocity(Vec3d.ZERO);
                this.obj.setPosition(this.position);
                this.obj.updateTrackedPosition(this.position.x, this.position.y, this.position.z);
                this.obj.setPose(forcedPose);
                if (this.obj instanceof PathAwareEntity pathAwareEntity) {
                    pathAwareEntity.getNavigation().stop();
                }
                stopRiding();
            }
        }
        if (!world.isClient()) {
            if (this.isGolden) {
                this.incrementStatueTicks();
                ((EntityAccessor) this.obj).invokeCheckWaterState();
                if (shouldUndo()) {
                    undo();
                }
            }
            if (this.karma > KARMA_LIMIT) {
                if (this.isPlayerImmuneToKarma()) {
                    resetKarma();
                }
                if (this.karma > KARMA_LIMIT) {
                    LightningEntity lightningEntity = EntityType.LIGHTNING_BOLT.create(world);
                    if (lightningEntity != null) {
                        lightningEntity.refreshPositionAfterTeleport(Vec3d.ofBottomCenter(this.obj.getBlockPos()));
                        ModEntityComponents.CHANNELING.get(lightningEntity).setSafe(true);
                        world.spawnEntity(lightningEntity);
                        this.obj.damage(ModDamageTypes.create(world, UnboundDamageTypes.MIDAS_LINK), 4f);
                    }
                    if (this.obj.isRemoved() || !this.obj.isAlive()) {
                        this.karma = 0;
                    }
                }
            }
        }
    }

    public int getStatueTicks() {
        return this.statueTicks;
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.isGolden = tag.getBoolean("Golden");
        this.karma = tag.getInt("Karma");
        this.hadNoAI = tag.getBoolean("HadNoAI");
        this.wasPersistent = tag.getBoolean("WasPersistent");
        this.wasSprinting = tag.getBoolean("WasSprinting");
        if (tag.containsUuid("GetVengeanceOnThisPlayer")) {
            this.oneWhoWrongedUuid = tag.getUuid("GetVengeanceOnThisPlayer");
        } else {
            this.oneWhoWrongedUuid = null;
        }
        this.statueTicks = tag.getInt("StatueTicks");
        this.position = new Vec3d(tag.getDouble("PosX"), tag.getDouble("PosY"), tag.getDouble("PosZ"));
        this.forcedPose = EntityPose.valueOf(tag.getString("ForcedPose"));
        this.forcedHeadYaw = tag.getFloat("ForcedHeadYaw");
        this.forcedBodyYaw = tag.getFloat("ForcedBodyYaw");
        this.forcedPitch = tag.getFloat("ForcedPitch");
        this.forcedLimbAngle = tag.getFloat("ForcedLimbAngle");
        this.forcedLimbDistance = tag.getFloat("ForcedLimbDistance");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("Golden", this.isGolden);
        tag.putInt("Karma", this.karma);
        tag.putBoolean("HadNoAI", this.hadNoAI);
        tag.putBoolean("WasPersistent", this.wasPersistent);
        tag.putBoolean("WasSprinting", this.wasSprinting);
        if (this.oneWhoWronged != null) tag.putUuid("GetVengeanceOnThisPlayer", this.oneWhoWronged.getUuid());
        tag.putInt("StatueTicks", this.statueTicks);
        tag.putDouble("PosX", this.position.x);
        tag.putDouble("PosY", this.position.y);
        tag.putDouble("PosZ", this.position.z);
        tag.putString("ForcedPose", this.forcedPose.toString());
        tag.putFloat("ForcedHeadYaw", this.forcedHeadYaw);
        tag.putFloat("ForcedBodyYaw", this.forcedBodyYaw);
        tag.putFloat("ForcedPitch", this.forcedPitch);
        tag.putFloat("ForcedLimbAngle", this.forcedLimbAngle);
        tag.putFloat("ForcedLimbDistance", this.forcedLimbDistance);
    }

    private void stopRiding() {
        if (this.obj.getWorld().isClient()) {
            return;
        }
        Entity vehicle = this.obj.getVehicle();
        if (vehicle != null) {
            this.obj.stopRiding();
        }
    }

    public void accumulateKarma(){
        this.karma++;
        sync();
    }

    private void sync() {
        UnboundEntityComponents.MIDAS_TOUCH.sync(this.obj);
    }

    private void incrementStatueTicks(){
        this.statueTicks++;
        sync();
    }

    private void resetStatueTicks(){
        this.statueTicks = 0;
        sync();
    }

    public void resetKarma(){
        this.karma = 0;
        sync();
    }

    public boolean isGolden(){
        return this.isGolden;
    }

    public boolean shouldPreventTicking(){
        return this.isGolden() && this.getStatueTicks() > 5;
    }

    public EntityPose getForcedPose() {
        return this.forcedPose;
    }

    public float getForcedBodyYaw() {
        return forcedBodyYaw;
    }

    public float getForcedHeadYaw() {
        return forcedHeadYaw;
    }

    public float getForcedPitch() {
        return forcedPitch;
    }

    public void setGolden(boolean golden) {
        this.isGolden = golden;
        if (golden) {
            this.position = this.obj.getPos();
            this.forcedPose = this.obj.getPose();
            this.forcedHeadYaw = this.obj.getHeadYaw();
            this.forcedBodyYaw = this.obj.getBodyYaw();
            this.forcedPitch = this.obj.getPitch();
            final float maxRand = 0.5F;
            this.forcedLimbAngle = MathHelper.nextFloat(this.obj.getRandom(), -maxRand, maxRand);
            this.forcedLimbDistance = MathHelper.nextFloat(this.obj.getRandom(), -maxRand, maxRand);
            if (this.obj instanceof MobEntity mob) {
                mob.setTarget(null);
                this.hadNoAI = mob.isAiDisabled();
                mob.setAiDisabled(true);
                this.wasPersistent = mob.isPersistent();
                mob.setPersistent();
            }
            if (this.obj instanceof PlayerEntity player) {
                this.wasSprinting = player.isSprinting();
            }
            this.resetKarma();
            this.stopRiding();
        } else {
            if (this.obj instanceof MobEntity mob) {
                if (!hadNoAI) mob.setAiDisabled(false);
                if (!wasPersistent) ((MobEntityAccessor) mob).setPersistent(false);
            }
            this.obj.getBrain().forgetAll();
            if (this.obj instanceof WardenEntity warden) {
                warden.getBrain().remember(MemoryModuleType.DIG_COOLDOWN, Unit.INSTANCE, 1200L);
            }
            if (this.obj instanceof PlayerEntity player) {
                if (wasSprinting) player.setSprinting(true);
            }
            this.obj.setBodyYaw(this.forcedBodyYaw);
            this.obj.setHeadYaw(this.forcedHeadYaw);
            this.obj.setPitch(this.getForcedPitch());
            this.obj.setPose(this.forcedPose);
        }
        this.resetStatueTicks();
        if (this.obj.getWorld() instanceof ServerWorld serverWorld) {
            serverWorld.playSound(null, this.obj.getBlockPos(), golden ? UnboundSoundEvents.ENTITY_GENERIC_MIDAS_TOUCHED : UnboundSoundEvents.ENTITY_GENERIC_STATUE_UNDONE, this.obj instanceof PlayerEntity ? SoundCategory.PLAYERS : SoundCategory.NEUTRAL, golden ? 2 : 10, MathHelper.nextBetween(serverWorld.getRandom(), 0.8f,1.2f));
        }
        sync();
    }

    private void weirdCheckThing() {
        if (oneWhoWronged == null || oneWhoWronged.getHealth() <= 0) {
            this.undo();
        }
    }

    private void updateOneWhoWronged() {
        if (!(this.obj.getWorld() instanceof ServerWorld serverWorld)) {
            return;
        }
        if (oneWhoWronged != null) {
            weirdCheckThing();
            return;
        }
        if (oneWhoWrongedUuid == null) {
            return;
        }
        oneWhoWronged = serverWorld.getPlayerByUuid(oneWhoWrongedUuid);
        weirdCheckThing();
        sync();
    }

    public void damageLink(){
        updateOneWhoWronged();
        if (oneWhoWronged != null) {
            DamageSource source = ModDamageTypes.create(this.obj.getWorld(), UnboundDamageTypes.MIDAS_LINK, this.obj);
            float amount = 0.25f * Math.max(1, UnboundEntityComponents.MIDAS_TOUCH.get(oneWhoWronged).karma / 5f);
            oneWhoWronged.damage(source, amount);
        }
    }

    public void setOneWhoWronged(@Nullable PlayerEntity oneWhoWronged) {
        this.oneWhoWronged = oneWhoWronged;
        if (oneWhoWronged != null) this.oneWhoWrongedUuid = this.oneWhoWronged.getUuid();
    }

    public float getForcedLimbDistance() {
        return this.forcedLimbDistance;
    }

    public float getForcedLimbAngle() {
        return this.forcedLimbAngle;
    }
}
