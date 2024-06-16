package survivalblock.enchancement_unbound.client.util;

import com.mojang.blaze3d.systems.RenderSystem;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.Entity;
import net.minecraft.entity.boss.BossBar;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;
import survivalblock.enchancement_unbound.mixin.veil.client.particle.GlowParticleAccessor;
import survivalblock.enchancement_unbound.mixin.veil.client.particle.ParticleAccessor;

public class UnboundClientUtil {

    @SuppressWarnings({"UnstableApiUsage", "UnreachableCode", "SameParameterValue"})
    public static GlowParticle createCustomGlowParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
        // SHOULD ONLY BE CALLED IN A RUNNABLE TO MinecraftClient#execute TO AVOID THREAD PROBLEMS
        SpriteProvider spriteProvider;
        try {
            Identifier glowParticleId = Registries.PARTICLE_TYPE.getId(ParticleTypes.GLOW);
            spriteProvider = ((ParticleManagerAccessor) MinecraftClient.getInstance().particleManager).getSpriteAwareFactories().get(glowParticleId);
        } catch (Exception exception) {
            EnchancementUnbound.LOGGER.error("An error occured while trying to access Sprite Providers!", exception);
            return null;
        }
        if (spriteProvider == null) {
            return null;
        }
        GlowParticle glowParticle = GlowParticleAccessor.invokeGlowParticleConstructor(clientWorld, x, y, z, 0.5 - CurtainComponent.RANDOM.nextDouble(), velocityY, 0.5 - CurtainComponent.RANDOM.nextDouble(), spriteProvider);
        int colorType = MathHelper.nextInt(CurtainComponent.RANDOM, 0, 3);
        if (colorType == 0) {
            // 232 174 182
            glowParticle.setColor((232 / 255f), (174 / 255f), (182 / 255f));
        } else if (colorType == 1) {
            // 225 167 193
            glowParticle.setColor((225 / 255f), (167 / 255f), (193 / 255f));
        } else if (colorType == 2) {
            // 146 98 166
            glowParticle.setColor((146 / 255f), (98 / 255f), (166 / 255f));
        } else if (colorType == 3) {
            // 42 92 128
            glowParticle.setColor((42 / 255f), (92 / 255f), (128 / 255f));
        }
        ((ParticleAccessor) glowParticle).setVelocityY(((ParticleAccessor) glowParticle).getVelocityY() * 0.6f);
        if (velocityX == 0.0 && velocityZ == 0.0) {
            ((ParticleAccessor) glowParticle).setVelocityX(((ParticleAccessor) glowParticle).getVelocityX() * 0.1f);
            ((ParticleAccessor) glowParticle).setVelocityZ(((ParticleAccessor) glowParticle).getVelocityZ() * 0.1f);
        }
        glowParticle.setMaxAge((int) (8.0 / (clientWorld.random.nextDouble() * 0.8 + 0.2)));
        return glowParticle;
    }

    public static boolean shouldRenderWithEndShader(Entity entity){
        return entity instanceof PlayerEntity player && EnchancementUtil.hasEnchantment(ModEnchantments.VEIL, player);
    }
    public static RenderLayer getEndShader(){
        return RenderLayer.getEndPortal();
    }

    public static Identifier getSuperSecretTikTokShaderThing() {
        return EnchancementUnbound.id("shaders/post/sobel.json");
    }


    public static class BossBarUtil {
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
            BossBar.Style style = BossBar.Style.valueOf(UnboundConfig.astralPlaneInsanityBarStyle.name());
            if (style != net.minecraft.entity.boss.BossBar.Style.PROGRESS) {
                RenderSystem.enableBlend();
                context.drawTexture(BARS_TEXTURE, x, y, 0, NOTCHED_BAR_OVERLAY_V + (style.ordinal() - 1) * 5 * 2 + height, width, HEIGHT);
                RenderSystem.disableBlend();
            }
            matrixStack.pop();
        }
    }
}
