package pl.mateam.marpg.core.modules.external.database.explorer.collections.gamemasteracc;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.gamemasteracc.GamemasterAccountDatabaseReader;
import pl.mateam.marpg.core.internal.hardcoded.database.GamemasterAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.internal.utils.DatabaseUtils;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseReader;

public class GamemasterAccountDatabaseReaderImplementation extends AbstractDatabaseReader implements GamemasterAccountDatabaseReader {
	public GamemasterAccountDatabaseReaderImplementation(String playername) 	{
		super(DatabaseUtils.getUsingSingleCriteria(
				GamemasterAccountEntityCollectionHardcodedNames.COLLECTION_NAME,
				GamemasterAccountEntityCollectionHardcodedNames.IDENTIFICATOR,
				playername));
	}
	
	@Override public String getLatestIP() 	{	return document.getString(GamemasterAccountEntityCollectionHardcodedNames.LATEST_IP);	}
}
