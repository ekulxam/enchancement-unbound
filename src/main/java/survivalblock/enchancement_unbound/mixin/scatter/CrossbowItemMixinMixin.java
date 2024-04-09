package survivalblock.enchancement_unbound.mixin.scatter;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = CrossbowItem.class, priority = 1500)
public class CrossbowItemMixinMixin {

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.scatter.CrossbowItemMixin",
            name = "Lmoriyashiine/enchancement/mixin/scatter/CrossbowItemMixin;enchancement$scatter(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;FZFFFLorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V"
    )
    @WrapWithCondition(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/ItemCooldownManager;set(Lnet/minecraft/item/Item;I)V"
            )
    )
    private static boolean noScatterCooldown(ItemCooldownManager itemCooldownManager, Item item, int cooldownTicks) {
        return !UnboundConfig.noCrossbowCooldown;
    }

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.scatter.CrossbowItemMixin",
            name = "Lmoriyashiine/enchancement/mixin/scatter/CrossbowItemMixin;enchancement$scatter(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Lnet/minecraft/item/ItemStack;FZFFFLorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I"
            )
    )
    private static int moreScatter(int original){
        return (int) (original * UnboundConfig.scatterProjectileMultiplier);
    }
}
