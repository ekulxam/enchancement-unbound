package survivalblock.enchancement_unbound.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

public class AscensionComponent implements AutoSyncedComponent, CommonTickingComponent {
    private final PlayerEntity obj;
    private boolean activated = false;
    private float blocksAscended;
    private double x;
    private double z;
    public AscensionComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    @Override
    public void tick() {
    }

    @Override
    public void serverTick() {
        if (this.activated) {
            Vec3d pos = this.obj.getPos();
            double stuff = 0.1;
            this.obj.setPos(x, pos.y + stuff, z);
            this.blocksAscended += (float) stuff;
            if (this.obj.getBlockStateAtPos().isSolid()) {
                this.blocksAscended = 10;
            } else if (this.blocksAscended > 6) {
                this.setAsending(false);
            }
        }
        CommonTickingComponent.super.serverTick();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.activated = tag.getBoolean("Ascending");
        this.blocksAscended = tag.getFloat("BlocksAscended");
        this.x = tag.getDouble("X");
        this.z = tag.getDouble("Z");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("Ascending", this.activated);
        tag.putFloat("BlocksAscended", this.blocksAscended);
        tag.putDouble("X", this.x);
        tag.putDouble("Z", this.z);
    }
    public void setAsending(boolean value){
        this.activated = value;
        this.blocksAscended = 0;
        this.x = this.obj.getPos().x;
        this.z = this.obj.getPos().z;
        UnboundEntityComponents.CURTAIN.sync(this.obj);
    }
}
