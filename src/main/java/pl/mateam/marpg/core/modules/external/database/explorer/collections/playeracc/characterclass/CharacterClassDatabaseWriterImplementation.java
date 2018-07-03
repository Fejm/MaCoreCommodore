package pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.characterclass;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.characterclass.CharacterClassDatabaseWriter;
import pl.mateam.marpg.core.internal.hardcoded.database.players.CharacterClassHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseWriter;

public class CharacterClassDatabaseWriterImplementation extends AbstractDatabaseWriter implements CharacterClassDatabaseWriter {
	public CharacterClassDatabaseWriterImplementation(Document document)	{	super(document);	}

	@Override public void setLevel(int newValue) 		{	document.put(CharacterClassHardcodedNames.LEVEL, newValue);			}
	@Override public void setExperience(float newValue) {	document.put(CharacterClassHardcodedNames.EXPERIENCE, newValue);	}
	@Override public void setHealth(double newValue) 	{	document.put(CharacterClassHardcodedNames.HEALTH, newValue);		}
	@Override public void setEnergy(int newValue) 		{	document.put(CharacterClassHardcodedNames.ENERGY, newValue);		}
	@Override public void setMoney(int newValue) 		{	document.put(CharacterClassHardcodedNames.MONEY, newValue);			}
	@Override public void setTimePlayed(long newValue) 	{	document.put(CharacterClassHardcodedNames.TIME_PLAYED, newValue);	}
}
