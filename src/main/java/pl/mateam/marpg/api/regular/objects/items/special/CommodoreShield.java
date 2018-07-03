package pl.mateam.marpg.api.regular.objects.items.special;

import pl.mateam.marpg.api.regular.objects.items.CommodoreSpecialItem;

public interface CommodoreShield extends CommodoreSpecialItem {
	public static interface ShieldEffectiveStats {
		double getBlockRate();
	}
	
	ShieldEffectiveStats getEffectiveStats();
	double getBlockMultiplier();
}
