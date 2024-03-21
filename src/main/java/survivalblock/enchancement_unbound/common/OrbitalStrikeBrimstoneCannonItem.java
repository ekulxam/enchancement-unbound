package survivalblock.enchancement_unbound.common;

import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemUsageContext;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;

public class OrbitalStrikeBrimstoneCannonItem extends Item {
    public OrbitalStrikeBrimstoneCannonItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    public void createOrbitalStrike(World world, LivingEntity user, Vec3d pos, float pitch, float yaw, double damage){
        double x = pos.getX();
        double y = pos.getY();
        double z = pos.getZ();
        BrimstoneEntity brimstone = new BrimstoneEntity(world, user);
        brimstone.setDamage(damage);
        brimstone.setPosition(x, y+256, z);
        brimstone.getDataTracker().set(BrimstoneEntity.FORCED_PITCH, pitch);
        brimstone.getDataTracker().set(BrimstoneEntity.FORCED_YAW, yaw);
        world.spawnEntity(brimstone);
        world.playSound(null, user.getX(), user.getY(), user.getZ(), ModSoundEvents.ITEM_CROSSBOW_BRIMSTONE_6, SoundCategory.PLAYERS, 1.0F, 1.0F);
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
        BlockHitResult blockHitResult = OrbitalStrikeBrimstoneCannonItem.raycast(world, player, 512F);
        if (blockHitResult.getType() == HitResult.Type.MISS) {
            return;
        }
        if (blockHitResult.getType() == HitResult.Type.BLOCK) {
            Vec3d pos = blockHitResult.getPos();
            createOrbitalStrike(world, player, pos, 90.0F, player.getYaw(), 100000);
        }
        super.usageTick(world, player, stack, remainingUseTicks);
    }

    protected static BlockHitResult raycast(World world, PlayerEntity player, float multiplier) {
        float f = player.getPitch();
        float g = player.getYaw();
        Vec3d vec3d = player.getEyePos();
        float h = MathHelper.cos(-g * ((float)Math.PI / 180) - (float)Math.PI);
        float i = MathHelper.sin(-g * ((float)Math.PI / 180) - (float)Math.PI);
        float j = -MathHelper.cos(-f * ((float)Math.PI / 180));
        float k = MathHelper.sin(-f * ((float)Math.PI / 180));
        multiplier += 5.0F;
        float l = i * j;
        float m = k;
        float n = h * j;
        Vec3d vec3d2 = vec3d.add((double)l * multiplier, (double)m * multiplier, (double)n * multiplier);
        return world.raycast(new RaycastContext(vec3d, vec3d2, RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE, player));
    }
}
