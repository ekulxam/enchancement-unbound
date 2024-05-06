package survivalblock.enchancement_unbound.common.item;

import moriyashiine.enchancement.common.component.entity.ChaosArrowComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.mixin.util.PersistentProjectileEntityAccessor;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.ArrowEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.*;
import net.minecraft.potion.PotionUtil;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.enchancement_unbound.access.PhaserChaosAccess;

import java.util.List;
import java.util.function.Predicate;

public class UnboundSlingshotItem extends RangedWeaponItem {
    public UnboundSlingshotItem(Settings settings) {
        super(settings);
    }
    @Override
    public Predicate<ItemStack> getProjectiles() {
        return BOW_PROJECTILES;
    }

    @Override
    public int getRange() {
        return BowItem.RANGE;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return Items.BOW.getMaxUseTime(Items.BOW.getDefaultStack());
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        ItemStack stack = user.getStackInHand(hand);
        return TypedActionResult.pass(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!(user instanceof PlayerEntity player)) {
            return;
        }
        if (world.isClient()) {
            world.playSound(null, player.getX(), player.getY(), player.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + 1.5f);
            return;
        }
        ItemStack itemStack = player.getProjectileType(stack);
        if (itemStack.isEmpty()) {
            itemStack = new ItemStack(Items.ARROW);
        }
        ArrowItem arrowItem = (ArrowItem)(itemStack.getItem() instanceof ArrowItem ? itemStack.getItem() : Items.ARROW);
        PersistentProjectileEntity persistentProjectileEntity = arrowItem.createArrow(world, itemStack, player);
        float speed = 3.0f;
        float divergence = 1.0f;
        persistentProjectileEntity.setVelocity(player, player.getPitch(), player.getYaw(), 0.0f, speed, divergence);
        if (EnchantmentHelper.getLevel(ModEnchantments.PHASING, stack) > 0) {
            // phasing
            ModEntityComponents.PHASING.maybeGet(persistentProjectileEntity).ifPresent((phasingComponent) -> {
                phasingComponent.setPhasingLevel(2);
                phasingComponent.sync();
            });
            persistentProjectileEntity.setNoGravity(true);
        }
        if (EnchantmentHelper.getLevel(ModEnchantments.DELAY, stack) > 0) {
            // delay
            ModEntityComponents.DELAY.maybeGet(persistentProjectileEntity).ifPresent((delayComponent) -> {
                delayComponent.setDelayLevel(2);
                delayComponent.setStackShotFrom(player.getActiveItem());
                delayComponent.setCachedSpeed(speed);
                delayComponent.setCachedDivergence(divergence);
                delayComponent.sync();
            });
            persistentProjectileEntity.setCritical(true);
        }
        if (EnchantmentHelper.getLevel(ModEnchantments.CHAOS, stack) > 0 && persistentProjectileEntity instanceof ArrowEntity arrow) {
            // chaos
            ModEntityComponents.CHAOS_ARROW.maybeGet(arrow).ifPresent((chaosArrowComponent -> {
                ((PhaserChaosAccess) chaosArrowComponent).enchancement_unbound$setLevelCheckBypass(true);
            }));
            ChaosArrowComponent.applyChaos(player, stack, (statusEffects) -> {
                ModEntityComponents.CHAOS_ARROW.get(arrow).setOriginalStack(((PersistentProjectileEntityAccessor)arrow).enchancement$asItemStack());
                arrow.initFromStack(PotionUtil.setCustomPotionEffects(new ItemStack(Items.TIPPED_ARROW), statusEffects));
            });
        }
        persistentProjectileEntity.pickupType = PersistentProjectileEntity.PickupPermission.CREATIVE_ONLY;
        int power = EnchantmentHelper.getLevel(Enchantments.POWER, stack);
        if (power > 0) persistentProjectileEntity.setDamage(persistentProjectileEntity.getDamage() + (double)power * 0.5 + 0.5);
        int pierce = EnchantmentHelper.getLevel(Enchantments.PIERCING, stack);
        if (pierce > 0) persistentProjectileEntity.setPierceLevel((byte) pierce);
        int punch = EnchantmentHelper.getLevel(Enchantments.PUNCH, stack);
        if (punch > 0) persistentProjectileEntity.setPunch(punch);
        int flame = EnchantmentHelper.getLevel(Enchantments.FLAME, stack);
        if (flame > 0) persistentProjectileEntity.setOnFireFor(100);
        persistentProjectileEntity.setPosition(player.getEyePos().add(player.getRotationVector().multiply(0.7)));
        world.spawnEntity(persistentProjectileEntity);
        stack.damage(1, player, p -> p.sendToolBreakStatus(player.getActiveHand()));
        player.incrementStat(Stats.USED.getOrCreateStat(this));
    }

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return Items.BOW.getUseAction(Items.BOW.getDefaultStack());
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (world.isClient()) user.swingHand(user.getActiveHand(), true);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0f, 1.0f / (world.getRandom().nextFloat() * 0.4f + 1.2f) + 0.5f);
    }

    @Override
    public int getEnchantability() {
        return Items.BOW.getEnchantability();
    }
    @Override
    public boolean isEnchantable(ItemStack stack) {
        return true;
    }

    @Override
    public void appendTooltip(ItemStack stack, @Nullable World world, List<Text> tooltip, TooltipContext context) {
        if (FabricLoader.getInstance().isModLoaded("aylyth") || Screen.hasShiftDown()) {
            tooltip.add(Text.translatable("item.enchancement_unbound.slingshot.intro").formatted(Formatting.GRAY)
                    .append(Text.translatable("item.enchancement_unbound.slingshot.aylythian_origins").formatted(Formatting.GOLD)));
        }
        super.appendTooltip(stack, world, tooltip, context);
    }
}
