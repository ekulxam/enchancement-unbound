package survivalblock.enchancement_unbound.client.packet;

import eu.midnightdust.lib.util.PlatformFunctions;
import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import net.minecraft.util.Identifier;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.UnboundConfig;

public class UnboundConfigMatchPacket {
    // literally just copied from EnforceConfigMatchPacket
    // moriya, if you're reading this and you don't want me to copy your code, just tell me to remove it and I will
    public static final Identifier ID = EnchancementUnbound.id("enforce_config_match");

    private static final Text DISCONNECT_TEXT = Text.literal("The server you are attempting to connect to has ")
            .append(Text.literal("Enchancement Unbound").formatted(Formatting.GREEN))
            .append(" installed, but your configuration file does not match the server's.\n\n")
            .append(Text.literal("Please make sure your configuration file matches the server's.\n").formatted(Formatting.RED))
            .append(Text.literal("Your configuration file is located at ").formatted(Formatting.RED))
            .append(Text.literal(PlatformFunctions.getConfigDirectory().resolve(EnchancementUnbound.MOD_ID + ".json").toString()).formatted(Formatting.BLUE))
            .append(Text.literal(".\n\n").formatted(Formatting.RED))
            .append(Text.literal("This is not a bug, do not report it.").formatted(Formatting.DARK_RED, Formatting.BOLD));

    public static void send(ServerPlayerEntity player, int encoding) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeInt(encoding);
        ServerPlayNetworking.send(player, ID, buf);
    }

    public static class Receiver implements ClientPlayNetworking.PlayChannelHandler {
        @Override
        public void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
            int encoding = buf.readInt();
            client.execute(() -> {
                if (UnboundConfig.encode() != encoding) {
                    handler.getConnection().disconnect(DISCONNECT_TEXT);
                }
            });
        }
    }
}
