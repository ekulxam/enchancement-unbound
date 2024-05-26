package survivalblock.enchancement_unbound.client.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import survivalblock.enchancement_unbound.client.util.UnboundClientUtil;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;

public class SpawnAstralParticlesPacket {

    public static final Identifier ID = EnchancementUnbound.id("spawn_astral_particles");

    public static void send(NbtCompound nbt, ServerPlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeNbt(nbt);
        ServerPlayNetworking.send(player, ID, buf);
    }

    @SuppressWarnings("unused") // method reference looks nicer
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        ClientWorld clientWorld = client.world;
        if (clientWorld == null) {
            return;
        }
        NbtCompound nbt = buf.readNbt();
        if (nbt == null) {
            return;
        }
        PlayerEntity player = clientWorld.getPlayerByUuid(nbt.getUuid("ObjUuid"));
        if (player == null) {
            return;
        }
        client.execute(() -> {
            for(int i = 0; i < 48; ++i) {
                GlowParticle glowParticle = UnboundClientUtil.createCustomGlowParticle(clientWorld, player.getParticleX(1.5), player.getRandomBodyY(), player.getParticleZ(1.5), 0.0, 0.0, 0.0);
                if (glowParticle == null) continue;
                client.particleManager.addParticle(glowParticle);
            }
        });
    }
}
