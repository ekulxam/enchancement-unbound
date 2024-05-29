package survivalblock.enchancement_unbound.common.component;

import net.minecraft.registry.RegistryWrapper;
import org.ladysnake.cca.api.v3.component.sync.AutoSyncedComponent;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import net.minecraft.nbt.NbtCompound;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

public class BrimstoneBypassComponent implements AutoSyncedComponent {

    private final BrimstoneEntity obj;

    private boolean ignoresDamageLimit = false;

    public BrimstoneBypassComponent(BrimstoneEntity obj) {
        this.obj = obj;
    }

    public void readFromNbt(NbtCompound tag) {
        this.ignoresDamageLimit = tag.getBoolean("IgnoresDamageLimit");
    }

    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IgnoresDamageLimit", this.ignoresDamageLimit);
    }

    public void setIgnoresDamageLimit(boolean value) {
        this.ignoresDamageLimit = value;
        UnboundEntityComponents.BRIMSTONE_BYPASS.sync(this.obj);
    }

    public boolean getIgnoresDamageLimit() {
        return this.ignoresDamageLimit;
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
