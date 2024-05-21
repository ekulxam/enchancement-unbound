package survivalblock.enchancement_unbound.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
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
import net.minecraft.server.MinecraftServer;
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
import survivalblock.enchancement_unbound.common.entity.AstralPhantomEntity;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.packet.SyncCurtainComponentPacket;
import survivalblock.enchancement_unbound.mixin.veil.client.particle.GlowParticleAccessor;
import survivalblock.enchancement_unbound.mixin.veil.client.particle.ParticleAccessor;

public class CurtainComponent implements AutoSyncedComponent, CommonTickingComponent {

    private final PlayerEntity obj;
    private boolean activated = false;
    private int swapCooldownTicks = 0;
    private int ticksInAstralPlane = 0;
    public static final int MAX_INSANITY = 7200; // 5 minutes
    private static final Random RANDOM = Random.create();
    private boolean isAware = false;

    public CurtainComponent(PlayerEntity obj) {
        this.obj = obj;
    }

    @Override
    public void clientTick() {
        boolean canIBeActivated = EnchancementUtil.hasEnchantment(ModEnchantments.VEIL, this.obj) && UnboundConfig.astralVeil;
        if (canIBeActivated && EnchancementUnboundClient.VEIL_SYZYGY_KEYBINDING.wasPressed() && swapCooldownTicks <= 0) {
            this.setInCurtain(!this.isInCurtain(), false);
        }
        if (!canIBeActivated && swapCooldownTicks <= 0) {
            this.setInCurtain(false, false);
        }
        if (this.swapCooldownTicks > 0) decrementSwapCooldownTicks();
        accumulateTicksInAstralPlane(this.isInCurtain());
        CommonTickingComponent.super.clientTick();
    }

    public int getTicksInAstralPlane() {
        return this.ticksInAstralPlane;
    }

    @Override
    public void tick() {

    }

    @Override
    public void serverTick() {
        if (this.activated && this.ticksInAstralPlane % 100 == 0 && this.ticksInAstralPlane > 0) {
            // if activated and has been in plane (and check every 5 seconds)
            if (this.obj instanceof ServerPlayerEntity serverPlayer) {
                MinecraftServer server = serverPlayer.getServer();
                if (server != null) {
                    int chance = Math.max(0, MathHelper.nextInt(RANDOM, 0, MAX_INSANITY / 2 - 1));
                    if (chance < (this.ticksInAstralPlane / 2)) {
                        server.execute(() -> {
                            ServerWorld serverWorld = serverPlayer.getServerWorld();
                            AstralPhantomEntity astralPhantom = AstralPhantomEntity.of(serverWorld, serverPlayer);
                            if (astralPhantom != null) serverWorld.spawnEntity(astralPhantom);
                        });
                    }
                }
            }
        }
        CommonTickingComponent.super.serverTick();
    }

    @Override
    public void readFromNbt(NbtCompound tag) {
        boolean isInCurtain = tag.getBoolean("IsInCurtain");
        if (this.activated != isInCurtain) {
            this.activated = isInCurtain;
            refreshShader();
        }
        this.swapCooldownTicks = tag.getInt("SwapCooldownTicks");
        this.ticksInAstralPlane = tag.getInt("TicksActivated");
        this.isAware = tag.getBoolean("IsAware");
    }

    @Override
    public void writeToNbt(NbtCompound tag) {
        tag.putBoolean("IsInCurtain", this.activated);
        tag.putInt("SwapCooldownTicks", this.swapCooldownTicks);
        tag.putInt("TicksActivated", this.ticksInAstralPlane);
        tag.putBoolean("IsAware", this.isAware);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isAware() {
        return this.isAware;
    }

    public boolean isInCurtain(){
        return this.activated;
    }

    private void decrementSwapCooldownTicks() {
        this.swapCooldownTicks--;
        sync();
    }

    private void accumulateTicksInAstralPlane(boolean increment) {
        if (this.obj.isSpectator() || this.obj.isCreative()) {
            return;
        }
        if (increment) {
            if (this.ticksInAstralPlane <= MAX_INSANITY) this.ticksInAstralPlane++;
        } else {
            if (this.ticksInAstralPlane > 0) this.ticksInAstralPlane--;
        }
        sync();
    }

    public void sync() {
        boolean fromClient = this.obj.getWorld().isClient();
        if (fromClient) {
            NbtCompound nbt = new NbtCompound();
            this.writeToNbt(nbt);
            nbt.putUuid("ObjUuid", this.obj.getUuid());
            SyncCurtainComponentPacket.send(nbt);
        } else {
            UnboundEntityComponents.CURTAIN.sync(this.obj);
        }
    }

    public void setInCurtain(boolean value, boolean wasForced){
        if (this.activated != value) {
            this.activated = value;
            refreshShader();
        }
        if (this.activated) {
            this.isAware = true;
        }
        if (wasForced) {
            this.swapCooldownTicks = 200;
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
