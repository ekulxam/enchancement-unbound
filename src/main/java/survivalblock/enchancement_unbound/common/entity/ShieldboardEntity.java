package survivalblock.enchancement_unbound.common.entity;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.entity.*;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.predicate.entity.EntityPredicates;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundDamageTypes;
import survivalblock.enchancement_unbound.common.init.UnboundEntityTypes;

import java.util.List;

public class ShieldboardEntity extends Entity {
    private static final TrackedData<ItemStack> SHIELD_STACK = DataTracker.registerData(ShieldboardEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
    private float ticksUnderwater;
    private double waterLevel;
    private BoatEntity.Location location;
    private BoatEntity.Location lastLocation;
    private double fallVelocity;

    public ShieldboardEntity(EntityType<?> type, World world) {
        super(type, world);
    }

    public ShieldboardEntity(World world, LivingEntity rider, ItemStack stack) {
        super(UnboundEntityTypes.SHIELDBOARD, world);
        this.dataTracker.set(SHIELD_STACK, stack.copy());
        this.setPos(rider.getX(), rider.getY(), rider.getZ());
    }

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(SHIELD_STACK, ItemStack.EMPTY);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Shield", NbtElement.COMPOUND_TYPE)) {
            this.dataTracker.set(SHIELD_STACK, ItemStack.fromNbt(nbt.getCompound("Shield")));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("Shield", this.dataTracker.get(SHIELD_STACK).writeNbt(new NbtCompound()));
    }

    @Override
    public boolean collidesWith(Entity other) {
        return BoatEntity.canCollide(this, other);
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        this.scheduleVelocityUpdate();
        if (!this.getWorld().isClient()) {
            this.kill();
        }
        return true;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource damageSource) {
        return this.isRemoved() || this.isInvulnerable() && !damageSource.isIn(DamageTypeTags.BYPASSES_INVULNERABILITY) && !damageSource.isSourceCreativePlayer() || damageSource.isIn(DamageTypeTags.IS_FIRE) || damageSource.isIn(DamageTypeTags.IS_FALL);
    }

    @Override
    public boolean isFireImmune() {
        return true;
    }

    @Override
    public void remove(RemovalReason reason) {
        LivingEntity controllingPassenger = this.getControllingPassenger();
        if (controllingPassenger instanceof PlayerEntity player) {
            player.getInventory().insertStack(this.dataTracker.get(SHIELD_STACK));
        } else {
            this.dropStack(this.dataTracker.get(SHIELD_STACK));
        }
        this.dataTracker.set(SHIELD_STACK, ItemStack.EMPTY);
        if (this.hasPassengers()) {
            this.removeAllPassengers();
        }
        super.remove(reason);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        this.discard();
        super.removePassenger(passenger);
    }

    @Override
    public void tick() {
        this.lastLocation = this.location;
        this.location = this.checkLocation();
        this.ticksUnderwater = this.location == BoatEntity.Location.UNDER_WATER || this.location == BoatEntity.Location.UNDER_FLOWING_WATER ? this.ticksUnderwater + 1.0f : 0.0f;
        super.tick();
        LivingEntity living = this.getControllingPassenger();
        if (living == null) {
            return;
        }
        this.tickRotation(getControlledRotation(living));
        this.tickMovement();
        this.checkBlockCollision();
        List<Entity> list = this.getWorld().getOtherEntities(this, this.getBoundingBox().expand(0.4f, -0.02f, 0.4f), EntityPredicates.canBePushedBy(this));
        if (!list.isEmpty()) {
            for (Entity entity : list) {
                if (this.hasPassenger(entity) || entity.hasPassenger(this)) continue;
                this.pushAwayFrom(entity);
                entity.damage(ModDamageTypes.create(getWorld(), UnboundDamageTypes.SHIELDBOARD_COLLISION, this, living), (float) UnboundConfig.slideImpactDamage);
            }
        }
    }

    private void tickRotation(Vec2f rotation) {
        this.setRotation(rotation.y, rotation.x);
        this.setYaw(this.getYaw());
        this.prevYaw = this.getYaw();
    }

    private void tickMovement() {
        if (this.isLogicalSideForUpdatingMovement()) {
            this.updateVelocity();
            if (this.getWorld().isClient) {
                this.board();
            }
            this.move(MovementType.SELF, this.getVelocity());
        } else {
            this.setVelocity(Vec3d.ZERO);
        }
    }

