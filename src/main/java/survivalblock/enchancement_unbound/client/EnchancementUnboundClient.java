package survivalblock.enchancement_unbound.client;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import survivalblock.enchancement_unbound.client.packet.UnboundConfigMatchPacket;

public class EnchancementUnboundClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(UnboundConfigMatchPacket.ID, new UnboundConfigMatchPacket.Receiver());
	}
}
