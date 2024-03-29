/*
 * All Rights Reserved (c) MoriyaShiine
 */

package survivalblock.enchancement_unbound.common;

import eu.midnightdust.lib.config.MidnightConfig;


public class UnboundConfig extends MidnightConfig {

	@Entry(category = "client", min = 15)
	public static double maxBrimstoneSize = 50; // visual
	@Entry(category = "client")
	public static BrimstoneVisuals brimstoneVisuals = BrimstoneVisuals.DEFAULT; // visual
	public enum BrimstoneVisuals {
		VERY_VERY_LOW, VERY_LOW, LOW, DEFAULT, HIGH, VERY_HIGH, VERY_VERY_HIGH
	}

	@Entry
	public static boolean noCrossbowCooldown = true;
	@Entry
	public static boolean brimstoneSelfDamage = false;
	@Entry(min = 50)
	public static double maxBrimstoneDamage = (double) Long.MAX_VALUE;
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
	public static boolean veilEqualsGhost = false;
	@Entry
	public static boolean perceptionUsersGetESP = true;
	@Entry
	public static boolean amphibiousAirSwimming = true;
	public static int encode() {
		String encoding = "I can put whatever I want here" + noCrossbowCooldown + brimstoneSelfDamage + maxBrimstoneDamage + allCrossbowsHaveMultishot + superQuickChargeCrossbow + sustainExtraDamageWhileSliding + shouldDealSlamDamage + slideImpactDamage + veilUsersAlwaysInvisible + perceptionUsersGetESP;
		return encoding.hashCode();
	}
}
