package survivalblock.enchancement_unbound.mixin.vanillachanges.weakerfireaspect;

import com.bawnorton.mixinsquared.TargetHandler;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = MobEntity.class, priority = 1500)
public class MobEntityMixinMixin {

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.vanillachanges.rebalancefireaspect.MobEntityMixin",
            name = "Lmoriyashiine/enchancement/mixin/vanillachanges/rebalancefireaspect/MobEntityMixin;enchancement$rebalanceFireAspect(I)I"
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "HEAD"
            ), cancellable = true
    )
    private void cursePatchItemStack(int value, CallbackInfoReturnable<Integer> cir) {
        if (UnboundConfig.infiniteFireAspect) {
            cir.setReturnValue(Integer.MAX_VALUE); // practically infinite
        }
    }
}
