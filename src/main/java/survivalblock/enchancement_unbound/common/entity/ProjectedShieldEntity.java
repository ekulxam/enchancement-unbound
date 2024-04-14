package survivalblock.enchancement_unbound.common.entity;

import moriyashiine.enchancement.common.init.ModDamageTypes;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.world.World;
import survivalblock.enchancement_unbound.common.component.ShieldStackComponent;
import survivalblock.enchancement_unbound.common.init.UnboundDamageTypes;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.init.UnboundEntityTypes;
import survivalblock.enchancement_unbound.common.init.UnboundSoundEvents;

import java.util.Objects;

public class ProjectedShieldEntity extends PersistentProjectileEntity {

    public ProjectedShieldEntity(EntityType<? extends ProjectedShieldEntity> entityType, World world) {
        super(entityType, world);
        this.getShieldStackComponent().setShieldStack(Items.SHIELD.getDefaultStack());
    }

    public ProjectedShieldEntity(World world, LivingEntity owner, ItemStack stack) {
        super(UnboundEntityTypes.PROJECTED_SHIELD, owner.getX(), (owner.getY() * 4 + owner.getEyeY()) / 5, owner.getZ(), world);
        this.getShieldStackComponent().setShieldStack(stack.copy());
        this.setNoGravity(true);
        this.setOwner(owner);
    }

    private ShieldStackComponent getShieldStackComponent(){
        return UnboundEntityComponents.SHIELD_STACK.get(this);
    }

    @Override
    public ItemStack asItemStack() {
        return this.getShieldStackComponent().getShieldStack().copy();
    }

    @Override
    public void tick() {
        boolean ownerDoesNotExist = this.getOwner() == null || this.getOwner().isRemoved() || !this.getOwner().isAlive();
        if (this.age > 120 || ownerDoesNotExist) {
            this.kill();
        }
        super.tick();
        if (this.isInsideWall()){
            this.kill();
        }
    }

    @Override
    protected void onEntityHit(EntityHitResult entityHitResult) {
        Entity entity = entityHitResult.getEntity();
        Entity entity2 = this.getOwner();
        if (Objects.equals(entity, entity2)) {
            return;
        }
        DamageSource damageSource = ModDamageTypes.create(getWorld(), UnboundDamageTypes.SLAM_IMPACT, this, entity2);
        if (entity.damage(damageSource, (float) this.getDamage())) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }
            if (entity instanceof LivingEntity livingEntity2) {
                if (entity2 instanceof LivingEntity) {
                    EnchantmentHelper.onUserDamaged(livingEntity2, entity2);
                    EnchantmentHelper.onTargetDamaged((LivingEntity)entity2, livingEntity2);
                }
                this.onHit(livingEntity2);
            }
        }
    }

    @Override
    protected void onBlockHit(BlockHitResult blockHitResult) {
        super.onBlockHit(blockHitResult);
        this.discard();
    }

    @Override
    protected SoundEvent getHitSound() {
        return UnboundSoundEvents.ENTITY_PROJECTED_SHIELD_HIT;
    }
}
