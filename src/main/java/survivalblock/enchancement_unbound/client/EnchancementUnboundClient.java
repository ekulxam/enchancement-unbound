package survivalblock.enchancement_unbound.client;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.entity.EntityRenderer;
import survivalblock.enchancement_unbound.client.packet.UnboundConfigMatchPacket;
import survivalblock.enchancement_unbound.client.render.ProjectedShieldEntityRenderer;
import survivalblock.enchancement_unbound.client.render.ShieldboardEntityRenderer;
import survivalblock.enchancement_unbound.common.init.UnboundEntityTypes;

public class EnchancementUnboundClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(UnboundConfigMatchPacket.ID, new UnboundConfigMatchPacket.Receiver());
		EntityRendererRegistry.register(UnboundEntityTypes.SHIELDBOARD, ShieldboardEntityRenderer::new);
		EntityRendererRegistry.register(UnboundEntityTypes.PROJECTED_SHIELD, ProjectedShieldEntityRenderer::new);
	}
}
