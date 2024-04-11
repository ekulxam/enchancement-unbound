package survivalblock.enchancement_unbound.mixin.aegis;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(PlayerEntity.class)
public abstract class PlayerEntityMixin extends LivingEntity {

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "damageShield", at = @At(value = "HEAD"))
    private void setActiveItemStackToAegisShield(float amount, CallbackInfo ci){
        if (!this.activeItemStack.isOf(Items.SHIELD)) {
            this.activeItemStack = UnboundUtil.getFirstAegisStack(this, true);
        }
    }

    @Inject(method = "damageShield", at = @At(value = "TAIL"))
    private void clearActiveItemStack(float amount, CallbackInfo ci){
        this.clearActiveItem();
    }
}
