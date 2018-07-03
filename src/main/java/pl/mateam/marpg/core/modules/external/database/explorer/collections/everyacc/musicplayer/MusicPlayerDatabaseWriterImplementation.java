package pl.mateam.marpg.core.modules.external.database.explorer.collections.everyacc.musicplayer;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.musicplayer.MusicPlayerDatabaseWriter;
import pl.mateam.marpg.core.internal.hardcoded.database.everyacc.MusicPlayerHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseWriter;

public class MusicPlayerDatabaseWriterImplementation extends AbstractDatabaseWriter implements MusicPlayerDatabaseWriter {
	public MusicPlayerDatabaseWriterImplementation(Document document) 	{	super(document);	}

	@Override public void setMuted(boolean newValue) 	{	document.put(MusicPlayerHardcodedNames.MUTED, newValue);	}
}
