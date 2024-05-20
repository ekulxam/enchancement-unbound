package survivalblock.enchancement_unbound.common.component;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import dev.onyxstudios.cca.api.v3.component.tick.ClientTickingComponent;
import moriyashiine.enchancement.common.init.ModEnchantments;
import moriyashiine.enchancement.common.util.EnchancementUtil;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.world.World;
import survivalblock.enchancement_unbound.client.EnchancementUnboundClient;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.packet.SyncCurtainComponentPacket;

public class CurtainComponent implements AutoSyncedComponent, ClientTickingComponent {

    private final PlayerEntity obj;
    private boolean activated = false;
    private boolean wasForced = false;
    private int swapCooldownTicks = 0;

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
        if (!world.isClient()) {
            this.obj.stopRiding();
            return;
        }
        for(int i = 0; i < 16; ++i) {
            this.obj.getWorld().addParticle(ParticleTypes.GLOW, this.obj.getParticleX(1.5), this.obj.getRandomBodyY(), this.obj.getParticleZ(1.5), 0.0, 0.0, 0.0);
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
