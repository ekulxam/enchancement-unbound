package survivalblock.enchancement_unbound.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ServerTickingComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.MathHelper;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

public class CurtainComponent implements AutoSyncedComponent, ServerTickingComponent {
    private final LivingEntity obj;
    private int ticksInCurtain = 0;
    private int maxTicks;
    private int cooldown = 0;
    private boolean activated = false;
    private double multiplier;
    public CurtainComponent(LivingEntity obj) {
        this.obj = obj;
    }

    @Override
    public void serverTick() {
        if (this.activated) {
            if (this.ticksInCurtain % 20 == 10) {
                this.setMultiplier(multiplier * 0.9);
            }
            if (++this.ticksInCurtain > maxTicks) {
                this.setInCurtain(false);
                this.ticksInCurtain = 0;
            }
        } else if (this.cooldown > 0) {
            --this.cooldown;
        }
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        this.activated = tag.getBoolean("IsInCurtain");
        this.ticksInCurtain = tag.getInt("TicksInCurtain");
        this.maxTicks = tag.getInt("TickLimit");
        this.cooldown = tag.getInt("CooldownTicks");
        this.multiplier = tag.getDouble("CurtainMultiplier");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IsInCurtain", this.activated);
        tag.putInt("TicksInCurtain", this.ticksInCurtain);
        tag.putInt("TickLimit", this.maxTicks);
        tag.putInt("CooldownTicks", this.cooldown);
        tag.putDouble("CurtainMultiplier", this.multiplier);
    }
    public boolean isInCurtain(){
        return this.activated;
    }
    public void setInCurtain(boolean value){
        this.multiplier = Math.max(this.multiplier, 1);
        if (value && this.cooldown <= 0) {
            this.cooldown = 0;
            this.activated = true;
            this.maxTicks = MathHelper.clamp(maxTicks + 40, 60, 3600);
            this.multiplier *= 1.15;
        } else {
            this.activated = false;
            this.maxTicks = 60;
            this.ticksInCurtain = 0;
            if (this.cooldown <= 0) {
                this.cooldown = 600;
            }
        }
        UnboundEntityComponents.CURTAIN.sync(this.obj);
    }

    public void setMultiplier(double value){
        this.multiplier = value;
        UnboundEntityComponents.CURTAIN.sync(this.obj);
    }

    public double getMultiplier(){
        this.multiplier = Math.max(this.multiplier, 1);
        return this.multiplier;
    }
}