    protected Vec2f getControlledRotation(LivingEntity controllingPassenger) {
        return new Vec2f(controllingPassenger.getPitch() * 0.5f, controllingPassenger.getYaw());
    }

    @Override
    public void changeLookDirection(double cursorDeltaX, double cursorDeltaY) {
        super.changeLookDirection(cursorDeltaX, cursorDeltaY);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity entity = this.getFirstPassenger();
        return entity instanceof LivingEntity living ? living : null;
    }

    public ItemStack asItemStack() {
        return this.dataTracker.get(SHIELD_STACK).copy();
    }

    @Override
    public boolean isPushable() {
        return true;
    }

    @Override
    public boolean isCollidable() {
        return true;
    }

    @Override
    public double getMountedHeightOffset() {
        return super.getMountedHeightOffset() + 0.35;
    }

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengerList().size() < this.getMaxPassengers() && !this.isSubmergedIn(FluidTags.WATER);
    }

    protected int getMaxPassengers() {
        return 1;
    }

    private void board() {
        if (!this.hasPassengers()) {
            return;
        }
        this.velocityDirty = true;
        float speed = 0.75f; // speed of board (in blocks/sec) in air is equivalent to speed * 20
        if (this.getNearbySlipperiness() > 0) {
            speed *= (float) (1 + this.getNearbySlipperiness() * 0.8);
        }
        Vec3d currentVelocity = this.getVelocity();
        this.setVelocity(MathHelper.sin(-this.getYaw() * ((float) Math.PI / 180)) * speed, currentVelocity.y, MathHelper.cos(this.getYaw() * ((float) Math.PI / 180)) * speed);
        this.velocityModified = true;
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.setPosition(x, y, z);
        this.setYaw(yaw);
        this.setPitch(pitch);
    }

    private BoatEntity.Location checkLocation() {
        BoatEntity.Location location = this.getUnderWaterLocation();
        if (location != null) {
            this.waterLevel = this.getBoundingBox().maxY;
            return location;
        }
        if (this.checkBoatInWater()) {
            return BoatEntity.Location.IN_WATER;
        }
        float f = this.getNearbySlipperiness();
        if (f > 0.0f) {
            return BoatEntity.Location.ON_LAND;
        }
        return BoatEntity.Location.IN_AIR;
    }

    public float getWaterHeightBelow() {
        Box box = this.getBoundingBox();
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.maxY);
        int l = MathHelper.ceil(box.maxY - this.fallVelocity);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        block0: for (int o = k; o < l; ++o) {
            float f = 0.0f;
            for (int p = i; p < j; ++p) {
                for (int q = m; q < n; ++q) {
                    mutable.set(p, o, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (fluidState.isIn(FluidTags.WATER)) {
                        f = Math.max(f, fluidState.getHeight(this.getWorld(), mutable));
                    }
                    if (f >= 1.0f) continue block0;
                }
            }
            if (!(f < 1.0f)) continue;
            return (float)mutable.getY() + f;
        }
        return l + 1;
    }

    public float getNearbySlipperiness() {
        Box box = this.getBoundingBox();
        Box box2 = new Box(box.minX, box.minY - 0.001, box.minZ, box.maxX, box.minY, box.maxZ);
        int i = MathHelper.floor(box2.minX) - 1;
        int j = MathHelper.ceil(box2.maxX) + 1;
        int k = MathHelper.floor(box2.minY) - 1;
        int l = MathHelper.ceil(box2.maxY) + 1;
        int m = MathHelper.floor(box2.minZ) - 1;
        int n = MathHelper.ceil(box2.maxZ) + 1;
        VoxelShape voxelShape = VoxelShapes.cuboid(box2);
        float f = 0.0f;
        int o = 0;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int p = i; p < j; ++p) {
            for (int q = m; q < n; ++q) {
                int r = (p == i || p == j - 1 ? 1 : 0) + (q == m || q == n - 1 ? 1 : 0);
                if (r == 2) continue;
                for (int s = k; s < l; ++s) {
                    if (r > 0 && (s == k || s == l - 1)) continue;
                    mutable.set(p, s, q);
                    BlockState blockState = this.getWorld().getBlockState(mutable);
                    if (blockState.getBlock() instanceof LilyPadBlock || !VoxelShapes.matchesAnywhere(blockState.getCollisionShape(this.getWorld(), mutable).offset(p, s, q), voxelShape, BooleanBiFunction.AND)) continue;
                    f += blockState.getBlock().getSlipperiness();
                    ++o;
                }
            }
        }
        return f / (float)o;
    }

    private boolean checkBoatInWater() {
        Box box = this.getBoundingBox();
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.minY);
        int l = MathHelper.ceil(box.minY + 0.001);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        boolean bl = false;
        this.waterLevel = -1.7976931348623157E308;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int o = i; o < j; ++o) {
            for (int p = k; p < l; ++p) {
                for (int q = m; q < n; ++q) {
                    mutable.set(o, p, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (!fluidState.isIn(FluidTags.WATER)) continue;
                    float f = (float)p + fluidState.getHeight(this.getWorld(), mutable);
                    this.waterLevel = Math.max(f, this.waterLevel);
                    bl |= box.minY < (double)f;
                }
            }
        }
        return bl;
    }

    @Nullable
    private BoatEntity.Location getUnderWaterLocation() {
        Box box = this.getBoundingBox();
        double d = box.maxY + 0.001;
        int i = MathHelper.floor(box.minX);
        int j = MathHelper.ceil(box.maxX);
        int k = MathHelper.floor(box.maxY);
        int l = MathHelper.ceil(d);
        int m = MathHelper.floor(box.minZ);
        int n = MathHelper.ceil(box.maxZ);
        boolean bl = false;
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (int o = i; o < j; ++o) {
            for (int p = k; p < l; ++p) {
                for (int q = m; q < n; ++q) {
                    mutable.set(o, p, q);
                    FluidState fluidState = this.getWorld().getFluidState(mutable);
                    if (!fluidState.isIn(FluidTags.WATER) || !(d < (double)((float)mutable.getY() + fluidState.getHeight(this.getWorld(), mutable)))) continue;
                    if (fluidState.isStill()) {
                        bl = true;
                        continue;
                    }
                    return BoatEntity.Location.UNDER_FLOWING_WATER;
                }
            }
        }
        return bl ? BoatEntity.Location.UNDER_WATER : null;
    }

    private void updateVelocity() {
        double downward = this.hasNoGravity() ? 0.0 : (double) -0.04f;
        double upwardIThink = 0.0;
        if (this.lastLocation == BoatEntity.Location.IN_AIR && this.location != BoatEntity.Location.IN_AIR && this.location != BoatEntity.Location.ON_LAND) {
            this.waterLevel = this.getBodyY(1.0);
            this.setPosition(this.getX(), (double)(this.getWaterHeightBelow() - this.getHeight()) + 0.101, this.getZ());
            this.setVelocity(this.getVelocity().multiply(1.0, 0.0, 1.0));
            this.fallVelocity = 0.0;
            this.location = BoatEntity.Location.IN_WATER;
        } else {
            if (this.location == BoatEntity.Location.IN_WATER) {
                upwardIThink = (this.waterLevel - this.getY()) / (double)this.getHeight();
            } else if (this.location == BoatEntity.Location.UNDER_FLOWING_WATER) {
                downward = -7.0E-4;
            } else if (this.location == BoatEntity.Location.UNDER_WATER) {
                upwardIThink = 0.01f;
            }
            Vec3d vec3d = this.getVelocity();
            this.setVelocity(vec3d.x, vec3d.y + downward, vec3d.z);
            if (upwardIThink > 0.0) {
                Vec3d vec3d2 = this.getVelocity();
                this.setVelocity(vec3d2.x, (vec3d2.y + upwardIThink * 0.06153846016296973) * 0.75, vec3d2.z);
            }
        }
    }

    @Override
    public Direction getMovementDirection() {
        return super.getMovementDirection().rotateYClockwise();
    }

    @Override
    protected Entity.MoveEffect getMoveEffect() {
        return Entity.MoveEffect.EVENTS;
    }

    @Override
    protected void fall(double heightDifference, boolean onGround, BlockState state, BlockPos landedPosition) {
        this.fallVelocity = this.getVelocity().y;
        if (this.hasVehicle()) {
            return;
        }
        if (onGround) {
            if (this.fallDistance > 3.0f) {
                if (this.location != BoatEntity.Location.ON_LAND) {
                    this.onLanding();
                    return;
                }
            }
            this.onLanding();
            this.fallDistance -= (float) heightDifference;
        }
    }

    @Override
    public boolean isOnGround() {
        return super.isOnGround();
    }

    @Nullable
    @Override
    public ItemStack getPickBlockStack() {
        return this.asItemStack();
    }

    @Override
    public boolean canHit() {
        return this.getControllingPassenger() == null;
    }

    public boolean hasGlint() {
        return this.dataTracker.get(SHIELD_STACK).hasGlint();
    }
}