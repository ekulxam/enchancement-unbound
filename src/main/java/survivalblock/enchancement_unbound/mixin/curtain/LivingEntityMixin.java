package survivalblock.enchancement_unbound.mixin.curtain;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Debug(export = true)
@Mixin(value = LivingEntity.class, priority = 750)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @ModifyVariable(method = "damage", at = @At("HEAD"), index = 2, argsOnly = true)
    private float sendToTheVoid(float amount, DamageSource source){
        if (source.getAttacker() instanceof LivingEntity living && hasCurtainTouch(living)) {
            UnboundEntityComponents.CURTAIN.get(this).setInCurtain(true);
            return amount * (float) UnboundEntityComponents.CURTAIN.get(this).getMultiplier();
        }
        return amount;
    }

    @Unique
    private boolean hasCurtainTouch(LivingEntity living){
        for (ItemStack stack : living.getHandItems()) {
            if (EnchantmentHelper.getLevel(UnboundEnchantments.CURTAIN, stack) > 0) return true;
        }
        return false;
    }
}
