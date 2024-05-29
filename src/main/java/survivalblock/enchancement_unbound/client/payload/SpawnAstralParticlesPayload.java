package survivalblock.enchancement_unbound.client.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.particle.GlowParticle;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Uuids;
import survivalblock.enchancement_unbound.client.util.UnboundClientUtil;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

import java.util.UUID;

public record SpawnAstralParticlesPayload(UUID uuid) implements CustomPayload {

    public static final CustomPayload.Id<SpawnAstralParticlesPayload> ID = CustomPayload.id(EnchancementUnbound.id("spawn_astral_particles").toString());
    public static final PacketCodec<PacketByteBuf, SpawnAstralParticlesPayload> CODEC = PacketCodec.tuple(Uuids.PACKET_CODEC, SpawnAstralParticlesPayload::uuid, SpawnAstralParticlesPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send(ServerPlayerEntity player, UUID uuid) {
        ServerPlayNetworking.send(player, new SpawnAstralParticlesPayload(uuid));
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<SpawnAstralParticlesPayload> {
        @Override
        public void receive(SpawnAstralParticlesPayload payload, ClientPlayNetworking.Context context) {
            MinecraftClient client = context.client();
            if (client == null) {
                return;
            }
            ClientWorld clientWorld = client.world;
            if (clientWorld == null) {
                return;
            }
            UUID uuid1 = payload.uuid();
            if (uuid1 == null) {
                return;
            }
            PlayerEntity player = clientWorld.getPlayerByUuid(uuid1);
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
}
