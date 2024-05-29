package survivalblock.enchancement_unbound.common.component;

import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import org.ladysnake.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.EntityPose;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.Vec3d;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

public class AscensionComponent implements AutoSyncedComponent, ServerTickingComponent {
    private final PlayerEntity obj;
    private boolean activated = false;
    private double originalX;
    private double originalY;
    private double originalZ;
    private double maxYDifference;

    public AscensionComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    @Override
    public void serverTick() {
         if (this.activated) {
             if (Math.abs(this.obj.getX() - this.originalX) > 0.25 || Math.abs(this.obj.getZ() - this.originalZ) > 0.25) {
                 this.setAscending(false, 0);
                 return;
             }
             Vec3d current = this.obj.getVelocity();
             double stuff = 1;
             this.obj.velocityDirty = true;
             boolean hadNoClip = this.obj.noClip;
             this.obj.noClip =  true;
             Vec3d move = new Vec3d( this.originalX + current.x, this.obj.getY() + stuff, this.originalZ + current.z);
             this.obj.teleport(move.x, move.y, move.z);
             this.obj.noClip = hadNoClip;
             this.obj.velocityModified = true;
             if (this.obj.isFallFlying()) {
                 this.obj.stopFallFlying();
             }
             double difference = this.obj.getPos().y - this.originalY;
             boolean isSolidAtPos = this.obj.getBlockStateAtPos().isSolid();
             if (isSolidAtPos) {
                 this.originalY = Integer.MIN_VALUE;
             }
             if ((difference >= maxYDifference && !isSolidAtPos) || difference < 0) {
                 this.setAscending(false, 0);
                 this.obj.velocityDirty = true;
                 this.obj.setVelocity(new Vec3d(current.x, 0, current.z));
                 this.obj.velocityModified = true;
             }
        }
    }

    public void readFromNbt(NbtCompound tag) {
        this.activated = tag.getBoolean("Ascending");
        this.originalX = tag.getDouble("OriginalX");
        this.originalY = tag.getDouble("OriginalY");
        this.originalZ = tag.getDouble("OriginalZ");
        this.maxYDifference = tag.getDouble("MaxYDifference");
        if (this.obj.getPose().equals(EntityPose.STANDING)) {
            this.obj.setPose(EntityPose.SWIMMING);
        }
    }

    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("Ascending", this.activated);
        tag.putDouble("OriginalX", this.originalX);
        tag.putDouble("OriginalY", this.originalY);
        tag.putDouble("OriginalZ", this.originalZ);
        tag.putDouble("MaxYDifference", this.maxYDifference);
    }

    public void setAscending(boolean value, double maxYDifference){
        if (value) {
            this.activated = maxYDifference > 0;
        } else {
            this.activated = false;
        }
        Vec3d pos = this.obj.getPos();
        this.originalX = pos.x;
        this.originalY = pos.y;
        this.originalZ = pos.z;
        this.maxYDifference = maxYDifference;
        UnboundEntityComponents.ASCENSION.sync(this.obj);
    }

    public boolean isAscending() {
        return this.activated;
    }

    @Override
    public void readFromNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.readFromNbt(tag);
    }

    @Override
    public void writeToNbt(NbtCompound tag, RegistryWrapper.WrapperLookup registryLookup) {
        this.writeToNbt(tag);
    }
}
