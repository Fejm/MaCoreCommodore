package pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.globalban;

import java.util.Date;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.globalban.GlobalBanDatabaseReader;
import pl.mateam.marpg.core.internal.hardcoded.database.players.GlobalBanHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseReader;

public class GlobalBanDatabaseReaderImplementation extends AbstractDatabaseReader implements GlobalBanDatabaseReader {
	public GlobalBanDatabaseReaderImplementation(Object object) 	{	super(object);	}

	@Override public Date getBannedDate() 		{	return document.getDate(GlobalBanHardcodedNames.DATE);			}
	@Override public Date getExpirationDate() 	{	return document.getDate(GlobalBanHardcodedNames.EXPIRATION);	}
	@Override public String getWhoBanned() 		{	return document.getString(GlobalBanHardcodedNames.WHOBANNED);	}
	@Override public String getReason() 		{	return document.getString(GlobalBanHardcodedNames.REASON);		}
}
