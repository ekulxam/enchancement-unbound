package survivalblock.enchancement_unbound.mixin.slide;

import moriyashiine.enchancement.common.component.entity.SlideComponent;
import net.minecraft.entity.player.PlayerEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(SlideComponent.class)
public interface SlideComponentAccessor {

    @Accessor("obj")
    PlayerEntity enchancement_unbound$getPlayer();
}
