package survivalblock.enchancement_unbound.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.fabricmc.fabric.mixin.client.particle.ParticleManagerAccessor;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.particle.SpriteProvider;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.registry.Registries;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import survivalblock.enchancement_unbound.client.EnchancementUnboundClient;
import survivalblock.enchancement_unbound.client.packet.SpawnAstralParticlesPacket;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.packet.SyncCurtainComponentPacket;
import survivalblock.enchancement_unbound.mixin.veil.client.particle.GlowParticleAccessor;
import survivalblock.enchancement_unbound.mixin.veil.client.particle.ParticleAccessor;

public class CurtainComponent implements AutoSyncedComponent, ClientTickingComponent {

    private final PlayerEntity obj;
    private boolean activated = false;
    private boolean wasForced = false;
    private int swapCooldownTicks = 0;
    private static final Random RANDOM = Random.create();

    public CurtainComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    @Override
    public void clientTick() {
        boolean canIBeActivated = EnchancementUtil.hasEnchantment(ModEnchantments.VEIL, this.obj) && UnboundConfig.astralVeil;
        if (canIBeActivated && EnchancementUnboundClient.VEIL_SYZYGY_KEYBINDING.wasPressed() && swapCooldownTicks <= 0) {
            if (this.isInCurtain()) {
                if (!this.wasForced) {
                    this.setInCurtain(false, false);
                }
            } else {
                this.setInCurtain(true, false);
            }
        }
        if (!canIBeActivated && !this.wasForced) {
            this.setInCurtain(false, false);
        }
        if (this.swapCooldownTicks > 0) decrementSwapCooldownTicks();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        boolean isInCurtain = tag.getBoolean("IsInCurtain");
        if (this.activated != isInCurtain) {
            this.activated = isInCurtain;
            refreshShader();
        }
        this.wasForced = tag.getBoolean("WasForced");
        this.swapCooldownTicks = tag.getInt("SwapCooldownTicks");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IsInCurtain", this.activated);
        tag.putBoolean("WasForced", this.wasForced);
        tag.putInt("SwapCooldownTicks", this.swapCooldownTicks);
    }

    public boolean isInCurtain(){
        return this.activated;
    }

    private void decrementSwapCooldownTicks() {
        this.swapCooldownTicks--;
        sync();
    }

    private void sync() {
        NbtCompound nbt = new NbtCompound();
        this.writeToNbt(nbt);
        nbt.putUuid("ObjUuid", this.obj.getUuid());
        SyncCurtainComponentPacket.send(nbt);
    }

    public void setInCurtain(boolean value, boolean wasForced){
        if (this.activated != value) {
            this.activated = value;
            refreshShader();
        }
        this.wasForced = wasForced;
        if (wasForced) {
            this.swapCooldownTicks = -1;
        } else {
            this.swapCooldownTicks = 20;
        }
        sync();
    }

    private void refreshShader() {
        World world = this.obj.getWorld();
        if (world instanceof ServerWorld serverWorld) {
            this.obj.stopRiding();
            for (ServerPlayerEntity player : serverWorld.getPlayers()) {
                NbtCompound nbt = new NbtCompound();
                nbt.putUuid("ObjUuid", this.obj.getUuid());
                SpawnAstralParticlesPacket.send(nbt, player);
            }
            return;
        }
        MinecraftClient client = MinecraftClient.getInstance();
        if (client == null) {
            return;
        }
        ClientPlayerEntity player = client.player;
        if (player == null) {
            return;
        }
        GameRenderer gameRenderer = client.gameRenderer;
        if (gameRenderer == null) {
            return;
        }
        try {
            gameRenderer.onCameraEntitySet(client.cameraEntity);
        } catch (Exception ignored) {

        }
    }

    @SuppressWarnings({"UnstableApiUsage", "UnreachableCode", "SameParameterValue"})
    public static GlowParticle createCustomGlowParticle(ClientWorld clientWorld, double x, double y, double z, double velocityX, double velocityY, double velocityZ) {
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
        GlowParticle glowParticle = GlowParticleAccessor.invokeGlowParticleConstructor(clientWorld, x, y, z, 0.5 - RANDOM.nextDouble(), velocityY, 0.5 - RANDOM.nextDouble(), spriteProvider);
        int colorType = MathHelper.nextInt(RANDOM, 0, 3);
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
}
