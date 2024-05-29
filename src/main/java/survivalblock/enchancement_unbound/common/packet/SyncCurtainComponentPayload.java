package survivalblock.enchancement_unbound.common.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

public record SyncCurtainComponentPayload(NbtCompound nbt) implements CustomPayload {

    public static final CustomPayload.Id<SyncCurtainComponentPayload> ID = CustomPayload.id(EnchancementUnbound.id("sync_curtain_component").toString());
    public static final PacketCodec<PacketByteBuf, SyncCurtainComponentPayload> CODEC = PacketCodec.tuple(PacketCodecs.NBT_COMPOUND, SyncCurtainComponentPayload::nbt, SyncCurtainComponentPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send(NbtCompound nbt) {
        ClientPlayNetworking.send(new SyncCurtainComponentPayload(nbt));
    }

    public static class Receiver implements ServerPlayNetworking.PlayPayloadHandler<SyncCurtainComponentPayload> {
        @Override
        public void receive(SyncCurtainComponentPayload payload, ServerPlayNetworking.Context context) {
            NbtCompound nbt = payload.nbt();
            if (nbt == null) {
                return;
            }
            ServerPlayerEntity player = context.player();
            if (!nbt.getUuid("ObjUuid").equals(player.getUuid())) {
                return;
            }
            CurtainComponent curtainComponent = UnboundEntityComponents.CURTAIN.get(player);
            curtainComponent.readFromNbt(nbt);
        }
    }
}
