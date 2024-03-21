/*
 * All Rights Reserved (c) MoriyaShiine
 */

package survivalblock.enchancement_unbound.common;

import eu.midnightdust.lib.config.MidnightConfig;

import java.util.Iterator;


public class UnboundConfig extends MidnightConfig {

	@Entry
	public static boolean noCrossbowCooldown = true;
	@Entry
	public static boolean brimstoneSelfDamage = false;
	@Entry(min = 50)
	public static double maxBrimstoneDamage = Integer.MAX_VALUE;
	@Entry(min = 15)
	public static double maxBrimstoneSize = 50.0;

	public static int encode() {
		String forceString = "";
		String encoding = forceString + noCrossbowCooldown + brimstoneSelfDamage + maxBrimstoneDamage + maxBrimstoneSize;
		return encoding.hashCode();
	}
}
