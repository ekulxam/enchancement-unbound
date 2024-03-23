package survivalblock.enchancement_unbound.common.item;

import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.entity.projectile.AmethystShardEntity;
import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import moriyashiine.enchancement.common.entity.projectile.IceShardEntity;
import moriyashiine.enchancement.common.entity.projectile.TorchEntity;
import moriyashiine.enchancement.common.init.ModSoundEvents;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.CrossbowUser;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.projectile.*;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.registry.tag.ItemTags;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.TypedActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundItems;
import survivalblock.enchancement_unbound.mixin.vanillachanges.TridentEntityAccessor;

import java.util.function.Predicate;

public class ShardMinigunItem extends Item {
    public ShardMinigunItem(Settings settings) {
        super(settings);
    }

    @Override
    public int getMaxUseTime(ItemStack stack) {
        return 72000;
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
        Random random = world.getRandom();
        for(short shardNum = 0; shardNum < MathHelper.nextBetween(random,20, 32); shardNum++){
            PersistentProjectileEntity shardEntity;
            if(stack.isOf(UnboundItems.AMETHYST_SHARD_MINIGUN)){
                if(!player.isCreative()){
                    Predicate<ItemStack> predicate = amethystStack -> amethystStack.isOf(Items.AMETHYST_SHARD);
                    ItemStack projectileStack = null;
                    for (int i = 0; i < player.getInventory().size(); ++i) {
                        ItemStack aStackInInventory = player.getInventory().getStack(i);
                        if (predicate.test(aStackInInventory)) {
                            projectileStack = aStackInInventory;
                            break;
                        }
                    }
                    if(projectileStack == null){
                        super.usageTick(world, player, stack, remainingUseTicks);
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
                world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_HIT, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
                world.playSound(null, user.getBlockPos(), SoundEvents.BLOCK_AMETHYST_BLOCK_CHIME, SoundCategory.BLOCKS, 1.0F, 0.5F + world.random.nextFloat() * 1.2F);
            } else {
                super.usageTick(world, player, stack, remainingUseTicks);
                return;
            }
            Vec3d vec3d = player.getOppositeRotationVector(1.0F);
            Quaternionf quaternionf = (new Quaternionf()).setAngleAxis(0, vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = player.getRotationVec(1.0F);
            Vector3f vector3f = vec3d2.toVector3f().rotate(quaternionf);
            shardEntity.setVelocity(vector3f.x(), vector3f.y(), vector3f.z(), MathHelper.nextBetween(random,2F, 4F), MathHelper.nextBetween(random,3.15F, 4F));
            world.spawnEntity(shardEntity);
        }
        super.usageTick(world, player, stack, remainingUseTicks);
    }
}
