package survivalblock.enchancement_unbound.mixin.veil;

import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;

@Debug(export = true)
@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world); // constructor to tell the compiler to be quiet
    }

    @ModifyArg(method = "updatePotionVisibility", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;setInvisible(Z)V"))
    private boolean arathainMoment(boolean mouthpiece){
        return EnchantmentHelper.getEquipmentLevel(ModEnchantments.VEIL, (LivingEntity) (Object) this) > 0 || mouthpiece;
        // this should work for both of them, I think (didn't specify ordinal)
    }
}
