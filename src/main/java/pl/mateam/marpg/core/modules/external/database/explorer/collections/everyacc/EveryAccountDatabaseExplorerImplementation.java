package pl.mateam.marpg.core.modules.external.database.explorer.collections.everyacc;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.EveryAccountDatabaseExplorer;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.EveryAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.EveryAccountDatabaseWriter;
import pl.mateam.marpg.core.internal.hardcoded.database.EveryAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.GenericDatabaseExplorerImplementation;

public class EveryAccountDatabaseExplorerImplementation extends GenericDatabaseExplorerImplementation implements EveryAccountDatabaseExplorer {
	@Override protected String getCollectionName() 					{	return EveryAccountEntityCollectionHardcodedNames.COLLECTION_NAME;	}
	@Override protected String getCollectionIdentificatorName() 	{	return EveryAccountEntityCollectionHardcodedNames.IDENTIFICATOR;	}

	@Override public EveryAccountDatabaseReader getReader(String playername) 	{	return new EveryAccountDatabaseReaderImplementation(playername);	}
	@Override public EveryAccountDatabaseWriter getWriter(Document document) 	{	return new EveryAccountDatabaseWriterImplementation(document);		}
}
