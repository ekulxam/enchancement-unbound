package survivalblock.enchancement_unbound.client.payload;

import eu.midnightdust.lib.util.PlatformFunctions;
import moriyashiine.enchancement.client.payload.EnforceConfigMatchPayload;
import moriyashiine.enchancement.common.ModConfig;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.UnboundConfig;

public record UnboundConfigMatchPayload(int encoding) implements CustomPayload {
    public static final CustomPayload.Id<UnboundConfigMatchPayload> ID = CustomPayload.id(EnchancementUnbound.id("unbound_config_match").toString());
    public static final PacketCodec<PacketByteBuf, UnboundConfigMatchPayload> CODEC = PacketCodec.tuple(PacketCodecs.VAR_INT, UnboundConfigMatchPayload::encoding, UnboundConfigMatchPayload::new);

    private static final Text UNBOUND_DISCONNECT_TEXT = Text.literal("The server you are attempting to connect to has ")
            .append(Text.literal("Enchancement Unbound").formatted(Formatting.GREEN))
            .append(" installed, but your configuration file does not match the server's.\n\n")
            .append(Text.literal("Please make sure your configuration file matches the server's.\n").formatted(Formatting.RED))
            .append(Text.literal("Your configuration file is located at ").formatted(Formatting.RED))
            .append(Text.literal(PlatformFunctions.getConfigDirectory().resolve("enchancement_unbound.json").toString()).formatted(Formatting.BLUE))
            .append(Text.literal(".\n\n").formatted(Formatting.RED))
            .append(Text.literal("This is not a bug, do not report it.").formatted(Formatting.DARK_RED, Formatting.BOLD));

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public static void send(ServerPlayerEntity player, int encoding) {
        ServerPlayNetworking.send(player, new UnboundConfigMatchPayload(encoding));
    }

    public static class Receiver implements ClientPlayNetworking.PlayPayloadHandler<UnboundConfigMatchPayload> {
        @Override
        public void receive(UnboundConfigMatchPayload payload, ClientPlayNetworking.Context context) {
            if (UnboundConfig.encode() != payload.encoding()) {
                context.player().networkHandler.getConnection().disconnect(UNBOUND_DISCONNECT_TEXT);
            }
        }
    }
}
