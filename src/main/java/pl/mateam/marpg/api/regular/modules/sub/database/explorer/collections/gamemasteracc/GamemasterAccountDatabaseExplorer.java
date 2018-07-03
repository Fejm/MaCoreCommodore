package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.gamemasteracc;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.DatabaseCollectionExplorer;

public interface GamemasterAccountDatabaseExplorer extends DatabaseCollectionExplorer {
	GamemasterAccountDatabaseReader getReader(String playername);	//Reader should be checked by using isValid() method before use!
	GamemasterAccountDatabaseWriter getWriter(Document document);
}
