package pl.mateam.marpg.api.regular.objects.users;

import org.bukkit.event.Event;

import pl.mateam.marpg.api.regular.enums.nbt.NBT_DefaultBonus;
import pl.mateam.marpg.api.regular.enums.players.CharacterClass;

public interface ExtendedPlayer extends PlayerBasedUser {
	CharacterClass getCharacterClass();
	int getMoney();
	void addMoney(int amount);
	
	double getBonusRawValue(NBT_DefaultBonus bonusKey);
	double getBonusTemporaryValue(NBT_DefaultBonus bonusKey);
	double getBonusValue(NBT_DefaultBonus bonusKey);
	double getBonusRawValue(String bonusKey);
	double getBonusTemporaryValue(String bonusKey);
	double getBonusValue(String bonusKey);

	double getDamageBase();
	double getRandomDamage();
	double getSkillDamageBase();
	int getDamageAmplitude();

	int getDefence();
	double getBlockingRate();
	int getHpRegeneration();
	int getEpRegeneration();

	boolean isInBattleMode();
	void switchToBattleMode();
	void refreshHealthBar();
	
	//Do NOT use bukkit methods to change these values!
	//Below methods are responsible for refreshing bars.
	void changeHPLevel(double value);
	void changeEPLevel(double value);

	boolean canInterract(String playername);
	boolean hasProposedPvP(String playername);
	boolean hasProposedTrade(String playername);
	
	ExtendedPlayer getPvPOpponent();
	boolean hasPvPStartedYet();
		
	void addObserver(ExtendedPlayer observer, String observingModuleRemoteName);
	void removeObserver(ExtendedPlayer observer, String observingModuleRemoteName);

	void addLocalObserver(Class<? extends Event> observedEvent, String observingModuleRemoteName);
	void removeLocalObserver(Class<? extends Event> observedEvent, String observingModuleRemoteName);
}
