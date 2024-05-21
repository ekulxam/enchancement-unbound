package survivalblock.enchancement_unbound.mixin.veil.client.screen;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.ingame.AbstractInventoryScreen;
import net.minecraft.client.network.ClientPlayerEntity;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.client.util.UnboundClientUtil;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Debug(export = true)
@Mixin(AbstractInventoryScreen.class)
public class AbstractInventoryScreenMixin {

    @Inject(method = "render", at = @At("RETURN"))
    private void drawNotABossbar(DrawContext context, int mouseX, int mouseY, float delta, CallbackInfo ci) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        CurtainComponent curtainComponent = UnboundEntityComponents.CURTAIN.get(player);
        float percent = (float) curtainComponent.getTicksInAstralPlane() / CurtainComponent.MAX_INSANITY;
        if (percent <= 0) {
            return;
        }
        UnboundClientUtil.BossBar.renderBossBar(context, percent);
    }
}
