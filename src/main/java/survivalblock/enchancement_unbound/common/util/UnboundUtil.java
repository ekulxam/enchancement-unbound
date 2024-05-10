package survivalblock.enchancement_unbound.common.util;

import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.enchantment.UnboundHoeEnchantment;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.init.UnboundTags;

public class UnboundUtil {

    public static boolean shouldPreventAction(PlayerEntity player, boolean shouldCheckConfig){
        if (player.isSpectator()) {
            return false;
        }
        boolean hasVeil = EnchantmentHelper.getEquipmentLevel(ModEnchantments.VEIL, player) > 0;
        return shouldCheckConfig ? (UnboundConfig.astralVeil && hasVeil) : hasVeil;
    }

    public static ActionResult veilActionResult(PlayerEntity player){
        return shouldPreventAction(player, true) ? ActionResult.FAIL : ActionResult.PASS;
    }

    public static TypedActionResult<ItemStack> veilTypedActionResult(PlayerEntity player, ItemStack stack){
        return shouldPreventAction(player, true) ? TypedActionResult.fail(stack) : TypedActionResult.pass(stack);
    }

    public static boolean cancelHoeEnchantments(Enchantment original, Enchantment other){
        return !(other instanceof UnboundHoeEnchantment) || other == original;
    }

    public static boolean shouldRenderWithEndShader(Entity entity){
        return entity instanceof LivingEntity living && UnboundEntityComponents.CURTAIN.get(living).isInCurtain();
    }
    public static RenderLayer getEndShader(){
        return RenderLayer.getEndPortal();
    }

    public static boolean shouldActivateAscension(PlayerEntity player, BlockPos pos){
        World world = player.getWorld();
        BlockPos playerPos = player.getBlockPos();
        double yDiff = pos.getY() + player.getHeight() - playerPos.getY();
        double xDiffSquared = Math.abs(pos.getX() - playerPos.getX()) * Math.abs(pos.getX() - playerPos.getX());
        double zDiffSquared = Math.abs(pos.getZ() - playerPos.getZ()) * Math.abs(pos.getZ() - playerPos.getZ());
        double difference = Math.sqrt(xDiffSquared + (yDiff * yDiff) + zDiffSquared);
        for (int i = 1; i < difference; i++){
            if (world.getBlockState(playerPos.add(0, i, 0)).isSolid()) {
                return true;
            }
        }
        return false;
    }

    public static float execute(float value, LivingEntity attacker, Entity target, float cooldown) {
        if (!EnchancementUtil.hasEnchantment(UnboundEnchantments.EXECUTIONER, attacker)) {
            return value;
        }
        if (attacker instanceof PlayerEntity && cooldown <= 0.8f) {
            return value;
        }
        World world = attacker.getWorld();
        if (world.isClient()) return value;
        Random random = world.getRandom();
        if (random.nextFloat() < 0.95f) {
            return value;
        }
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION, target.getPos().x, target.getEyeY(), target.getPos().z, 4, 0.5, 0.5, 0.5, 1);
            serverWorld.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1.0f, 1.0f);
            serverWorld.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
        if (target.getType().isIn(UnboundTags.CANNOT_EXECUTE) || target instanceof PlayerEntity) {
            return value * 10;
        }
        return Float.MAX_VALUE;
    }
}
