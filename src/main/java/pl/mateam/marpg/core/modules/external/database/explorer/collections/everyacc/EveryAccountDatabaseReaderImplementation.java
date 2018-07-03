package pl.mateam.marpg.core.modules.external.database.explorer.collections.everyacc;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.EveryAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.musicplayer.MusicPlayerDatabaseReader;
import pl.mateam.marpg.core.internal.hardcoded.database.EveryAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.internal.utils.DatabaseUtils;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseReader;
import pl.mateam.marpg.core.modules.external.database.explorer.collections.everyacc.musicplayer.MusicPlayerDatabaseReaderImplementation;

public class EveryAccountDatabaseReaderImplementation extends AbstractDatabaseReader implements EveryAccountDatabaseReader {
	public EveryAccountDatabaseReaderImplementation(String playername) 	{
		super(DatabaseUtils.getUsingSingleCriteria(
				EveryAccountEntityCollectionHardcodedNames.COLLECTION_NAME,
				EveryAccountEntityCollectionHardcodedNames.IDENTIFICATOR,
				playername));
	}

	@Override public MusicPlayerDatabaseReader getMusicPlayer()	 {
		return new MusicPlayerDatabaseReaderImplementation(document.get(EveryAccountEntityCollectionHardcodedNames.MUSIC_PLAYER));
	}
}
