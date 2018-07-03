package pl.mateam.marpg.core.modules.external.database.explorer.collections.everyacc.musicplayer;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.musicplayer.MusicPlayerDatabaseReader;
import pl.mateam.marpg.core.internal.hardcoded.database.everyacc.MusicPlayerHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseReader;

public class MusicPlayerDatabaseReaderImplementation extends AbstractDatabaseReader implements MusicPlayerDatabaseReader {
	public MusicPlayerDatabaseReaderImplementation(Object object) 	{	super(object);	}

	@Override public boolean getIsMuted() 	{	return document.getBoolean(MusicPlayerHardcodedNames.MUTED);	}
}
