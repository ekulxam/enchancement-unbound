package survivalblock.enchancement_unbound.mixin.frostbite;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
    @Shadow protected abstract ItemStack asItemStack();
    @Shadow private boolean dealtDamage;

    @Shadow @Final private static TrackedData<Byte> LOYALTY;

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void frostbiteAtlan(CallbackInfo ci){
        if (this.getWorld().isClient()) {
            return;
        }
        if (!UnboundConfig.frostbiteTridentFreezesWater) {
            return;
        }
        ItemStack stack = this.asItemStack();
        if (this.dealtDamage && (this.dataTracker.get(LOYALTY) > 0 || ModConfig.allTridentsHaveLoyalty)) {
            return;
        }
        if (!this.getWorld().isClient()) {
            if (this.inGround && !this.isSubmergedInWater()) {
                freezeWater(this, this.getWorld(), this.getBlockPos(), EnchantmentHelper.getLevel(ModEnchantments.FROSTBITE, stack), -1);
            }
            if (!this.dealtDamage) {
                freezeWater(this, this.getWorld(), this.getBlockPos(), EnchantmentHelper.getLevel(ModEnchantments.FROSTBITE, stack), 0);
            }
        }
    }
    @Unique
    private static void freezeWater(Entity entity, final World world, BlockPos blockPos, int level, int divergence) {
        BlockState blockState = Blocks.FROSTED_ICE.getDefaultState();
        int i = Math.min(16, 2 + level);
        BlockPos.Mutable mutable = new BlockPos.Mutable();
        for (BlockPos blockPos2 : BlockPos.iterate(blockPos.add(-i, divergence, -i), blockPos.add(i, divergence, i))) {
            if (!blockPos2.isWithinDistance(entity.getPos(), i)) continue;
            mutable.set(blockPos2.getX(), blockPos2.getY() + 1, blockPos2.getZ());
            BlockState blockState2 = world.getBlockState(mutable);
            if (!blockState2.isAir() || world.getBlockState(blockPos2) != FrostedIceBlock.getMeltedState() || !blockState.canPlaceAt(world, blockPos2) || !world.canPlace(blockState, blockPos2, ShapeContext.absent())) continue;
            world.setBlockState(blockPos2, blockState);
            world.scheduleBlockTick(blockPos2, Blocks.FROSTED_ICE, MathHelper.nextInt(entity.getWorld().getRandom(), 60, 120));
        }
    }
}
