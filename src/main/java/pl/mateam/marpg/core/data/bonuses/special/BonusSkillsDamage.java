package pl.mateam.marpg.core.data.bonuses.special;

import pl.mateam.marpg.api.regular.classes.CommodoreBonus.CommodoreSpecialBonus;
import pl.mateam.marpg.api.regular.enums.nbt.NBT_DefaultBonus;

public class BonusSkillsDamage extends CommodoreSpecialBonus {
	@Override public String getNBTkey() 			{	return NBT_DefaultBonus.SKILLSBONUS.getKey();		}
	@Override public String getLoreDescription() 	{	return "Bonus do umiejêtnoœci";					}
}
