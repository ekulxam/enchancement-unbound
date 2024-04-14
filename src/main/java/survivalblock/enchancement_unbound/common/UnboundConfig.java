/*
 * All Rights Reserved (c) MoriyaShiine
 */

package survivalblock.enchancement_unbound.common;

import eu.midnightdust.lib.config.MidnightConfig;


public class UnboundConfig extends MidnightConfig {

	@Comment public static Comment welcome;
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
	public static boolean astralVeil = false;
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
	@Entry(min = 1, max = 100)
	public static int shieldboardBaseSpeed = 14;
	@Entry
	public static boolean aegisUseDisables = true;
	@Entry(min = 1, max = 1000)
	public static int expulsionMultiplier = 2; // lag
	@Entry(min = 1)
	public static float grapplePullEntityMultiplier = 1; // lag
	@Entry(min = 1)
	public static float grapplePullUserMultiplier = 1; // lag
	@Entry
	public static boolean shouldHaveNightVision = true;

	@Entry(category = "client", min = 15)
	public static double maxBrimstoneSize = 50; // visual
	@Entry(category = "client")
	public static BrimstoneVisuals brimstoneVisuals = BrimstoneVisuals.DEFAULT; // visual
	public enum BrimstoneVisuals {
		VERY_VERY_LOW, VERY_LOW, LOW, DEFAULT, HIGH, VERY_HIGH, VERY_VERY_HIGH
	}
	@Entry(category = "client")
	public static boolean projectedShieldsRenderOutwards = true;

    public static int encode() {
		String encoding = "I can put whatever I want here"
				+ welcome
				+ noCrossbowCooldown
				+ brimstoneSelfDamage
				+ maxBrimstoneDamage
				+ allCrossbowsHaveMultishot
				+ superQuickChargeCrossbow
				+ sustainExtraDamageWhileSliding
				+ shouldDealSlamDamage
				+ slideImpactDamage
				+ veilUsersAlwaysInvisible
				+ astralVeil
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
