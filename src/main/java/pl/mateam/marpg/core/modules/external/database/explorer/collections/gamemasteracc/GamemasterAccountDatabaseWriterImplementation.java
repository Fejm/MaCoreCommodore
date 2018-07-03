package pl.mateam.marpg.core.modules.external.database.explorer.collections.gamemasteracc;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.gamemasteracc.GamemasterAccountDatabaseWriter;
import pl.mateam.marpg.core.internal.hardcoded.database.GamemasterAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseWriter;

public class GamemasterAccountDatabaseWriterImplementation extends AbstractDatabaseWriter implements GamemasterAccountDatabaseWriter {
	public GamemasterAccountDatabaseWriterImplementation(Document document) 	{	super(document);	}

	@Override public void setLatestIP(String newValue) 	{	document.put(GamemasterAccountEntityCollectionHardcodedNames.LATEST_IP, newValue);	}
}
