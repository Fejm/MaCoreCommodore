package pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseExplorer;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseWriter;
import pl.mateam.marpg.core.internal.hardcoded.database.PlayerAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.GenericDatabaseExplorerImplementation;

public class PlayerAccountDatabaseExplorerImplementation extends GenericDatabaseExplorerImplementation implements PlayerAccountDatabaseExplorer {
	@Override protected String getCollectionName() 					{	return PlayerAccountEntityCollectionHardcodedNames.COLLECTION_NAME;	}
	@Override protected String getCollectionIdentificatorName() 	{	return PlayerAccountEntityCollectionHardcodedNames.IDENTIFICATOR;	}
	
	@Override public PlayerAccountDatabaseReader getReader(String playername) 	{	return new PlayerAccountDatabaseReaderImplementation(playername);	}
	@Override public PlayerAccountDatabaseWriter getWriter(Document document) 	{	return new PlayerAccountDatabaseWriterImplementation(document);		}
}
