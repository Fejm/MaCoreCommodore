package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.gamemasteracc;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.DatabaseReader;


public interface GamemasterAccountDatabaseReader extends DatabaseReader {	
	String getLatestIP();
}
