package survivalblock.enchancement_unbound.common.util;

import moriyashiine.enchancement.common.component.entity.ExtendedWaterComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.api.tag.convention.v1.ConventionalEntityTypeTags;
import net.minecraft.enchantment.*;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.passive.HorseEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.AnimalArmorItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.registry.entry.RegistryEntryList;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.init.UnboundTags;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class UnboundUtil {


    private static final Map<TagKey<Item>, Boolean> TAG_TO_HORSESHOES = new HashMap<>();
    public static final double CONFIG_FLOATING_POINT_PRECISION = 0.001;

    public static boolean isBasicallyOriginal(double value, double original) {
        return Math.abs(value - original) <= CONFIG_FLOATING_POINT_PRECISION;
    }

    public static boolean isBasicallyOriginal(float value, float original) {
        return Math.abs(value - original) <= CONFIG_FLOATING_POINT_PRECISION;
    }

    public static boolean shouldPreventAction(PlayerEntity player){
        if (player.isSpectator()) {
            return false;
        }
        return UnboundEntityComponents.MIDAS_TOUCH.get(player).shouldPreventTicking() || UnboundEntityComponents.CURTAIN.get(player).isInCurtain();
    }

    public static ActionResult veilHitEntityResult(PlayerEntity player, Entity entity) {
        if (player.isSpectator()) {
            return ActionResult.PASS;
        }
        if (UnboundEntityComponents.MIDAS_TOUCH.get(player).shouldPreventTicking()) {
            return ActionResult.FAIL;
        }
        if (UnboundEntityComponents.CURTAIN.get(player).isInCurtain() && !entity.getType().isIn(UnboundTags.EntityTypes.SHOULD_HIT_IN_VEIL)) {
            return ActionResult.FAIL;
        }
        return ActionResult.PASS;
    }

    public static ActionResult veilActionResult(PlayerEntity player){
        return shouldPreventAction(player) ? ActionResult.FAIL : ActionResult.PASS;
    }

    public static TypedActionResult<ItemStack> veilTypedActionResult(PlayerEntity player, ItemStack stack){
        return shouldPreventAction(player) ? TypedActionResult.fail(stack) : TypedActionResult.pass(stack);
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
        if (!UnboundConfig.unboundEnchantments) {
            return value;
        }
        if (!EnchancementUtil.hasEnchantment(UnboundEnchantments.EXECUTIONER, attacker)) {
            return value;
        }
        World world = attacker.getWorld();
        if (world.isClient()) return value;
        if (attacker instanceof PlayerEntity && cooldown <= 0.8f) {
            return value;
        }
        if (target instanceof LivingEntity living) {
            if (living.getHealth() > living.getMaxHealth() * 0.4) {
                return value;
            }
        }
        Random random = world.getRandom();
        double chance = target instanceof LivingEntity living ? executionChanceCalculation(living.getHealth() / living.getMaxHealth()) : 0.1f;
        if (random.nextFloat() < 1f - chance) {
            return value;
        }
        if (world instanceof ServerWorld serverWorld) {
            serverWorld.spawnParticles(ParticleTypes.EXPLOSION, target.getPos().x, target.getEyeY(), target.getPos().z, 4, 0.5, 0.5, 0.5, 1);
            serverWorld.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_STRONG, SoundCategory.PLAYERS, 1.0f, 1.0f);
            serverWorld.playSound(null, target.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.PLAYERS, 1.0f, 1.0f);
        }
        if (cannotExecute(target, true)) {
            return value * 10;
        }
        return Float.MAX_VALUE;
    }

    private static double executionChanceCalculation(double healthPercentage){
        if (healthPercentage <= 0) {
            return 0.5;
        }
        double calculation = Math.log10(healthPercentage);
        calculation /= 5;
        calculation *= -1;
        calculation -= 0.0546;
        // y = -(log10(x) / 5) - 0.0546
        // desmos: y=-\frac{\log\left(x\right)}{5}-0.0546
        return Math.min(Math.max(calculation, 0), 0.5); // I didn't want to do a clamp because I've seen those mods that make
        // MathHelper methods really weird, so call me crazy but I'm just taking a precaution
    }

    public static void applyExtendedWater(LivingEntity living){
        ExtendedWaterComponent extendedWaterComponent = ModEntityComponents.EXTENDED_WATER.get(living);
        extendedWaterComponent.markWet();
        ModEntityComponents.EXTENDED_WATER.get(living).sync();
    }

    public static boolean cannotExecute(Entity target, boolean shouldCheckPlayer) {
        if (shouldCheckPlayer && target instanceof PlayerEntity) {
            return true;
        }
        return target.getType().isIn(UnboundTags.EntityTypes.CANNOT_EXECUTE) || target.getType().isIn(ConventionalEntityTypeTags.BOSSES);
    }

    public static int getHorseshoeEnchantment(Enchantment enchantment, HorseEntity horse) {
        if (horse.hasStackEquipped(EquipmentSlot.FEET)) {
            ItemStack footStack = horse.getEquippedStack(EquipmentSlot.FEET);
            if (footStack != null && !footStack.equals(ItemStack.EMPTY)) {
                int i = EnchantmentHelper.getLevel(enchantment, footStack);
                return Math.max(i, EnchantmentHelper.getLevel(enchantment, horse.getEquippedStack(EquipmentSlot.CHEST)));
            }
        }
        return EnchantmentHelper.getLevel(enchantment, horse.getEquippedStack(EquipmentSlot.CHEST));
    }

    public static boolean isValidHorseshoeEnchantment(Enchantment enchantment) {
        if (!UnboundConfig.horseshoes) {
            return false;
        }
        if (!isPartOfFootArmorEnchantable(enchantment.getApplicableItems())) {
            return false;
        }
        return enchantment instanceof SoulSpeedEnchantment || enchantment instanceof FrostWalkerEnchantment || enchantment instanceof DepthStriderEnchantment || enchantment instanceof ProtectionEnchantment;
    }

    private static boolean isPartOfFootArmorEnchantable(TagKey<Item> tag) {
        return TAG_TO_HORSESHOES.computeIfAbsent(tag, bl -> {
            Optional<RegistryEntryList.Named<Item>> opt = Registries.ITEM.getEntryList(ItemTags.FOOT_ARMOR_ENCHANTABLE);
            if (opt.isEmpty()) {
                return false;
            }
            RegistryEntryList.Named<Item> named = opt.get();
            for (RegistryEntry<Item> registryEntry : named) {
                if (registryEntry.isIn(ItemTags.FOOT_ARMOR_ENCHANTABLE)) {
                    return false;
                }
            }
            return true;
        });
    }

    public static boolean isHorseArmor(Item item) {
        return item instanceof AnimalArmorItem animalArmorItem && animalArmorItem.getType().equals(AnimalArmorItem.Type.EQUESTRIAN);
    }

    public static boolean galeAirbending() {
        return UnboundConfig.infiniteGale && UnboundConfig.airbending;
    }

    public static boolean strafeAirbending() {
        return UnboundConfig.infiniteStrafe && UnboundConfig.airbending;
    }

    public static boolean dashAirbending() {
        return UnboundConfig.infiniteDash && UnboundConfig.airbending;
    }
}
