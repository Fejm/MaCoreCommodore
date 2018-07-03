package pl.mateam.marpg.api.regular.objects.items.special;

import pl.mateam.marpg.api.regular.objects.items.CommodoreSpecialItem;

public interface CommodoreJewelleryElement extends CommodoreSpecialItem {
	public static interface JewelleryEffectiveStats {
		int getRegenerationRate();
	}
	
	JewelleryEffectiveStats getEffectiveStats();
	double getRegenerationMultiplier();
	boolean isNecklace();
}
