package pl.mateam.marpg.api.regular.objects.items;

import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.regular.enums.items.Tier;
import pl.mateam.marpg.api.regular.enums.nbt.NBT_DefaultBonus;
import pl.mateam.marpg.api.regular.enums.players.CharacterClass;

public interface CommodoreSpecialItem extends CommodoreItem {
	ItemStack getEffectiveAppearance(CharacterClass playerClass, int playerLevel, float colorValue);
	boolean hasProperClass(CharacterClass playerClass);
	void allowUsingToClass(CharacterClass characterClass);
	void denyUsingToClass(CharacterClass characterClass);
	int getItemLevel();
	boolean isBonusable();
	void clearBonuses();
	void setBonusable(boolean bonusable);
	void changeBonuses(int boost);							//No effect if isBonusable returns false
	void setBonusValue(NBT_DefaultBonus bonus, int value);	//Value will be adjusted to maximal value of bonus in item
	void setBonusValue(String bonusName, int value);		//Value will be adjusted to maximal value of bonus in item
	double getOffensiveBonusChance();						//Returns -1 if isBonusable returns false
	double getDefensiveBonusChance();						//Returns -1 if isBonusable returns false
	double getSpecialBonusChance();							//Returns -1 if isBonusable returns false
	Tier getTier();											//Returns null if item is non-tierable
	void setTier(Tier newTier);								//No effect if item is non-tierable
	void setRandomTier(int tierBonus);						//No effect if item is non-tierable
	int getUpgradementLevel();								//Returns -1 if item is non-upgradeable
	void setUpgradementLevel(int newLevel);					//No effect if item is non-upgradeable
}
