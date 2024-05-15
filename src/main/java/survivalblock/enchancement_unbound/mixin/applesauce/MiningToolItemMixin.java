package survivalblock.enchancement_unbound.mixin.applesauce;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.ItemEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MiningToolItem;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;

@Mixin(MiningToolItem.class)
public class MiningToolItemMixin {

    @Inject(method = "postMine", at = @At("HEAD"))
    private void dropMoreApples(ItemStack stack, World world, BlockState state, BlockPos pos, LivingEntity miner, CallbackInfoReturnable<Boolean> cir){
        if (world.isClient()) {
            return;
        }
        if (!(stack.getItem() instanceof HoeItem)) {
            return;
        }
        if (!UnboundConfig.unboundEnchantments) {
            return;
        }
        int amplifier = EnchantmentHelper.getLevel(UnboundEnchantments.APPLE_SAUCE, stack);
        if (amplifier <= 0) {
            return;
        }
        if (!(state.isIn(BlockTags.LEAVES))) {
            return;
        }
        amplifier--;
        Random random = world.getRandom();
        if (MathHelper.nextBetween(random, 0, 9) >= 9 - amplifier) { // 1 in 10
            Block.dropStack(world, pos, Items.APPLE.getDefaultStack());
        }
        if (MathHelper.nextBetween(random, 0, 24) >= 24 - amplifier) { // 1 in 25
            if (MathHelper.nextBetween(random, 0, 4) >= 4 - Math.floor(amplifier / 2d)) { // 1 in 5
                Block.dropStack(world, pos, Items.ENCHANTED_GOLDEN_APPLE.getDefaultStack());
            } else {
                Block.dropStack(world, pos, Items.GOLDEN_APPLE.getDefaultStack()); // 4 in 5
            }
        }
    }

    @ModifyExpressionValue(method = "getMiningSpeedMultiplier", at = @At(value = "FIELD", target = "Lnet/minecraft/item/MiningToolItem;miningSpeed:F", opcode = Opcodes.GETFIELD))
    private float instaMineLeavesMaybe(float original, ItemStack stack, BlockState state){
        if (!(stack.getItem() instanceof HoeItem)) {
            return original;
        }
        if (!UnboundConfig.unboundEnchantments) {
            return original;
        }
        if (EnchantmentHelper.getLevel(UnboundEnchantments.APPLE_SAUCE, stack) <= 0) {
            return original;
        }
        if (!(state.isIn(BlockTags.LEAVES))) {
            return original;
        }
        return Math.max(15.0f, original);
    }
}
