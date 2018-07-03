package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.globalban;

import java.util.Date;

public interface GlobalBanDatabaseReader {
	Date getBannedDate();
	Date getExpirationDate();
	String getWhoBanned();
	String getReason();
}
