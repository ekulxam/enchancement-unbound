package survivalblock.enchancement_unbound.mixin.frostbite;

import moriyashiine.enchancement.common.ModConfig;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FrostedIceBlock;
import net.minecraft.block.ShapeContext;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.enchantment.FrostWalkerEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.TridentEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.access.TridentWalkerAccess;
import survivalblock.enchancement_unbound.common.UnboundConfig;

@Mixin(TridentEntity.class)
public abstract class TridentEntityMixin extends PersistentProjectileEntity {
    @Shadow protected abstract ItemStack asItemStack();
    @Shadow private boolean dealtDamage;

    @Shadow @Final private static TrackedData<Byte> LOYALTY;

    protected TridentEntityMixin(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
    }

    @SuppressWarnings("UnreachableCode")
    @Inject(method = "tick", at = @At("HEAD"))
    private void frostbiteAtlan(CallbackInfo ci){
        World world = this.getWorld();
        if (world.isClient()) {
            return;
        }
        if (!UnboundConfig.frostbiteTridentFreezesWater) {
            return;
        }
        ItemStack stack = this.asItemStack();
        if (this.dealtDamage && (this.dataTracker.get(LOYALTY) > 0 || ModConfig.allTridentsHaveLoyalty)) {
            return;
        }
        LivingEntity living;
        BlockPos blockPos = this.getBlockPos();
        if (this.getOwner() instanceof LivingEntity) {
            living = (LivingEntity) this.getOwner();
        } else {
            living = this.getWorld().getClosestEntity(LivingEntity.class, TargetPredicate.DEFAULT, null, this.getPos().x, this.getPos().y, this.getPos().z, new Box(blockPos).expand(100));
        }
        if (living == null) {
            return;
        }
        ((TridentWalkerAccess) Enchantments.FROST_WALKER).enchancement_unbound$setEntityCheckBypass(true);
        ((TridentWalkerAccess) Enchantments.FROST_WALKER).enchancement_unbound$setTridentEntityWalker((TridentEntity) (Object) this);
        if (this.inGround && !this.isSubmergedInWater()) {
            ((TridentWalkerAccess) Enchantments.FROST_WALKER).enchancement_unbound$setFrostYOffset(-1);
            FrostWalkerEnchantment.freezeWater(living, world, blockPos, EnchantmentHelper.getLevel(ModEnchantments.FROSTBITE, stack));
        }
        if (!this.dealtDamage) {
            ((TridentWalkerAccess) Enchantments.FROST_WALKER).enchancement_unbound$setFrostYOffset(0);
            FrostWalkerEnchantment.freezeWater(living, world, blockPos, EnchantmentHelper.getLevel(ModEnchantments.FROSTBITE, stack));
        }
        ((TridentWalkerAccess) Enchantments.FROST_WALKER).enchancement_unbound$setEntityCheckBypass(false);
        ((TridentWalkerAccess) Enchantments.FROST_WALKER).enchancement_unbound$setTridentEntityWalker(null);
    }
}
