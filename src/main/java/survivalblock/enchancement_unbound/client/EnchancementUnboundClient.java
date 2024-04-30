package survivalblock.enchancement_unbound.client;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.util.Identifier;
import survivalblock.enchancement_unbound.client.packet.UnboundConfigMatchPacket;
import survivalblock.enchancement_unbound.common.init.UnboundItems;

public class EnchancementUnboundClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(UnboundConfigMatchPacket.ID, new UnboundConfigMatchPacket.Receiver());
		ModelPredicateProviderRegistry.register(UnboundItems.SLINGSHOT, new Identifier("pulling"), (stack, clientWorld, living, seed) -> living == null ? 0.0f : (living.isUsingItem() && living.getActiveItem() == stack ? 1.0F : 0.0F));
	}
}
