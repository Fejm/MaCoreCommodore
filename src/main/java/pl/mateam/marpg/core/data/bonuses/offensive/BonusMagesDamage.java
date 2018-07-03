package pl.mateam.marpg.core.data.bonuses.offensive;

import pl.mateam.marpg.api.regular.classes.CommodoreBonus.CommodoreOffensiveBonus;
import pl.mateam.marpg.api.regular.enums.nbt.NBT_DefaultBonus;

public class BonusMagesDamage extends CommodoreOffensiveBonus {
	@Override public String getNBTkey() 			{	return NBT_DefaultBonus.MAGES.getKey();		}
	@Override public String getLoreDescription() 	{	return "Silny przeciwko magom";			}
}
