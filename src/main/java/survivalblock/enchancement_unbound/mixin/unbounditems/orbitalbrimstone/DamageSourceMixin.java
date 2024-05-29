package survivalblock.enchancement_unbound.mixin.unbounditems.orbitalbrimstone;

import moriyashiine.enchancement.common.entity.projectile.BrimstoneEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.tag.DamageTypeTags;
import net.minecraft.registry.tag.TagKey;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import survivalblock.enchancement_unbound.common.component.BrimstoneBypassComponent;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Mixin(DamageSource.class)
public abstract class DamageSourceMixin {

    @Shadow @Nullable public abstract Entity getSource();

    @Inject(method = "isIn", at = @At("HEAD"), cancellable = true)
    private void brimstoneBypassCooldownSometimes(TagKey<DamageType> tag, CallbackInfoReturnable<Boolean> cir) {
        if (!(this.getSource() instanceof BrimstoneEntity brimstone)) {
            return;
        }
        BrimstoneBypassComponent brimstoneBypassComponent = UnboundEntityComponents.BRIMSTONE_BYPASS.get(brimstone);
        if (brimstoneBypassComponent.getIgnoresDamageLimit() && shouldBeCountedAsInThisTag(tag)) {
            cir.setReturnValue(true);
        }
    }

    @Unique
    private boolean shouldBeCountedAsInThisTag(TagKey<DamageType> tag) {
        if (tag.equals(DamageTypeTags.BYPASSES_COOLDOWN)) {
            return true;
        }
        if (tag.equals(DamageTypeTags.BYPASSES_RESISTANCE)) {
            return true;
        }
        if (tag.equals(DamageTypeTags.BYPASSES_ARMOR)) {
            return true;
        }
        if (tag.equals(DamageTypeTags.BYPASSES_SHIELD)) {
            return true;
        }
        if (tag.equals(DamageTypeTags.BYPASSES_ENCHANTMENTS)) {
            return true;
        }
        return tag.equals(DamageTypeTags.BYPASSES_EFFECTS);
    }
}
