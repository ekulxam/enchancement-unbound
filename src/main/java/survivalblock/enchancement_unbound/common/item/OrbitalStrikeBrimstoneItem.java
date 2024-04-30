package survivalblock.enchancement_unbound.common.item;

import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.CreeperEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.sound.SoundCategory;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import survivalblock.enchancement_unbound.access.BrimstoneIgnoreDamageAccess;

import java.util.List;

public class OrbitalStrikeBrimstoneItem extends Item {
    private final String useKey = "TicksUsed";
    private final int MAX_USAGE_TICKS = 600;
    public OrbitalStrikeBrimstoneItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    protected void createOrbitalStrike(World world, LivingEntity user, Vec3d pos, float pitch, float yaw, double damage){
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        BrimstoneEntity brimstone = new BrimstoneEntity(world, user);
        brimstone.setDamage(damage);
        brimstone.setPosition(x, y+256, z);
        brimstone.getDataTracker().set(BrimstoneEntity.FORCED_PITCH, pitch);
        brimstone.getDataTracker().set(BrimstoneEntity.FORCED_YAW, yaw);
        ((BrimstoneIgnoreDamageAccess) brimstone).enchancement_unbound$setBrimstoneIgnoresDamage(true);
        world.spawnEntity(brimstone);
        world.playSound(null, pos.getX(), pos.getY(), pos.getZ(), ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_6, SoundCategory.PLAYERS, 1.0F, 1.0F);
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        ItemStack stack = user.getStackInHand(hand);
        if(world.isClient()){
            return TypedActionResult.pass(stack);
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
        BlockHitResult blockHitResult = OrbitalStrikeBrimstoneItem.raycast(world, player);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return;
        }
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            Vec3d pos = blockHitResult.getPos();
            int useTicks = stack.getOrCreateNbt().getInt(useKey);
            if(!(useTicks > MAX_USAGE_TICKS)){
                createOrbitalStrike(world, player, pos, 90.0F, player.getYaw(), Integer.MAX_VALUE);
                useTicks++;
                stack.getOrCreateNbt().putInt(useKey, useTicks);
            } else {
                if(player.getMainHandStack() == stack){
                    world.sendEntityStatus(user, (byte) 47);
                } else {
                    world.sendEntityStatus(user, (byte) 47);
                }
                stack.decrement(1);
                player.incrementStat(Stats.BROKEN.getOrCreateStat(this));
            }
        }
        super.usageTick(world, player, stack, remainingUseTicks);
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
        return true;
    }
    @Override
    public int getItemBarStep(ItemStack stack) {
        int useTicks = stack.getOrCreateNbt().getInt(useKey);
        useTicks = Math.max(useTicks, 0);
        stack.getOrCreateNbt().putInt(useKey, useTicks);
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
}
