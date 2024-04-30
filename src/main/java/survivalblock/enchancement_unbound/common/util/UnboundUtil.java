package survivalblock.enchancement_unbound.common.util;

import moriyashiine.enchancement.common.init.ModEnchantments;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalItemTags;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.enchantment.UnboundHoeEnchantment;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

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
}
