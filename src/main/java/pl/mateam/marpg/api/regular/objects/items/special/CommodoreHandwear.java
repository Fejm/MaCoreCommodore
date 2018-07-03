package pl.mateam.marpg.api.regular.objects.items.special;

import pl.mateam.marpg.api.regular.objects.items.CommodoreSpecialItem;

public interface CommodoreHandwear extends CommodoreSpecialItem {
	public static interface HandwearEffectiveStats {
		//Returns stats corresponding to item's level, tier and upgradement
		double getMinimalDamage();
		double getMaximalDamage();
		double getAverageDamage();
		double getRandomDamage();
		double getFinalSkillDamageMultiplier();	//Returns -1 if using skills is impossible with this item
	}
	
	HandwearEffectiveStats getEffectiveStats();
	double getDamageMultiplier();
	double getSkillDamageMultiplier();			//Returns -1 if using skills is impossible with this item
	double getChanceToMineSuccesfully();		//Returns -1 if item is unable to mine resources
	int getDamageAmplitudePercent();
}
