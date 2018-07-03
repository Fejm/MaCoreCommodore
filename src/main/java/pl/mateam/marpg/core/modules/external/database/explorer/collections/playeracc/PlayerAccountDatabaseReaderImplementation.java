package pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc;

import pl.mateam.marpg.api.regular.enums.players.Rank;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.characterclass.CharacterClassDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.globalban.GlobalBanDatabaseReader;
import pl.mateam.marpg.core.internal.hardcoded.database.PlayerAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.internal.utils.DatabaseUtils;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseReader;
import pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.characterclass.CharacterClassDatabaseReaderImplementation;
import pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.globalban.GlobalBanDatabaseReaderImplementation;

public class PlayerAccountDatabaseReaderImplementation extends AbstractDatabaseReader implements PlayerAccountDatabaseReader {
	public PlayerAccountDatabaseReaderImplementation(String playername) 	{
		super(DatabaseUtils.getUsingSingleCriteria(
				PlayerAccountEntityCollectionHardcodedNames.COLLECTION_NAME,
				PlayerAccountEntityCollectionHardcodedNames.IDENTIFICATOR,
				playername));
	}

	@Override public String getHashedPassword() 	{	return document.getString(PlayerAccountEntityCollectionHardcodedNames.PASSWORD);			}
	@Override public boolean getIsWoman() 			{	return document.getBoolean(PlayerAccountEntityCollectionHardcodedNames.IS_WOMAN);			}
	@Override public boolean getIsModerator() 		{	return document.getBoolean(PlayerAccountEntityCollectionHardcodedNames.IS_MODERATOR);		}
	@Override public Rank getRank() 				{	return Rank.valueOf(document.getString(PlayerAccountEntityCollectionHardcodedNames.RANK));	}

	@Override public GlobalBanDatabaseReader getBanInfo() 	{	return new GlobalBanDatabaseReaderImplementation(document.get(PlayerAccountEntityCollectionHardcodedNames.BAN_INFO));	}

	@Override public CharacterClassDatabaseReader getWarriorClassInfo() {
		return new CharacterClassDatabaseReaderImplementation(document.get(PlayerAccountEntityCollectionHardcodedNames.CLASS_WARRIOR));
	}

	@Override public CharacterClassDatabaseReader getBarbarianClassInfo() {
		return new CharacterClassDatabaseReaderImplementation(document.get(PlayerAccountEntityCollectionHardcodedNames.CLASS_BARBARIAN));
	}

	@Override public CharacterClassDatabaseReader getMagClassInfo() {
		return new CharacterClassDatabaseReaderImplementation(document.get(PlayerAccountEntityCollectionHardcodedNames.CLASS_MAGE));
	}

	@Override public CharacterClassDatabaseReader getSlayerClassInfo() {
		return new CharacterClassDatabaseReaderImplementation(document.get(PlayerAccountEntityCollectionHardcodedNames.CLASS_SLAYER));
	}
}
