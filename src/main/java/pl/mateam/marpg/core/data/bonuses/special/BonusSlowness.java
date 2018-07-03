package pl.mateam.marpg.core.data.bonuses.special;

import pl.mateam.marpg.api.regular.classes.CommodoreBonus.CommodoreSpecialBonus;
import pl.mateam.marpg.api.regular.enums.nbt.NBT_DefaultBonus;

public class BonusSlowness extends CommodoreSpecialBonus {
	@Override public String getNBTkey() 			{	return NBT_DefaultBonus.SLOWNESS.getKey();		}
	@Override public String getLoreDescription() 	{	return "Szansa na spowolnienie";			}
}
