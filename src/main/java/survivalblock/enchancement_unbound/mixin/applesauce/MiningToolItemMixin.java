package survivalblock.enchancement_unbound.mixin.applesauce;

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
        if (EnchantmentHelper.getLevel(UnboundEnchantments.APPLE_SAUCE, stack) <= 0) {
            return;
        }
        if (!(state.isIn(BlockTags.LEAVES))) {
            return;
        }
        Random random = world.getRandom();
        if (MathHelper.nextBetween(random, 0, 2) >= 2) {
            Block.dropStack(world, pos, Items.APPLE.getDefaultStack());
        }
        if (MathHelper.nextBetween(random, 0, 9) >= 9) {
            Block.dropStack(world, pos, Items.GOLDEN_APPLE.getDefaultStack());
        }
    }
}
