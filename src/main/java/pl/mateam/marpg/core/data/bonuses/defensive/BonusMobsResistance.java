package pl.mateam.marpg.core.data.bonuses.defensive;

import pl.mateam.marpg.api.regular.classes.CommodoreBonus.CommodoreDefensiveBonus;
import pl.mateam.marpg.api.regular.enums.nbt.NBT_DefaultBonus;

public class BonusMobsResistance extends CommodoreDefensiveBonus {
	@Override public String getNBTkey() 			{	return NBT_DefaultBonus.RESISTANCE_MOBS.getKey();	}
	@Override public String getLoreDescription() 	{	return "Odporność na potwory";						}
}
