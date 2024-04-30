package survivalblock.enchancement_unbound.common;


import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundItems;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

public class EnchancementUnbound implements ModInitializer {
	public static final String MOD_ID = "enchancement_unbound";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@SuppressWarnings("CodeBlock2Expr")
	@Override
	public void onInitialize() {
		if(!FabricLoader.getInstance().isModLoaded("enchancement")){
			LOGGER.error("Enchancement not found!");
			LOGGER.warn("How did we get here?");
			throw new RuntimeException("Missing dependency for mod " + MOD_ID);
			// System.exit(1);
		}
		if (UnboundConfig.unboundItems) UnboundItems.init();
		if (UnboundConfig.unboundEnchantments) UnboundEnchantments.init();
		if(FabricLoader.getInstance().isDevelopmentEnvironment()){
			LOGGER.info("Removing enchancement handicaps since 2024");
		}
		MidnightConfig.init(EnchancementUnbound.MOD_ID, UnboundConfig.class);
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			return UnboundUtil.veilActionResult(player);
		});
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return UnboundUtil.veilActionResult(player);
		});
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> UnboundUtil.veilActionResult(player));
		UseItemCallback.EVENT.register((player, world, hand) -> {
			return UnboundUtil.veilTypedActionResult(player, player.getStackInHand(hand));
		});
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return UnboundUtil.veilActionResult(player);
		});
	}

	public static Identifier id(String value) {
		return new Identifier(MOD_ID, value);
	}
}
