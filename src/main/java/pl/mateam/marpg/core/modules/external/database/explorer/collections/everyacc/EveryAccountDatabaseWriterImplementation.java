package pl.mateam.marpg.core.modules.external.database.explorer.collections.everyacc;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.EveryAccountDatabaseWriter;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.musicplayer.MusicPlayerDatabaseWriter;
import pl.mateam.marpg.core.internal.hardcoded.database.EveryAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseWriter;
import pl.mateam.marpg.core.modules.external.database.explorer.collections.everyacc.musicplayer.MusicPlayerDatabaseWriterImplementation;

public class EveryAccountDatabaseWriterImplementation extends AbstractDatabaseWriter implements EveryAccountDatabaseWriter {
	public EveryAccountDatabaseWriterImplementation(Document document) 	{	super(document);	}

	@Override public MusicPlayerDatabaseWriter getMusicPlayer() {
		return getSubWriter(MusicPlayerDatabaseWriterImplementation::new, EveryAccountEntityCollectionHardcodedNames.MUSIC_PLAYER);
	}
}
