package survivalblock.enchancement_unbound.common.packet;

import io.netty.buffer.Unpooled;
import moriyashiine.enchancement.client.packet.AddStrafeParticlesPacket;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.component.entity.StrafeComponent;
import moriyashiine.enchancement.common.init.ModEntityComponents;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import survivalblock.enchancement_unbound.common.EnchancementUnbound;
import survivalblock.enchancement_unbound.common.component.CurtainComponent;
import survivalblock.enchancement_unbound.common.init.UnboundEntityComponents;

public class SyncCurtainComponentPacket {

    public static final Identifier ID = EnchancementUnbound.id("sync_curtain_component");

    public static void send(NbtCompound nbt) {
        PacketByteBuf buf = new PacketByteBuf(Unpooled.buffer());
        buf.writeNbt(nbt);
        ClientPlayNetworking.send(ID, buf);
    }

    @SuppressWarnings("unused")
    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound nbt = buf.readNbt();
        if (nbt == null) {
            return;
        }
        if (!nbt.getUuid("ObjUuid").equals(player.getUuid())) {
            return;
        }
        CurtainComponent curtainComponent = UnboundEntityComponents.CURTAIN.get(player);
        curtainComponent.readFromNbt(nbt);
    }
}
