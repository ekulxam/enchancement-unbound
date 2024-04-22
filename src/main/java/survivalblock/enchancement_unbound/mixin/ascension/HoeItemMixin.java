package survivalblock.enchancement_unbound.mixin.ascension;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

import java.util.function.Consumer;

@Mixin(HoeItem.class)
public abstract class HoeItemMixin extends Item {

    public HoeItemMixin(Settings settings) {
        super(settings);
    }

    @Inject(method = "useOnBlock", at = @At("HEAD"), cancellable = true)
    private void phaseThroughWalls(ItemUsageContext context, CallbackInfoReturnable<ActionResult> cir){
        World world = context.getWorld();
        PlayerEntity player = context.getPlayer();
        if (context.getSide() == Direction.DOWN && player != null) {
            if (!world.isClient) {
                UnboundEntityComponents.ASCENSION.get(player).setAsending(true);
                context.getStack().damage(1, player, p -> p.sendToolBreakStatus(context.getHand()));
            }
            cir.setReturnValue(ActionResult.success(world.isClient));
            cir.cancel();
        }
    }
}
