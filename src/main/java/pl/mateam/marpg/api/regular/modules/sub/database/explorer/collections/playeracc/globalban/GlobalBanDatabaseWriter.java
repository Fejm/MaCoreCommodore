package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.globalban;

import java.util.Date;

public interface GlobalBanDatabaseWriter {
	void setBannedDate(Date newValue);
	void setExpirationDate(Date newValue);
	void setWhoBanned(String newValue);
	void setReason(String newValue);
}
