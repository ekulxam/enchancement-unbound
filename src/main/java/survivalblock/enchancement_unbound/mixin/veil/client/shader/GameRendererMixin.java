package survivalblock.enchancement_unbound.mixin.veil.client.shader;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import survivalblock.enchancement_unbound.client.util.UnboundClientUtil;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

@Mixin(GameRenderer.class)
public abstract class GameRendererMixin {

    @Shadow
    protected abstract void loadPostProcessor(Identifier id);

    @WrapOperation(method = "onCameraEntitySet", at = @At(value = "CONSTANT", args = "classValue=net/minecraft/entity/mob/EndermanEntity"))
    private boolean notSoSuperSecretSettingsAnymore(Object object, Operation<Boolean> original, Entity entity) {
        boolean isAnEnderman = original.call(object);
        if (!isAnEnderman) {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client == null) {
                return false;
            }
            ClientPlayerEntity thisPlayer = client.player;
            if (thisPlayer == null) {
                return false;
            }
            if (entity instanceof PlayerEntity player) {
                if (!UnboundEntityComponents.CURTAIN.get(player).isInCurtain()) {
                    return false;
                }
            } else if (!UnboundEntityComponents.CURTAIN.get(thisPlayer).isInCurtain() || thisPlayer.isSpectator()) {
                return false;
            }
            this.loadPostProcessor(UnboundClientUtil.getSuperSecretTikTokShaderThing());
            return false;
        }
        return true;
    }
}
