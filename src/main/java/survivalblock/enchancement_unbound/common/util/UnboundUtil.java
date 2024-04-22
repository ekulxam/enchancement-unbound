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
import org.spongepowered.asm.mixin.Unique;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.enchantment.UnboundShieldEnchantment;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

public class UnboundUtil {

    public static ItemStack getFirstAegisStack(LivingEntity living, boolean shouldCooldown){
        for (ItemStack handStack : living.getHandItems()){
            if (!isAShield(handStack)) continue;
            if (!(EnchantmentHelper.getLevel(UnboundEnchantments.AEGIS, handStack) > 0)) continue;
            if (living instanceof PlayerEntity player && player.getItemCooldownManager().isCoolingDown(handStack.getItem())) continue;
            return handStack;
        }
        if (living instanceof PlayerEntity player){
            int size = player.getInventory().size();
            for (short slot = 0; slot < size; slot++){
                ItemStack stackInSlot = player.getInventory().getStack(slot);
                if (!isAShield(stackInSlot)) continue;
                if (!(EnchantmentHelper.getLevel(UnboundEnchantments.AEGIS, stackInSlot) > 0)) continue;
                Item item = stackInSlot.getItem();
                if (player.getItemCooldownManager().isCoolingDown(item)) continue;
                if (shouldCooldown && UnboundConfig.aegisUseDisables) {
                    player.getItemCooldownManager().set(item, 100);
                }
                return stackInSlot;
            }
        }
        return ItemStack.EMPTY;
    }
    public static boolean hasAegis(LivingEntity living){
        return getFirstAegisStack(living, false) != ItemStack.EMPTY;
    }

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

    public static boolean isAShield(ItemStack stack){
        return stack.isIn(ConventionalItemTags.SHIELDS) || stack.isOf(Items.SHIELD);
    }

    public static boolean cancelShieldEnchantments(Enchantment original, Enchantment other){
        return !(other instanceof UnboundShieldEnchantment) || other == original;
    }
    public static boolean cancelHoeEnchantments(Enchantment original, Enchantment other){
        return !(other instanceof UnboundShieldEnchantment) || other == original;
    }

    public static boolean shouldRenderWithEndShader(Entity entity){
        return entity instanceof LivingEntity living && UnboundEntityComponents.CURTAIN.get(living).isInCurtain();
    }
    public static RenderLayer getEndShader(){
        return RenderLayer.getEndPortal();
    }
}
