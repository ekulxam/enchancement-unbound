package survivalblock.enchancement_unbound.common;


import eu.midnightdust.lib.config.MidnightConfig;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.player.*;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundEntityTypes;
import survivalblock.enchancement_unbound.common.init.UnboundItems;
import survivalblock.enchancement_unbound.common.init.UnboundSoundEvents;
import survivalblock.enchancement_unbound.common.util.UnboundUtil;

public class EnchancementUnbound implements ModInitializer {
	public static final String MOD_ID = "enchancement_unbound";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@SuppressWarnings("CodeBlock2Expr")
	@Override
	public void onInitialize() {
		if(!FabricLoader.getInstance().isModLoaded("enchancement")){
			LOGGER.error("Enchancement not found!");
			throw new RuntimeException("Missing dependency for mod " + MOD_ID);
			// System.exit(1);
		}
		UnboundItems.init();
		UnboundEnchantments.init();
		UnboundEntityTypes.init();
		UnboundSoundEvents.init();
		if(FabricLoader.getInstance().isDevelopmentEnvironment()){
			LOGGER.info("Removing enchancement handicaps since 2024");
		}
		MidnightConfig.init(EnchancementUnbound.MOD_ID, UnboundConfig.class);
		AttackBlockCallback.EVENT.register((player, world, hand, pos, direction) -> {
			return UnboundUtil.preventedActionResult(player);
		});
		AttackEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return UnboundUtil.preventedActionResult(player);
		});
		UseBlockCallback.EVENT.register((player, world, hand, hitResult) -> {
			return UnboundUtil.preventedActionResult(player);
		});
		UseItemCallback.EVENT.register((player, world, hand) -> {
			ItemStack stack = player.getStackInHand(hand);
			return UnboundUtil.preventedTypedActionResult(player, stack);
		});
		UseEntityCallback.EVENT.register((player, world, hand, entity, hitResult) -> {
			return UnboundUtil.preventedActionResult(player);
		});
	}

	public static Identifier id(String value) {
		return new Identifier(MOD_ID, value);
	}

}
