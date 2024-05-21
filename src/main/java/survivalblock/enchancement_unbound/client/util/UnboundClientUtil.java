package survivalblock.enchancement_unbound.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.UnboundConfig;

public class UnboundClientUtil {

    public static boolean shouldRenderWithEndShader(Entity entity){
        return entity instanceof PlayerEntity player && EnchancementUtil.hasEnchantment(ModEnchantments.VEIL, player);
    }
    public static RenderLayer getEndShader(){
        return RenderLayer.getEndPortal();
    }

    public static Identifier getSuperSecretTikTokShaderThing() {
        return EnchancementUnbound.id("shaders/post/sobel.json");
    }

    public static class BossBar {
        public static final Identifier BARS_TEXTURE = new Identifier("textures/gui/bars.png");
        public static final int WIDTH = 182;
        public static final int HEIGHT = 5;
        public static final int NOTCHED_BAR_OVERLAY_V = 80;

        public static void renderBossBar(DrawContext context, float percent) {
            // I BORROWED the BossBar rendering code from BossBarHud
            // I didn't want to construct a new bossbar just in case of UUID conflicts
            int y = context.getScaledWindowHeight() / 2 + 91;
            int x = 12;
            renderBossBar(context, x, y, WIDTH, 0);
            int realPercent = (int) (percent * 183.0f);
            if (realPercent > 0) {
                renderBossBar(context, x, y, realPercent, HEIGHT);
            }
        }

        private static void renderBossBar(DrawContext context, int x, int y, int width, int height) {
            MatrixStack matrixStack = context.getMatrices();
            matrixStack.push();
            matrixStack.multiply(RotationAxis.NEGATIVE_Z.rotationDegrees(90), x, y, 0);
            context.drawTexture(BARS_TEXTURE, x, y, 0, UnboundConfig.astralPlaneInsanityBarColor.ordinal() * 5 * 2 + height, width, HEIGHT);
            net.minecraft.entity.boss.BossBar.Style style = UnboundConfig.astralPlaneInsanityBarStyle;
            if (style != net.minecraft.entity.boss.BossBar.Style.PROGRESS) {
                RenderSystem.enableBlend();
                context.drawTexture(BARS_TEXTURE, x, y, 0, NOTCHED_BAR_OVERLAY_V + (style.ordinal() - 1) * 5 * 2 + height, width, HEIGHT);
                RenderSystem.disableBlend();
            }
            matrixStack.pop();
        }
    }
}
