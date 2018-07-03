package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.DatabaseCollectionExplorer;

public interface PlayerAccountDatabaseExplorer extends DatabaseCollectionExplorer {
	PlayerAccountDatabaseReader getReader(String playername);	//Reader should be checked by using isValid() method before use!
	PlayerAccountDatabaseWriter getWriter(Document document);
}
