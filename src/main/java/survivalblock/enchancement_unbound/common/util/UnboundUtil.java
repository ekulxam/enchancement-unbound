package survivalblock.enchancement_unbound.common.util;

import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;

public class UnboundUtil {

    public static ItemStack getFirstAegisStack(LivingEntity living, boolean shouldCooldown){
        for (ItemStack handStack : living.getHandItems()){
            if (!handStack.isOf(Items.SHIELD)) continue;
            if (!(EnchantmentHelper.getLevel(UnboundEnchantments.AEGIS, handStack) > 0)) continue;
            if (living instanceof PlayerEntity player && player.getItemCooldownManager().isCoolingDown(handStack.getItem())) continue;
            return handStack;
        }
        if (living instanceof PlayerEntity player){
            int size = player.getInventory().size();
            for (short slot = 0; slot < size; slot++){
                ItemStack stackInSlot = player.getInventory().getStack(slot);
                if (!stackInSlot.isOf(Items.SHIELD)) continue;
                if (!(EnchantmentHelper.getLevel(UnboundEnchantments.AEGIS, stackInSlot) > 0)) continue;
                Item item = stackInSlot.getItem();
                if (player.getItemCooldownManager().isCoolingDown(item)) continue;
                if (shouldCooldown) {
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
}
