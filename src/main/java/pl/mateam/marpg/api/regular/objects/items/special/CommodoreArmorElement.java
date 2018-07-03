package pl.mateam.marpg.api.regular.objects.items.special;

import pl.mateam.marpg.api.regular.objects.items.CommodoreSpecialItem;

public interface CommodoreArmorElement extends CommodoreSpecialItem {
	public static interface ArmorElementEffectiveStats {
		int getDefence();
	}
	
	ArmorElementEffectiveStats getEffectiveStats();
	double getDefenceMultiplier();
}
