package survivalblock.enchancement_unbound.client;


import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import survivalblock.enchancement_unbound.client.packet.SpawnAstralParticlesPacket;
import survivalblock.enchancement_unbound.client.packet.UnboundConfigMatchPacket;
import survivalblock.enchancement_unbound.client.render.AstralPhantomEntityRenderer;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundEntityTypes;
import survivalblock.enchancement_unbound.common.init.UnboundItems;
import survivalblock.enchancement_unbound.mixin.veil.client.keybind.EnchancementClientAccessor;

public class EnchancementUnboundClient implements ClientModInitializer {


	public static final KeyBinding VEIL_SYZYGY_KEYBINDING = EnchancementClientAccessor.invokeRegisterKeyBinding(() -> KeyBindingHelper.registerKeyBinding(new KeyBinding("key.enchancement_unbound.veil_swap", GLFW.GLFW_KEY_ENTER, "key.categories.enchancement_unbound")));

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(UnboundConfigMatchPacket.ID, new UnboundConfigMatchPacket.UnboundReceiver());
		if (UnboundConfig.unboundItems) ModelPredicateProviderRegistry.register(UnboundItems.SLINGSHOT, new Identifier("pulling"), (stack, clientWorld, living, seed) -> living == null ? 0.0f : (living.isUsingItem() && living.getActiveItem() == stack ? 1.0F : 0.0F));
		ClientPlayNetworking.registerGlobalReceiver(SpawnAstralParticlesPacket.ID, SpawnAstralParticlesPacket::handle);
		EntityRendererRegistry.register(UnboundEntityTypes.ASTRAL_PHANTOM, AstralPhantomEntityRenderer::new);
	}
}
