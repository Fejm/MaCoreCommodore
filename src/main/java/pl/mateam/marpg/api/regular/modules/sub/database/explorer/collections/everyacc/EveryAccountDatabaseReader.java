package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.DatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.musicplayer.MusicPlayerDatabaseReader;

public interface EveryAccountDatabaseReader extends DatabaseReader {
	MusicPlayerDatabaseReader getMusicPlayer();
}
