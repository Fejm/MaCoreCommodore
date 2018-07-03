package pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.globalban;

import java.util.Date;

import org.bson.Document;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.globalban.GlobalBanDatabaseWriter;
import pl.mateam.marpg.core.internal.hardcoded.database.players.GlobalBanHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseWriter;

public class GlobalBanDatabaseWriterImplementation extends AbstractDatabaseWriter implements GlobalBanDatabaseWriter {
	public GlobalBanDatabaseWriterImplementation(Document document) 	{	super(document);	}

	@Override public void setBannedDate(Date newValue) 		{	document.put(GlobalBanHardcodedNames.DATE, newValue);		}
	@Override public void setExpirationDate(Date newValue) 	{	document.put(GlobalBanHardcodedNames.EXPIRATION, newValue);	}
	@Override public void setWhoBanned(String newValue) 	{	document.put(GlobalBanHardcodedNames.WHOBANNED, newValue);	}
	@Override public void setReason(String newValue)		{	document.put(GlobalBanHardcodedNames.REASON, newValue);		}
}
