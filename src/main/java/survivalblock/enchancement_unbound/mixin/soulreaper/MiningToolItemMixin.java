package survivalblock.enchancement_unbound.mixin.soulreaper;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.HoeItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.MiningToolItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundDamageTypes;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;

@Mixin(MiningToolItem.class)
public class MiningToolItemMixin {

    @Inject(method = "postHit", at = @At("HEAD"))
    private void reapTheExperience(ItemStack stack, LivingEntity target, LivingEntity attacker, CallbackInfoReturnable<Boolean> cir){
        World world = target.getWorld();
        if (world.isClient()) {
            return;
        }
        if (!(stack.getItem() instanceof HoeItem)) {
            return;
        }
        if (!UnboundConfig.unboundEnchantments) {
            return;
        }
        if (EnchantmentHelper.getLevel(UnboundEnchantments.SOUL_REAPER, stack) <= 0) {
            return;
        }
        if (!(attacker instanceof PlayerEntity attackingPlayer)) {
            return;
        }
        int xp;
        Random random = world.getRandom();
        if (target instanceof PlayerEntity playerTarget) {
            xp = MathHelper.nextBetween(random, 0, 12);
            if (xp > 10) {
                xp = MathHelper.nextBetween(random, xp, xp + 10);
            }
            if (xp > 20) {
                xp = MathHelper.nextBetween(random, xp, xp + 10);
            }
            playerTarget.addExperience(-1 * Math.min(playerTarget.totalExperience, xp));
            attackingPlayer.addExperience(xp);
        } else {
            xp = MathHelper.nextBetween(random, 0, 8);
            attackingPlayer.addExperience(xp);
        }
        if (random.nextBoolean()) {
            attackingPlayer.damage(ModDamageTypes.create(world, UnboundDamageTypes.FORCED_EXPERIENCE, target), Math.min(1, xp / 10f));
        }
        world.playSound(null, attackingPlayer.getBlockPos(), SoundEvents.ENTITY_EXPERIENCE_ORB_PICKUP, SoundCategory.PLAYERS, 1.0f, 1.0f);
    }
}
