package survivalblock.enchancement_unbound.mixin.vanillachanges.weakerfireaspect;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = PlayerEntity.class, priority = 1500)
public class PlayerEntityMixinMixin {

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.weakerfireaspect.PlayerEntityMixin",
            name = "Lmoriyashiine/enchancement/mixin/vanillachanges/weakerfireaspect/PlayerEntityMixin;enchancement$weakerFireAspect(I)I"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "HEAD"
            ), cancellable = true
    )
    private void cursePatchItemStack(int value, CallbackInfoReturnable<Integer> cir) {
        // more like strongerfireaspect
        if (UnboundConfig.infiniteFireAspect) {
            cir.setReturnValue(Integer.MAX_VALUE);
        }
    }
}
