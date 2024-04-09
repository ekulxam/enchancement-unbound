package survivalblock.enchancement_unbound.common;


import eu.midnightdust.lib.config.MidnightConfig;
import moriyashiine.enchancement.common.Enchancement;
import moriyashiine.enchancement.common.init.ModEnchantments;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerEntityEvents;
import net.fabricmc.fabric.api.event.player.AttackEntityCallback;
import net.fabricmc.loader.DependencyException;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import survivalblock.enchancement_unbound.access.VeilModifyWorldAccess;
import survivalblock.enchancement_unbound.common.init.UnboundEnchantments;
import survivalblock.enchancement_unbound.common.init.UnboundItems;

public class EnchancementUnbound implements ModInitializer {
	public static final String MOD_ID = "enchancement_unbound";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		UnboundItems.init();
		UnboundEnchantments.init();
		if(!FabricLoader.getInstance().isModLoaded("enchancement")){
			LOGGER.error("Enchancement not found!");
            throw new RuntimeException("Missing dependency for mod " + MOD_ID);
			// System.exit(1);
        }
		if(FabricLoader.getInstance().isDevelopmentEnvironment()){
			LOGGER.info("Removing enchancement handicaps since 2024");
		}
		MidnightConfig.init(MOD_ID, UnboundConfig.class);
	}

	public static Identifier id(String value) {
		return new Identifier(MOD_ID, value);
	}

}
