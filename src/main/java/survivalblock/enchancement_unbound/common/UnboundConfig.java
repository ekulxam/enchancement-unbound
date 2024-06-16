/*
 * All Rights Reserved (c) MoriyaShiine
 */

package survivalblock.enchancement_unbound.common;

import eu.midnightdust.lib.config.MidnightConfig;
import net.minecraft.entity.boss.BossBar;


public class UnboundConfig extends MidnightConfig {

	@SuppressWarnings("unused")
	@Comment public static Comment welcome;
	@Entry
	public static boolean unboundItems = false;
	@Entry(min = 0, max = 100)
	public static float orbitalBrimstoneExplosionPower = 0;
	@Entry
	public static boolean orbitalBrimstoneGoesThroughWalls = false;
	@Entry
	public static boolean unboundEnchantments = false;
	@Entry
	public static boolean noCrossbowCooldown = false;
	@Entry
	public static boolean brimstoneSelfDamage = true;
	@Entry(min = 50)
	public static double maxBrimstoneDamage = 50;
	@Entry
	public static boolean allCrossbowsHaveMultishot = false;
	@Entry
	public static boolean superQuickChargeCrossbow = false;
	@Entry
	public static boolean sustainExtraDamageWhileSliding = true;
	@Entry
	public static boolean shouldDealSlamDamage = false;
	@Entry
	public static boolean slamSelfDamage = false;
	@Entry(min = 0, max = Long.MAX_VALUE)
	public static double slideImpactDamage = 4;
	@Entry
	public static boolean impactFallsSlightlyFaster = false;
	@Entry
	public static boolean veilUsersAlwaysInvisible = false;
	@Entry
	public static boolean astralVeil = false;
	@Entry
	public static boolean perceptionUsersGetESP = false;
	@Entry
	public static boolean amphibiousAirSwimming = false;
	@Entry
	public static boolean amphibiousExtendedWaterForever = false;
	@Entry
	public static boolean infiniteGale = false;
	@Entry
	public static boolean infiniteDash = false;
	@Entry
	public static boolean infiniteStrafe = false;
	@Entry
	public static boolean airbending = false;
	@Entry
	public static boolean assimilationInhaleFood = false;
	@Entry
	public static boolean noBerserkDamageCap = false;
	@Entry(min = 0.1, max = 10)
	public static float scatterProjectileMultiplier = 1;
	@Entry
	public static boolean wardenspineYeets = false;
	@Entry
	public static boolean noBuryCooldown = false;
	@Entry
	public static boolean canBuryEverything = false;
	@Entry
	public static boolean frostbiteTridentFreezesWater = false;
	@Entry
	public static BouncyChargeSpeed bouncyChargeSpeed = BouncyChargeSpeed.DEFAULT;

	public enum BouncyChargeSpeed {
		DEFAULT, FAST, VERY_FAST, VERY_VERY_FAST, INSTANT
	}
	@Entry(min = 0.1, max = 15)
	public static double bouncyJumpMultiplier = 1;
	@Entry
	public static boolean disarmStealsPlayerItems = false;
	@Entry
	public static boolean leechForever = false;
	@Entry(min = 1, max = 200)
	public static int leechInterval = 20;
	@Entry
	public static boolean strongerChaosEffects = false;
	@Entry
	public static boolean instantChargeDelay = false;
	@Entry(min = 1)
	public static int delayFloatTime = 200;
	@Entry(min = 0.01)
	public static double homingRadius = 0.5;
	@Entry(min = 1)
	public static float grapplePullEntityMultiplier = 1; // lag
	@Entry(min = 1)
	public static float grapplePullUserMultiplier = 1; // lag
	@Entry
	public static boolean loyaltyWarpTridentsReturnFaster = false;
	@Entry
	public static boolean infiniteFrostbite = false;
	@Entry
	public static boolean infiniteFireAspect = false;
	@Entry
	public static boolean innateEfficiency = false;
	@Entry
	public static boolean removeAdrenalineMovementBoostLimit = false;
	@Entry(min = 0.05, max = 1)
	public static float adrenalineSpeedMultiplier = 0.05F;
	@Entry
	public static boolean cursePatch = false;
	@Entry
	public static boolean horseshoes = false;
	@Entry
	public static boolean airMobilityEnchantsWorkWhenUsingElytra = false;
	@Entry(category = "client", min = 15)
	public static double maxBrimstoneSize = 50; // visual
	@Entry(category = "client")
	public static BrimstoneVisuals brimstoneVisuals = BrimstoneVisuals.DEFAULT; // visual
	public enum BrimstoneVisuals {
		VERY_VERY_LOW, VERY_LOW, LOW, DEFAULT, HIGH, VERY_HIGH, VERY_VERY_HIGH
	}
	@Entry(category = "client")
	public static boolean berserkColorTint = true;
	@Entry(category = "client")
	public static Color astralPlaneInsanityBarColor = Color.PURPLE;
	@Entry(category = "client")
	public static Style astralPlaneInsanityBarStyle = Style.PROGRESS;

    public static int encode() {
		String encoding = "I can put whatever I want here"
				+ unboundItems
				+ orbitalBrimstoneExplosionPower
				+ orbitalBrimstoneGoesThroughWalls
				+ unboundEnchantments
				+ noCrossbowCooldown
				+ brimstoneSelfDamage
				+ maxBrimstoneDamage
				+ allCrossbowsHaveMultishot
				+ superQuickChargeCrossbow
				+ sustainExtraDamageWhileSliding
				+ shouldDealSlamDamage
				+ slamSelfDamage
				+ slideImpactDamage
				+ veilUsersAlwaysInvisible
				+ astralVeil
				+ perceptionUsersGetESP
				+ amphibiousAirSwimming
				+ amphibiousExtendedWaterForever
				+ infiniteGale
				+ infiniteDash
				+ infiniteStrafe
				+ airbending
				+ assimilationInhaleFood
				+ noBerserkDamageCap
				+ scatterProjectileMultiplier
				+ wardenspineYeets
				+ noBuryCooldown
				+ canBuryEverything
				+ frostbiteTridentFreezesWater
				+ bouncyChargeSpeed
				+ bouncyJumpMultiplier
				+ disarmStealsPlayerItems
				+ leechForever
				+ leechInterval
				+ strongerChaosEffects
				+ instantChargeDelay
				+ delayFloatTime
				+ homingRadius
				+ grapplePullEntityMultiplier
				+ grapplePullUserMultiplier
				+ impactFallsSlightlyFaster
				+ loyaltyWarpTridentsReturnFaster
				+ infiniteFrostbite
				+ infiniteFireAspect
				+ innateEfficiency
				+ adrenalineSpeedMultiplier
				+ cursePatch
				+ horseshoes
				+ airMobilityEnchantsWorkWhenUsingElytra;
		return encoding.hashCode();
	}

	public enum Color {
		PINK("pink"),
		BLUE("blue"),
		RED("red"),
		GREEN("green"),
		YELLOW("yellow"),
		PURPLE("purple"),
		WHITE("white");

		private final String name;

		Color(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}

	public enum Style {
		PROGRESS("progress"),
		NOTCHED_6("notched_6"),
		NOTCHED_10("notched_10"),
		NOTCHED_12("notched_12"),
		NOTCHED_20("notched_20");

		private final String name;

		Style(String name) {
			this.name = name;
		}

		public String getName() {
			return this.name;
		}
	}
}
