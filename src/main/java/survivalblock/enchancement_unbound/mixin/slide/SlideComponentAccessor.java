package survivalblock.enchancement_unbound.mixin.slide;

import com.llamalad7.mixinextras.sugar.Local;
import moriyashiine.enchancement.common.component.entity.SlideComponent;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Debug(export = true)
@Mixin(SlideComponent.class)
public interface SlideComponentAccessor {

    @Accessor("obj")
    PlayerEntity getPlayer();
}
