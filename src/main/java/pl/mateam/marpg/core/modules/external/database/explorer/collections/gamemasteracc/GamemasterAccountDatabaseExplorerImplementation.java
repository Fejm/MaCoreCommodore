package pl.mateam.marpg.core.modules.external.database.explorer.collections.gamemasteracc;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.gamemasteracc.GamemasterAccountDatabaseExplorer;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.gamemasteracc.GamemasterAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.gamemasteracc.GamemasterAccountDatabaseWriter;
import pl.mateam.marpg.core.internal.hardcoded.database.GamemasterAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.GenericDatabaseExplorerImplementation;

public class GamemasterAccountDatabaseExplorerImplementation extends GenericDatabaseExplorerImplementation implements GamemasterAccountDatabaseExplorer {
	@Override protected String getCollectionName() 					{	return GamemasterAccountEntityCollectionHardcodedNames.COLLECTION_NAME;	}
	@Override protected String getCollectionIdentificatorName() 	{	return GamemasterAccountEntityCollectionHardcodedNames.IDENTIFICATOR;	}
	
	@Override public GamemasterAccountDatabaseReader getReader(String playername) 	{	return new GamemasterAccountDatabaseReaderImplementation(playername);	}
	@Override public GamemasterAccountDatabaseWriter getWriter(Document document)	{	return new GamemasterAccountDatabaseWriterImplementation(document);		}
}
