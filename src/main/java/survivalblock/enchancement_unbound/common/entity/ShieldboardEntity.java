package survivalblock.enchancement_unbound.common.entity;

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
import net.minecraft.item.Items;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.function.BooleanBiFunction;
import net.minecraft.util.math.*;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.enchancement_unbound.common.init.UnboundEntities;

import java.util.UUID;

public class ShieldboardEntity extends Entity implements Ownable {

    private static final TrackedData<Boolean> ENCHANTED = DataTracker.registerData(ShieldboardEntity.class, TrackedDataHandlerRegistry.BOOLEAN);
    @Nullable
    private UUID ownerUuid;
    @Nullable
    private Entity owner;
    private float ticksUnderwater;
    private double waterLevel;
    private BoatEntity.Location location;
    public ShieldboardEntity(EntityType<?> type, World world) {
        super(type, world);
        this.shieldStack = new ItemStack(Items.SHIELD);
    }

    public ShieldboardEntity(World world, LivingEntity owner, ItemStack stack) {
        super(UnboundEntities.SHIELDBOARD, world);
        this.dataTracker.set(ENCHANTED, stack.hasGlint());
        this.shieldStack = new ItemStack(Items.SHIELD);
        this.shieldStack = stack.copy();
        this.setOwner(owner);
        this.setPos(owner.getX(), owner.getY(), owner.getZ());
    }

    public void setOwner(@Nullable Entity entity) {
        if (entity != null) {
            this.ownerUuid = entity.getUuid();
            this.owner = entity;
        }
    }

    private ItemStack shieldStack;

    @Override
    protected void initDataTracker() {
        this.dataTracker.startTracking(ENCHANTED, false);
    }

    @Override
    protected void readCustomDataFromNbt(NbtCompound nbt) {
        if (nbt.contains("Shield", NbtElement.COMPOUND_TYPE)) {
            this.shieldStack = ItemStack.fromNbt(nbt.getCompound("Shield"));
        }
    }

    @Override
    protected void writeCustomDataToNbt(NbtCompound nbt) {
        nbt.put("Shield", this.shieldStack.writeNbt(new NbtCompound()));
    }

    @Override
    public boolean collidesWith(Entity other) {
        return BoatEntity.canCollide(this, other);
    }

    @Override
    @Nullable
    public Entity getOwner() {
        if (this.owner != null && !this.owner.isRemoved()) {
            return this.owner;
        }
        if (this.ownerUuid != null && this.getWorld() instanceof ServerWorld) {
            this.owner = ((ServerWorld)this.getWorld()).getEntity(this.ownerUuid);
            return this.owner;
        }
        return null;
    }

    @Override
    public boolean damage(DamageSource source, float amount) {
        if (this.isInvulnerableTo(source)) {
            return false;
        }
        this.scheduleVelocityUpdate();
        this.delete(RemovalReason.KILLED);
        return true;
    }

    private void delete(RemovalReason reason){
        this.dropStack(this.shieldStack);
        if (this.hasPassengers()) {
            this.removeAllPassengers();
        }
        this.remove(reason);
    }

    @Override
    protected void removePassenger(Entity passenger) {
        super.removePassenger(passenger);
        this.delete(RemovalReason.DISCARDED);
    }

    @Override
    public void tick() {
        this.location = this.checkLocation();
        this.ticksUnderwater = this.location == BoatEntity.Location.UNDER_WATER || this.location == BoatEntity.Location.UNDER_FLOWING_WATER ? (this.ticksUnderwater += 1.0f) : 0.0f;
        if (!this.getWorld().isClient && this.ticksUnderwater >= 60.0f) {
            this.removeAllPassengers();
        }
        super.tick();
        LivingEntity living = this.getControllingPassenger();
        if (living == null) {
            return;
        }
        Vec2f rotation = getControlledRotation(living);
        this.setRotation(rotation.y, rotation.x);
        this.setYaw(this.getYaw());
        this.prevYaw = this.getYaw();
        if (this.isLogicalSideForUpdatingMovement()) {
            if (this.getWorld().isClient) {
                this.board();
            }
            this.move(MovementType.SELF, this.getVelocity());
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
    public ItemStack asItemStack(){
        return this.shieldStack.copy();
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
        return super.getMountedHeightOffset() * 5;
    }

    private BoatEntity.Location checkLocation() {
        BoatEntity.Location location = this.getUnderWaterLocation();
        if (location != null) {
            this.waterLevel = this.getBoundingBox().maxY;
            return location;
        }
        if (this.checkIsInWater()) {
            return BoatEntity.Location.IN_WATER;
        }
        float f = this.getNearbySlipperiness();
        if (f > 0.0f) {
            return BoatEntity.Location.ON_LAND;
        }
        return BoatEntity.Location.IN_AIR;
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

    private boolean checkIsInWater() {
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

    @Override
    protected boolean canAddPassenger(Entity passenger) {
        return this.getPassengerList().size() < this.getMaxPassengers() && !this.isSubmergedIn(FluidTags.WATER);
    }
    protected int getMaxPassengers() {
        return 2;
    }

    @Override
    public boolean isSubmergedInWater() {
        return this.location == BoatEntity.Location.UNDER_WATER || this.location == BoatEntity.Location.UNDER_FLOWING_WATER;
    }

    private void board() {
        if (!this.hasPassengers()) {
            return;
        }
        this.velocityDirty = true;
        float f = 0.25f;
        this.setVelocity(MathHelper.sin(-this.getYaw() * ((float)Math.PI / 180)) * f, 0.0, MathHelper.cos(this.getYaw() * ((float)Math.PI / 180)) * f);
        this.velocityModified = true;
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps, boolean interpolate) {
        this.setPosition(x, y, z);
        this.setYaw(yaw);
        this.setPitch(pitch);
    }
}
