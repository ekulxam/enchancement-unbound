package survivalblock.enchancement_unbound.mixin.ascension;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.item.ShovelItem;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(ShovelItem.class)
public abstract class ShovelItemMixin extends Item {

    public ShovelItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void phaseThroughWalls(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (world.isClient()) {
            return;
        }
        if (!UnboundConfig.unboundEnchantments) {
            return;
        }
        if (player == null) {
            return;
        }
        if (context.getSide() == Direction.DOWN) {
            BlockPos pos = context.getBlockPos();
            if (EnchantmentHelper.getLevel(UnboundEnchantments.ASCENSION, context.getStack()) > 0  && UnboundUtil.shouldActivateAscension(player, pos)) {
                UnboundEntityComponents.ASCENSION.get(player).setAscending(true, pos.getY() + player.getHeight() - player.getBlockPos().getY());
                context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
            }
            cir.setReturnValue(ActionResult.success(world.isClient));
        }
    }
}
