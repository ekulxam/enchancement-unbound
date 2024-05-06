package survivalblock.enchancement_unbound.common.item;

import moriyashiine.enchancement.common.entity.projectile.AmethystShardEntity;
import moriyashiine.enchancement.common.entity.projectile.IceShardEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.item.RangedWeaponItem;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.UseAction;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundItems;
import survivalblock.enchancement_unbound.common.init.UnboundSoundEvents;

import java.util.function.Predicate;

public class ShardMinigunItem extends RangedWeaponItem {
    private final Item shardItem;
    public ShardMinigunItem(Settings settings, Item shardItem) {
        super(settings);
        this.shardItem = shardItem;
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
    }

    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        user.setCurrentHand(hand);
        ItemStack stack = user.getStackInHand(hand);
        return world.isClient() ? TypedActionResult.pass(stack) : TypedActionResult.success(stack);
    }

    public boolean isAmmunition(ItemStack stack){
        return stack.isOf(shardItem) || (stack.getItem() == UnboundItems.ICE_SHARD_MINIGUN && stack.isOf(Items.PACKED_ICE));
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if(world.isClient()){
            return;
        }
        if(!(user instanceof PlayerEntity player)){
            return;
        }
        Random random = world.getRandom();
        for(short shardNum = 0; shardNum < MathHelper.nextBetween(random,20, 32) * UnboundConfig.scatterProjectileMultiplier; shardNum++){
            PersistentProjectileEntity shardEntity;
            if(this.shardItem == Items.AMETHYST_SHARD){
                if(!player.isCreative()){
                    ItemStack projectileStack = null;
                    for (int i = 0; i < player.getInventory().size(); ++i) {
                        ItemStack aStackInInventory = player.getInventory().getStack(i);
                        if (isAmmunition(aStackInInventory)) {
                            projectileStack = aStackInInventory;
                            break;
                        }
                    }
                    if(projectileStack == null){
                        user.stopUsingItem();
                        player.getItemCooldownManager().set(this, MathHelper.clamp(this.getMaxUseTime(stack) - remainingUseTicks, 10, 300));
                        return;
                    }
                    if (MathHelper.nextBetween(random,0, 12) == 0){
                        projectileStack.decrement(1);
                        if (projectileStack.isEmpty()) {
                            player.getInventory().removeOne(projectileStack);
                        }
                    }
                }
                shardEntity = new AmethystShardEntity(world, player);
            } else if(this.shardItem == Items.BLUE_ICE){
                if(!player.isCreative()){
                    ItemStack projectileStack = null;
                    for (int i = 0; i < player.getInventory().size(); ++i) {
                        ItemStack aStackInInventory = player.getInventory().getStack(i);
                        if (isAmmunition(aStackInInventory)) {
                            projectileStack = aStackInInventory;
                            break;
                        }
                    }
                    if(projectileStack == null){
                        user.stopUsingItem();
                        player.getItemCooldownManager().set(this, MathHelper.clamp(this.getMaxUseTime(stack) - remainingUseTicks, 10, 300));
                        return;
                    }
                    if (MathHelper.nextBetween(random,0, 12) == 0){
                        projectileStack.decrement(1);
                        if (projectileStack.isEmpty()) {
                            player.getInventory().removeOne(projectileStack);
                        }
                    }
                }
                shardEntity = new IceShardEntity(world, player);
            } else {
                return;
            }
            world.playSound(null, user.getBlockPos(), UnboundSoundEvents.ITEM_SHARD_USE, SoundCategory.PLAYERS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
            world.playSound(null, user.getBlockPos(), UnboundSoundEvents.ITEM_SHARD_CHIME, SoundCategory.PLAYERS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
            Vec3d vec3d = player.getOppositeRotationVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0, vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = player.getRotationVec(1.0F);
            Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
            shardEntity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), MathHelper.nextBetween(random,2F, 4F), MathHelper.nextBetween(random,3.15F, 4F));
            shardEntity.setPosition(player.getEyePos().add(player.getRotationVector().multiply(0.3)));
            world.spawnEntity(shardEntity);
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        if (world.isClient()) user.swingHand(user.getActiveHand(), true);
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return null;
    }

    @Override
    public int getRange() {
        return 15;
    }
}
