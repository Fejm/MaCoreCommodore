package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.characterclass;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.characterclass.inventory.InventoryDatabaseReader;

public interface CharacterClassDatabaseReader {
	InventoryDatabaseReader getInventory();
	
	int getLevel();
	float getExperience();
	double getHealth();
	int getEnergy();
	int getMoney();
	long getTimePlayed();
}
