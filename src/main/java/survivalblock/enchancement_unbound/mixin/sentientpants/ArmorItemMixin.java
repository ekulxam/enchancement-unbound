package survivalblock.enchancement_unbound.mixin.sentientpants;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ArmorItem;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.client.packet.PantsOfUndyingPacket;
import survivalblock.enchancement_unbound.common.entity.SentientPantsEntity;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;

@Mixin(ArmorItem.class)
public class ArmorItemMixin {

    @Inject(method = "use", at = @At("HEAD"), cancellable = true)
    private void spawnPantsOnUse(World world, PlayerEntity user, Hand hand, CallbackInfoReturnable<TypedActionResult<ItemStack>> cir) {
        if (!user.isSneaking()) {
            return;
        }
        ItemStack stack = user.getStackInHand(hand);
        if (!EnchancementUtil.hasEnchantment(UnboundEnchantments.SENTIENT_PANTS, stack)) {
            return;
        }
        if (!world.isClient()) {
            world.spawnEntity(new SentientPantsEntity(world, user, stack));
            if (user instanceof ServerPlayerEntity player) {
                PantsOfUndyingPacket.send(stack, player);
            }
            stack.decrement(1);
        }
        cir.setReturnValue(TypedActionResult.success(stack, world.isClient()));
    }
}
