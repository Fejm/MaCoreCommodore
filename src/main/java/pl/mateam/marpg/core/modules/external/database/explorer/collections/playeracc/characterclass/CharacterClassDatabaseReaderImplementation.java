package pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.characterclass;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.characterclass.CharacterClassDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.characterclass.inventory.InventoryDatabaseReader;
import pl.mateam.marpg.core.internal.hardcoded.database.players.CharacterClassHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseReader;
import pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.characterclass.inventory.InventoryDatabaseReaderImplementation;

public class CharacterClassDatabaseReaderImplementation extends AbstractDatabaseReader implements CharacterClassDatabaseReader {
	public CharacterClassDatabaseReaderImplementation(Object object) 	{	super(object);	}

	@Override public InventoryDatabaseReader getInventory() 	{	return new InventoryDatabaseReaderImplementation(document.get(CharacterClassHardcodedNames.ITEMS));	}
	
	@Override public int getLevel() 		{	return document.getInteger(CharacterClassHardcodedNames.LEVEL);							}
	@Override public float getExperience() 	{	return (float) (double) document.getDouble(CharacterClassHardcodedNames.EXPERIENCE);	}
	@Override public double getHealth() 	{	return document.getDouble(CharacterClassHardcodedNames.HEALTH);							}
	@Override public int getEnergy() 		{	return document.getInteger(CharacterClassHardcodedNames.ENERGY);						}
	@Override public int getMoney() 		{	return document.getInteger(CharacterClassHardcodedNames.MONEY);							}
	@Override public long getTimePlayed() 	{	return document.getLong(CharacterClassHardcodedNames.TIME_PLAYED);						}
}
