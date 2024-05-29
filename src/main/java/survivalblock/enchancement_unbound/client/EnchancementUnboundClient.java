package survivalblock.enchancement_unbound.client;


import moriyashiine.enchancement.client.event.EnchantedToolsHaveEfficiencyEvent;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.item.ModelPredicateProviderRegistry;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;
import survivalblock.enchancement_unbound.client.event.EnchantedCrossbowsHaveMultishotEvent;
import survivalblock.enchancement_unbound.client.payload.PantsOfUndyingPayload;
import survivalblock.enchancement_unbound.client.payload.SpawnAstralParticlesPayload;
import survivalblock.enchancement_unbound.client.payload.UnboundConfigMatchPayload;
import survivalblock.enchancement_unbound.client.render.AstralPhantomEntityRenderer;
import survivalblock.enchancement_unbound.client.render.SentientPantsEntityRenderer;
import survivalblock.enchancement_unbound.client.render.UnboundEntityModelLayers;
import survivalblock.enchancement_unbound.common.UnboundConfig;
import survivalblock.enchancement_unbound.common.init.UnboundEntityTypes;
import survivalblock.enchancement_unbound.common.init.UnboundItems;
import survivalblock.enchancement_unbound.mixin.veil.client.keybind.EnchancementClientAccessor;

import java.util.function.Supplier;

public class EnchancementUnboundClient implements ClientModInitializer {


	public static final KeyBinding VEIL_SYZYGY_KEYBINDING = registerKeyBinding(() -> KeyBindingHelper.registerKeyBinding(new KeyBinding("key.enchancement_unbound.veil_swap", GLFW.GLFW_KEY_ENTER, "key.categories.enchancement_unbound")));

	@Override
	public void onInitializeClient() {
		ClientPlayNetworking.registerGlobalReceiver(UnboundConfigMatchPayload.ID, new UnboundConfigMatchPayload.Receiver());
		if (UnboundConfig.unboundItems) ModelPredicateProviderRegistry.register(UnboundItems.SLINGSHOT, new Identifier("pulling"), (stack, clientWorld, living, seed) -> living == null ? 0.0f : (living.isUsingItem() && living.getActiveItem() == stack ? 1.0F : 0.0F));
		ClientPlayNetworking.registerGlobalReceiver(SpawnAstralParticlesPayload.ID, new SpawnAstralParticlesPayload.Receiver());
		ClientPlayNetworking.registerGlobalReceiver(PantsOfUndyingPayload.ID, new PantsOfUndyingPayload.Receiver());
		EntityRendererRegistry.register(UnboundEntityTypes.ASTRAL_PHANTOM, AstralPhantomEntityRenderer::new);
		EntityRendererRegistry.register(UnboundEntityTypes.SENTIENT_PANTS, SentientPantsEntityRenderer::new);
		ItemTooltipCallback.EVENT.register(new EnchantedCrossbowsHaveMultishotEvent());
		UnboundEntityModelLayers.init();
	}

	public static KeyBinding registerKeyBinding(Supplier<KeyBinding> supplier) {
		return EnchancementClientAccessor.invokeRegisterKeyBinding(supplier);
	}
}
