package survivalblock.enchancement_unbound.mixin.bury;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import moriyashiine.enchancement.common.event.BuryEvent;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(BuryEvent.class)
public class BuryEventMixin {

    @Mixin(BuryEvent.Use.class)
    public static class UseMixin {

        @WrapWithCondition(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/player/ItemCooldownManager;set(Lnet/minecraft/item/Item;I)V"))
        private boolean burySpam(ItemCooldownManager instance, Item item, int duration) {
            return !UnboundConfig.noBuryCooldown;
        }

        @ModifyExpressionValue(method = "interact", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/EntityType;isIn(Lnet/minecraft/registry/tag/TagKey;)Z"))
        private boolean sayYesToBuryingWardens(boolean original, PlayerEntity player, World world, Hand hand, Entity entity, @Nullable EntityHitResult hitResult){
            if (UnboundConfig.canBuryEverything) {
                return false; // inverse
            }
            return original;
        }
    }
}
