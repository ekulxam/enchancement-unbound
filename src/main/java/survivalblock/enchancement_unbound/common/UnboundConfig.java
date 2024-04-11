/*
 * All Rights Reserved (c) MoriyaShiine
 */

package survivalblock.enchancement_unbound.common;

import eu.midnightdust.lib.config.MidnightConfig;


public class UnboundConfig extends MidnightConfig {

	// set to regular enchancement defaults
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
	@Entry(min = 0, max = Long.MAX_VALUE)
	public static double slideImpactDamage = 4;
	@Entry
	public static boolean veilUsersAlwaysInvisible = false;
	@Entry
	public static boolean perceptionUsersGetESP = false;
	@Entry
	public static boolean amphibiousAirSwimming = false;
	@Entry
	public static boolean infiniteGaleJumps = false;
	@Entry
	public static boolean dashChargeInAir = false;
	@Entry
	public static boolean infiniteStrafe = false;
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
	public static BouncyChargeSpeed bouncyChargeSpeed = BouncyChargeSpeed.DEFAULT;
	public enum BouncyChargeSpeed {
		DEFAULT, FAST, VERY_FAST, VERY_VERY_FAST, INSTANT
	}
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
	@Entry(category = "client", min = 15)
	public static double maxBrimstoneSize = 50; // visual
	@Entry(category = "client")
	public static BrimstoneVisuals brimstoneVisuals = BrimstoneVisuals.DEFAULT; // visual
	public enum BrimstoneVisuals {
		VERY_VERY_LOW, VERY_LOW, LOW, DEFAULT, HIGH, VERY_HIGH, VERY_VERY_HIGH
	}

    public static int encode() {
		String encoding = "I can put whatever I want here"
				+ noCrossbowCooldown
				+ brimstoneSelfDamage
				+ maxBrimstoneDamage
				+ allCrossbowsHaveMultishot
				+ superQuickChargeCrossbow
				+ sustainExtraDamageWhileSliding
				+ shouldDealSlamDamage
				+ slideImpactDamage
				+ veilUsersAlwaysInvisible
				+ perceptionUsersGetESP
				+ amphibiousAirSwimming
				+ infiniteGaleJumps
				+ dashChargeInAir
				+ infiniteStrafe
				+ assimilationInhaleFood
				+ noBerserkDamageCap
				+ scatterProjectileMultiplier
				+ wardenspineYeets
				+ noBuryCooldown
				+ canBuryEverything
				+ bouncyChargeSpeed
				+ disarmStealsPlayerItems
				+ leechForever
				+ leechInterval
				+ strongerChaosEffects
				+ instantChargeDelay;
		return encoding.hashCode();
	}
}
