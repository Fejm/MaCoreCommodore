package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.DatabaseCollectionExplorer;

public interface EveryAccountDatabaseExplorer extends DatabaseCollectionExplorer {	
	EveryAccountDatabaseReader getReader(String playername);	//Reader should be checked by using isValid() method before use!
	EveryAccountDatabaseWriter getWriter(Document document);
}
