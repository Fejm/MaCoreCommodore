package pl.mateam.marpg.core.data.bonuses.special;

import pl.mateam.marpg.api.regular.classes.CommodoreBonus.CommodoreSpecialBonus;
import pl.mateam.marpg.api.regular.enums.nbt.NBT_DefaultBonus;

public class BonusCritical extends CommodoreSpecialBonus {
	@Override public String getNBTkey() 			{	return NBT_DefaultBonus.CRITICAL.getKey();		}
	@Override public String getLoreDescription() 	{	return "Szansa na krytyczny cios";			}
}
