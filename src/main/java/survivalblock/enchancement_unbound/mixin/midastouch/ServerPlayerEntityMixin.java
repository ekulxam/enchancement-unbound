package survivalblock.enchancement_unbound.mixin.midastouch;

import com.mojang.authlib.GameProfile;
import dev.onyxstudios.cca.api.v3.component.ComponentProvider;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Mixin(value = ServerPlayerEntity.class, priority = 2000)
public abstract class ServerPlayerEntityMixin extends PlayerEntity {

    public ServerPlayerEntityMixin(World world, BlockPos pos, float yaw, GameProfile gameProfile) {
        super(world, pos, yaw, gameProfile);
    }

    @Inject(method = "tick", at = @At(value = "HEAD"), cancellable = true)
    private void midasTouchCancelServerPlayerTick(CallbackInfo ci){
        if (UnboundEntityComponents.MIDAS_TOUCH.get(this).shouldPreventTicking()) {
            ci.cancel();
        }
    }
}
