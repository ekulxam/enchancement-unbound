package survivalblock.enchancement_unbound.mixin.brimstone;

import com.bawnorton.mixinsquared.TargetHandler;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(value = CrossbowItem.class, priority = 1500)
public class CrossbowItemMixinMixin {
    // yeah this doesn't sound good already
    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.brimstone.CrossbowItemMixin",
            name = "enchancement$brimstone",
            prefix = "localvar"
    )
    @WrapWithCondition(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/LivingEntity;damage(Lnet/minecraft/entity/damage/DamageSource;F)Z"
            )
    )
    private static boolean selfDamageWhatNow(LivingEntity living, DamageSource source, float damage) {
        return UnboundConfig.brimstoneSelfDamage;
    }

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.brimstone.CrossbowItemMixin",
            name = "enchancement$brimstone",
            prefix = "localvar"
    )
    @WrapWithCondition(
            method = "@MixinSquared:Handler",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/entity/player/ItemCooldownManager;set(Lnet/minecraft/item/Item;I)V"
            )
    )
    private static boolean noBrimstoneCooldown(ItemCooldownManager itemCooldownManager, Item item, int cooldownTicks) {
        return !UnboundConfig.noCrossbowCooldown;
    }
}
