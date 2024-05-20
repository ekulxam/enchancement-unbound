package survivalblock.enchancement_unbound.mixin.veil;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@SuppressWarnings("UnreachableCode")
@Mixin(Entity.class)
public abstract class EntityMixin {

    @ModifyReturnValue(method = "isInvisible", at = @At(value = "RETURN"))
    private boolean arathainMoment(boolean mouthpiece){
        if(UnboundConfig.veilUsersAlwaysInvisible){
            return ((Entity) (Object) this instanceof LivingEntity living && EnchantmentHelper.getEquipmentLevel(ModEnchantments.VEIL, living) > 0) || mouthpiece;
        }
        // this should work for both of them, I think (didn't specify ordinal)
        return mouthpiece;
    }

    @ModifyExpressionValue(method = "isInvisibleTo", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/Entity;isInvisible()Z"))
    private boolean veilUsersInvisble(boolean original, PlayerEntity other){
        if (original) {
            return true;
        }
        if (((Entity) (Object) this instanceof PlayerEntity player)) {
            // I just watched the End Dust video, so I have some ideas
            CurtainComponent curtainComponent = UnboundEntityComponents.CURTAIN.get(player);
            return curtainComponent.isInCurtain() && !EnchancementUtil.hasEnchantment(ModEnchantments.PERCEPTION, other);
        }
        return false;
    }

    @Inject(method = "shouldSpawnSprintingParticles", at = @At("HEAD"), cancellable = true)
    private void noSprintingParticlesInOtherPlanes(CallbackInfoReturnable<Boolean> cir) {
        if (((Entity) (Object) this instanceof PlayerEntity player)) {
            CurtainComponent curtainComponent = UnboundEntityComponents.CURTAIN.get(player);
            if (curtainComponent.isInCurtain()) {
                cir.setReturnValue(false);
            }
        }
    }
}
