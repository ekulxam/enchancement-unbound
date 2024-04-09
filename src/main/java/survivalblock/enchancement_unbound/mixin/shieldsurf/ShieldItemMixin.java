package survivalblock.enchancement_unbound.mixin.shieldsurf;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ShieldItem;
import net.minecraft.stat.Stats;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.entity.ShieldboardEntity;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;

@Mixin(ShieldItem.class)
public class ShieldItemMixin {
    @Inject(method = "use", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/PlayerEntity;setCurrentHand(Lnet/minecraft/util/Hand;)V"))
    private void rideTheLightning(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir, @Local ItemStack stack){
        if(!world.isClient() && EnchantmentHelper.getLevel(UnboundEnchantments.SHIELD_SURF, stack) > 0){
            stack.damage(1, user, (p) -> p.sendToolBreakStatus(user.getActiveHand()));
            user.incrementStat(Stats.USED.getOrCreateStat((ShieldItem) (Object) this));
            ShieldboardEntity shieldboard = new ShieldboardEntity(world, user, stack);
            world.spawnEntity(shieldboard);
            user.getInventory().removeOne(stack);
            user.startRiding(shieldboard, true);
        }
    }
}
