package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc;

import pl.mateam.marpg.api.regular.enums.players.Rank;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.globalban.GlobalBanDatabaseWriter;

public interface PlayerAccountDatabaseWriter {
	GlobalBanDatabaseWriter getBanInfo();
	
	void setHashedPassword(String newValue);
	void setIsWoman(boolean newValue);
	void setIsModerator(boolean newValue);
	void setRank(Rank newValue);
}
