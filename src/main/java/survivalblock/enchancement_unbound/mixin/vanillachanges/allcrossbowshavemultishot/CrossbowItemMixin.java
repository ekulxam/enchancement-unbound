package survivalblock.enchancement_unbound.mixin.vanillachanges.allcrossbowshavemultishot;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.item.CrossbowItem;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Debug(export = true)
@Mixin(value = CrossbowItem.class)
public class CrossbowItemMixin {

    @ModifyVariable(method = "loadProjectiles",
            at = @At("STORE"),
            name = "j")
    private static int multishotEnabled(int j, @Local (ordinal = 0) int i) {
        int correctlyCalculateMultishot = i > 0 ? 1 + 2 * i : 0;
        return UnboundConfig.allCrossbowsHaveMultishot ? Math.max(3, correctlyCalculateMultishot) : j;
    }
}
