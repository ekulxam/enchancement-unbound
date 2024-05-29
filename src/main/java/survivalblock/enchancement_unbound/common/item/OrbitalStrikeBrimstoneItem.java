package survivalblock.enchancement_unbound.common.item;

import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.client.item.TooltipType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundDataComponentTypes;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

import java.util.List;

public class OrbitalStrikeBrimstoneItem extends Item {
    private final String useKey = "TicksUsed";
    private final int MAX_USAGE_TICKS = 600;
    public OrbitalStrikeBrimstoneItem(Settings settings) {
        super(settings);
    }

    private static boolean shouldDoExplosion() {
        return !UnboundUtil.isBasicallyOriginal(UnboundConfig.orbitalBrimstoneExplosionPower, 0);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return shouldDoExplosion() ? 1 : 72000;
    }

    private static void setDefaultComponent(ItemStack stack) {
        if (!stack.contains(UnboundDataComponentTypes.USE_TICKS)) {
            stack.set(UnboundDataComponentTypes.USE_TICKS, 0);
        }
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        ItemStack stack = user.getStackInHand(hand);
        if(world.isClient()){
            return TypedActionResult.pass(stack);
        }
        if (shouldDoExplosion()) {
            strike(world, user, stack, true);
        }
        return TypedActionResult.success(stack);
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if(world.isClient()){
            return;
        }
        if(!(user instanceof PlayerEntity player)){
            return;
        }
        setDefaultComponent(stack);
        if (shouldDoExplosion()) {
            return;
        }
        strike(world, player, stack, false);
        super.usageTick(world, player, stack, remainingUseTicks);
    }

    private void strike(World world, PlayerEntity player, ItemStack stack, boolean explosion) {
        BlockHitResult blockHitResult = OrbitalStrikeBrimstoneItem.raycast(world, player);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return;
        }
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            Vec3d pos = blockHitResult.getPos();
            int useTicks = stack.getOrDefault(UnboundDataComponentTypes.USE_TICKS, 0);
            if(!(useTicks > MAX_USAGE_TICKS)){
                createOrbitalStrike(world, player, pos, player.getYaw());
                if (explosion) {
                    world.createExplosion(player, pos.x, pos.y, pos.z, UnboundConfig.orbitalBrimstoneExplosionPower, World.ExplosionSourceType.BLOCK);
                    player.getItemCooldownManager().set(this, (int) UnboundConfig.orbitalBrimstoneExplosionPower / 10);
                    player.stopUsingItem();
                }
                if (!player.isCreativeLevelTwoOp()) {
                    useTicks++;
                    stack.set(UnboundDataComponentTypes.USE_TICKS, useTicks);
                }
            } else {
                world.sendEntityStatus(player, (byte) (player.getMainHandStack() == stack ? 47 : 48));
                stack.decrement(1);
                player.incrementStat(Stats.BROKEN.getOrCreateStat(this));
            }
        }
    }

    protected void createOrbitalStrike(World world, LivingEntity user, Vec3d pos, float yaw){
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        BrimstoneEntity brimstone = new BrimstoneEntity(world, user);
        brimstone.setDamage(Integer.MAX_VALUE);
        brimstone.setPosition(x, y+256, z);
        brimstone.getDataTracker().set(BrimstoneEntity.FORCED_PITCH, (float) 90.0);
        brimstone.getDataTracker().set(BrimstoneEntity.FORCED_YAW, yaw);
        UnboundEntityComponents.BRIMSTONE_BYPASS.get(brimstone).setIgnoresDamageLimit(true);
        world.spawnEntity(brimstone);
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_6, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    protected static BlockHitResult raycast(World world, PlayerEntity player) {
        float multiplier = 256f * 16;
        float f = player.getPitch();
        float g = player.getYaw();
        Vec3d vec3d = player.getEyePos();
        float h = MathHelper.cos(-g * ((float)Math.PI / 180) - (float)Math.PI);
        float i = MathHelper.sin(-g * ((float)Math.PI / 180) - (float)Math.PI);
        float j = -MathHelper.cos(-f * ((float)Math.PI / 180));
        float k = MathHelper.sin(-f * ((float)Math.PI / 180));
        // multiplier += 5.0F;
        float l = i * j;
        float n = h * j;
        Vec3d vec3d2 = vec3d.add((double)l * multiplier, (double) k * multiplier, (double)n * multiplier);
        return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
    }

    @Override
    public boolean isItemBarVisible(ItemStack stack) {
        setDefaultComponent(stack);
        return stack.getOrDefault(UnboundDataComponentTypes.USE_TICKS, 0) > 0;
    }
    @Override
    public int getItemBarStep(ItemStack stack) {
        setDefaultComponent(stack);
        int useTicks = stack.getOrDefault(UnboundDataComponentTypes.USE_TICKS, 0);
        useTicks = Math.max(useTicks, 0);
        stack.set(UnboundDataComponentTypes.USE_TICKS, useTicks);
        return Math.round((float) (MAX_USAGE_TICKS - useTicks) * 13.0f / (float) MAX_USAGE_TICKS);
    }

    @Override
    public int getItemBarColor(ItemStack stack) {
        return MathHelper.hsvToRgb(0F, 1.0F, 1.0F);
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        user.swingHand(user.getActiveHand(), true);
    }

    @Override
    public void appendTooltip(ItemStack stack, TooltipContext context, List<Text> tooltip, TooltipType type) {
        tooltip.add(Text.translatable("item.enchancement_unbound.orbital_strike_brimstone.alien").formatted(Formatting.DARK_RED));
        super.appendTooltip(stack, context, tooltip, type);
    }
}
