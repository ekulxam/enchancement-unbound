package survivalblock.enchancement_unbound.mixin.scatter;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.Item;
import net.minecraft.item.RangedWeaponItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(value = RangedWeaponItem.class, priority = 1500)
public class RangedWeaponItemMixinMixin {


    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.scatter.RangedWeaponItemMixin",
            name = "Lmoriyashiine/enchancement/mixin/scatter/RangedWeaponItemMixin;enchancement$scatterTail(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/entity/LivingEntity;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V"
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
            mixin = "moriyashiine.enchancement.mixin.scatter.RangedWeaponItemMixin",
            name = "Lmoriyashiine/enchancement/mixin/scatter/RangedWeaponItemMixin;enchancement$scatterTail(Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/util/Hand;Lnet/minecraft/item/ItemStack;Ljava/util/List;FFZLnet/minecraft/entity/LivingEntity;Lorg/spongepowered/asm/mixin/injection/callback/CallbackInfo;)V"
    )
    @ModifyExpressionValue(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/util/math/MathHelper;nextInt(Lnet/minecraft/util/math/random/Random;II)I"
            )
    )
    private static int moreScatter(int original){
        if (UnboundUtil.isBasicallyOriginal(UnboundConfig.scatterProjectileMultiplier, 1.0f)) {
            return original;
        }
        return (int) (original * UnboundConfig.scatterProjectileMultiplier);
    }
}
