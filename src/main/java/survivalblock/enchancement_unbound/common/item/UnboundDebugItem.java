package survivalblock.enchancement_unbound.common.item;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;

public class UnboundDebugItem extends Item {
    public UnboundDebugItem(Settings settings) {
        super(settings);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        ItemStack stack = user.getStackInHand(hand);
        if(world.isClient()){
            return TypedActionResult.pass(stack);
        }
        boolean canInteractWithWorld = user.getAbilities().allowModifyWorld;
        user.sendMessage(Text.literal(String.valueOf(canInteractWithWorld)), true);
        return TypedActionResult.success(stack, true);
    }
}
