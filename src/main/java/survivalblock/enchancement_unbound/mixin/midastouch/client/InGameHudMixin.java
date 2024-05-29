package survivalblock.enchancement_unbound.mixin.midastouch.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.option.GameOptions;
import net.minecraft.client.option.Perspective;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.component.MidasTouchComponent;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    // Step 1: Make a new CANVA project and put in some chains
    // Step 2: Export as png or jpg and put into Pinetools - Resize image, select pixel mode, uncheck constant proportions and set width and height to the same arbitary power of two
    // Step 3: Export that and put it in remove.bg to take away background
    // Step 4: Go to Pinetools - change brightness and set to 100
    // Step 5: Go to Pinetools - colorize with R255, G235, B80 and strongness 60 or 75
    // TADA
    @Unique
    private static final Identifier GOLDEN_CHAINS_OVERLAY = EnchancementUnbound.id("textures/misc/golden_chains_overlay.png");

    @Shadow @Final private MinecraftClient client;

    @Shadow protected abstract void renderOverlay(DrawContext context, Identifier texture, float opacity);

    @Inject(method = "renderMiscOverlays", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/MinecraftClient;getLastFrameDuration()F"))
    private void renderGoldenChains(DrawContext context, float tickDelta, CallbackInfo ci){
        if (this.client == null) {
            return;
        }
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        if (clientPlayerEntity == null) {
            return;
        }
        GameOptions options = this.client.options;
        if (options == null) {
            return;
        }
        Perspective perspective = options.getPerspective();
        if (perspective == null || !perspective.isFirstPerson()) {
            return;
        }
        MidasTouchComponent midasTouchComponent = UnboundEntityComponents.MIDAS_TOUCH.get(this.client.player);
        if (midasTouchComponent.isGolden()) {
            this.renderOverlay(context, GOLDEN_CHAINS_OVERLAY, 1.0f);
        }
    }

    @Inject(method = "renderCrosshair", at = @At("HEAD"), cancellable = true)
    private void noRenderCrosshairWithChains(DrawContext context, float tickDelta, CallbackInfo ci){
        if (this.client == null) {
            return;
        }
        ClientPlayerEntity clientPlayerEntity = this.client.player;
        if (clientPlayerEntity == null) {
            return;
        }
        MidasTouchComponent midasTouchComponent = UnboundEntityComponents.MIDAS_TOUCH.get(this.client.player);
        if (midasTouchComponent.isGolden()) {
            ci.cancel();
        }
    }
}
