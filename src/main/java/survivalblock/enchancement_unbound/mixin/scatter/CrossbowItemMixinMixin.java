package survivalblock.enchancement_unbound.mixin.scatter;

import com.bawnorton.mixinsquared.TargetHandler;
import net.minecraft.entity.player.ItemCooldownManager;
import net.minecraft.item.CrossbowItem;
import net.minecraft.item.Item;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

@Mixin(value = CrossbowItem.class, priority = 1500)
public class CrossbowItemMixinMixin {

    @TargetHandler(
            mixin = "moriyashiine.enchancement.mixin.scatter.CrossbowItemMixin",
            name = "enchancement$scatter",
            prefix = "localvar",
            print = true
    )
    @Inject(
            method = "@MixinSquared:Handler",
            at = @At("HEAD")
    )
    private static void noScatterCooldown(ItemCooldownManager itemCooldownManager, Item item, int cooldownTicks) {
        EnchancementUnbound.LOGGER.warn(EnchancementUnbound.MOD_ID);
    }
}
