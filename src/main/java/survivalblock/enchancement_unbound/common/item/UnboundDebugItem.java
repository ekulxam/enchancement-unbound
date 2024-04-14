package survivalblock.enchancement_unbound.common.item;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import survivalblock.enchancement_unbound.common.init.UnboundSoundEvents;

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
        // yay
        int pitch = stack.getOrCreateNbt().getInt("Pitch");
        pitch++;
        if (pitch >= 7 || pitch < 0){
            pitch = 0;
        }
        if (pitch == 0) {
            user.sendMessage(Text.of("C"), true);
        } else if (pitch == 1) {
            user.sendMessage(Text.of("D"), true);
        } else if (pitch == 2) {
            user.sendMessage(Text.of("E"), true);
        } else if (pitch == 3) {
            user.sendMessage(Text.of("F"), true);
        } else if (pitch == 4) {
            user.sendMessage(Text.of("G"), true);
        } else if (pitch == 5) {
            user.sendMessage(Text.of("A"), true);
        } else {
            user.sendMessage(Text.of("B"), true);
        }
        if (pitch < 4) {
            world.playSound(null, user.getBlockPos(), UnboundSoundEvents.ITEM_BOW_HEX, SoundCategory.PLAYERS, 1.0f, (float) pitch*0.1f + 1f);
        } else if (pitch == 4) {
            world.playSound(null, user.getBlockPos(), UnboundSoundEvents.ITEM_BOW_HEX, SoundCategory.PLAYERS, 1.0f, (float) pitch*0.1f + 1.07f);
        } else if (pitch == 6) {
            world.playSound(null, user.getBlockPos(), UnboundSoundEvents.ITEM_BOW_HEX, SoundCategory.PLAYERS, 1.0f, (float) pitch*0.1f + 1.2f);
        } else {
            world.playSound(null, user.getBlockPos(), UnboundSoundEvents.ITEM_BOW_HEX, SoundCategory.PLAYERS, 1.0f, (float) pitch*0.1f + 1.13f);
        }
        stack.getOrCreateNbt().putInt("Pitch", pitch);
        return TypedActionResult.success(stack, true);
    }
}
