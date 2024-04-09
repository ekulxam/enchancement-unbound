/*
 * All Rights Reserved (c) MoriyaShiine
 */

package survivalblock.enchancement_unbound.common;

import eu.midnightdust.lib.config.MidnightConfig;


public class UnboundConfig extends MidnightConfig {

	@Entry
	public static boolean noCrossbowCooldown = true;
	@Entry
	public static boolean brimstoneSelfDamage = false;
	@Entry(min = 50)
	public static double maxBrimstoneDamage = 75;
	@Entry
	public static boolean allCrossbowsHaveMultishot = true;
	@Entry
	public static boolean superQuickChargeCrossbow = false;
	@Entry
	public static boolean sustainExtraDamageWhileSliding = false;
	@Entry
	public static boolean shouldDealSlamDamage = true;
	@Entry(min = 0, max = Long.MAX_VALUE)
	public static double slideImpactDamage = 4;
	@Entry
	public static boolean veilUsersAlwaysInvisible = true;
	@Entry
	public static boolean perceptionUsersGetESP = true;
	@Entry
	public static boolean amphibiousAirSwimming = true;
	@Entry
	public static boolean infiniteGaleJumps = false;
	@Entry
	public static boolean dashChargeInAir = false;
	@Entry
	public static boolean infiniteStrafe = false;
	@Entry
	public static boolean assimilationInhaleFood = false;
	@Entry
	public static boolean noBerserkDamageCap = true;
	@Entry(min = 0.1, max = 10)
	public static float scatterProjectileMultiplier = 1;
	@Entry
	public static boolean wardenspineYeets = true;
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
				+ wardenspineYeets;
		return encoding.hashCode();
	}
}
