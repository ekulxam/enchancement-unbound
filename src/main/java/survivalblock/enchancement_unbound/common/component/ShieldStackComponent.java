package survivalblock.enchancement_unbound.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import survivalblock.enchancement_unbound.common.entity.ProjectedShieldEntity;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

public class ShieldStackComponent implements AutoSyncedComponent {
    Entity obj;
    ItemStack shieldStack;
    @Override
    public void readFromNbt(NbtCompound nbt) {
        if (nbt.contains("Shield", NbtElement.COMPOUND_TYPE)) {
            this.shieldStack = ItemStack.fromNbt(nbt.getCompound("Shield"));
        }
    }

    @Override
    public void writeToNbt(NbtCompound nbt) {
        nbt.put("Shield", shieldStack.writeNbt(new NbtCompound()));
    }

    public ShieldStackComponent(Entity entity) {
        this.obj = entity;
        this.shieldStack = ItemStack.EMPTY;
    }

    public ItemStack getShieldStack() {
        return this.shieldStack;
    }

    public void setShieldStack(ItemStack shieldStack) {
        this.shieldStack = shieldStack;
        UnboundEntityComponents.SHIELD_STACK.sync(this.obj);
    }
}
