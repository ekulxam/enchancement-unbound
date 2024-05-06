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
import survivalblock.enchancement_unbound.common.init.UnboundSoundEvents;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

public class EnchancementUnbound implements ModInitializer {
	public static final String MOD_ID = "enchancement_unbound";
	public static final String CAPITALIZED_MOD_ID = "Enchancement_Unbound";
	public static final Logger LOGGER = LoggerFactory.getLogger(CAPITALIZED_MOD_ID);

	@SuppressWarnings({"CodeBlock2Expr", "PointlessBooleanExpression", "RedundantSuppression"})
	@Override
	public void onInitialize() {
		if(!FabricLoader.getInstance().isModLoaded("enchancement")){
			try {
				LOGGER.warn("How did we get here?");
				throw new RuntimeException("This is a pre-defined exception thrown by " + CAPITALIZED_MOD_ID + ". Go download Enchancement, as it is a required dependency! You shouldn't even be reading this as an error message.");
			} catch (Exception e) {
				LOGGER.error("Missing dependency for mod " + MOD_ID, e);
				System.exit(-1);
			}
		}
		MidnightConfig.init(EnchancementUnbound.MOD_ID, UnboundConfig.class);
		if (UnboundConfig.unboundItems) UnboundItems.init();
		if (UnboundConfig.unboundEnchantments) UnboundEnchantments.init();
		UnboundSoundEvents.init();
		if(FabricLoader.getInstance().isDevelopmentEnvironment()){
			LOGGER.info("Removing enchancement handicaps since 2024");
		}
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
