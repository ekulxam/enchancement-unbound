package survivalblock.enchancement_unbound.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.CommonTickingComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import survivalblock.enchancement_unbound.client.EnchancementUnboundClient;
import survivalblock.enchancement_unbound.client.packet.SpawnAstralParticlesPacket;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.entity.AstralPhantomEntity;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;
import survivalblock.enchancement_unbound.common.packet.SyncCurtainComponentPacket;

public class CurtainComponent implements AutoSyncedComponent, CommonTickingComponent {

    private final PlayerEntity obj;
    private boolean activated = false;
    private int swapCooldownTicks = 0;
    private int ticksInAstralPlane = 0;
    public static final int MAX_INSANITY = 9000; // 7.5 minutes
    public static final Random RANDOM = Random.create();
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
        if (this.activated && this.ticksInAstralPlane % 400 == 0 && this.ticksInAstralPlane > 0) {
            // if activated and has been in plane (and check every 20 seconds)
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
}
