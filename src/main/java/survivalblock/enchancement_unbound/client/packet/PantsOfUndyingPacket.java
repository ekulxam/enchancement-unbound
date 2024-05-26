package survivalblock.enchancement_unbound.client.packet;

import io.netty.buffer.Unpooled;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;

public class PantsOfUndyingPacket {

    public static final Identifier ID = EnchancementUnbound.id("pants_of_undying_packet");

    public static void send(ItemStack stack, ServerPlayerEntity player) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeItemStack(stack);
        ServerPlayNetworking.send(player, ID, buf);
    }

    @SuppressWarnings("unused")
    public static void handle(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender sender) {
        ItemStack stack = buf.readItemStack();
        client.execute(() -> MinecraftClient.getInstance().gameRenderer.showFloatingItem(stack));
    }
}
