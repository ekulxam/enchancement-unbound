package survivalblock.enchancement_unbound.mixin.sentientpants;

import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.client.payload.PantsOfUndyingPayload;
import survivalblock.enchancement_unbound.common.entity.SentientPantsEntity;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public abstract ItemStack getEquippedStack(EquipmentSlot var1);

    @Shadow public abstract void equipStack(EquipmentSlot var1, ItemStack var2);

    @Shadow public abstract boolean addStatusEffect(StatusEffectInstance effect);

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "applyArmorToDamage", at = @At("RETURN"))
    private void spawnPants(DamageSource source, float amount, CallbackInfoReturnable<Float> cir) {
        World world = this.getWorld();
        if (world.isClient()) {
            return;
        }
        ItemStack stack = this.getEquippedStack(EquipmentSlot.LEGS);
        if (stack == null || stack.isEmpty()) {
            return;
        }
        if (!EnchancementUtil.hasEnchantment(UnboundEnchantments.SENTIENT_PANTS, stack)) {
            return;
        }
        LivingEntity living = (LivingEntity) (Object) this;
        if (living instanceof SentientPantsEntity) {
            return;
        }
        this.equipStack(EquipmentSlot.LEGS, ItemStack.EMPTY);
        world.spawnEntity(new SentientPantsEntity(world, living, stack));
        if (living instanceof ServerPlayerEntity player) {
            PantsOfUndyingPayload.send(stack, player);
        }
    }
}
