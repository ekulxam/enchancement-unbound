package survivalblock.enchancement_unbound.client.payload;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

public record PantsOfUndyingPayload(ItemStack stack) implements CustomPayload {

    public static final CustomPayload.Id<PantsOfUndyingPayload> ID = CustomPayload.id(EnchancementUnbound.id("pants_of_undying").toString());
    public static final PacketCodec<RegistryByteBuf, PantsOfUndyingPayload> CODEC = PacketCodec.tuple(ItemStack.PACKET_CODEC, PantsOfUndyingPayload::stack, PantsOfUndyingPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send(ItemStack stack, ServerPlayerEntity player) {
        ServerPlayNetworking.send(player, new PantsOfUndyingPayload(stack));
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<PantsOfUndyingPayload> {
        @Override
        public void receive(PantsOfUndyingPayload payload, ClientPlayNetworking.Context context) {
            ItemStack stack1 = payload.stack();
            MinecraftClient client = context.client();
            client.execute(() -> client.gameRenderer.showFloatingItem(stack1));
        }
    }
}
