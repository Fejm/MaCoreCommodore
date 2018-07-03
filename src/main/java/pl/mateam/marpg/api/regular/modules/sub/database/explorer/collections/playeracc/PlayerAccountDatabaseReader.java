package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc;

import pl.mateam.marpg.api.regular.enums.players.Rank;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.DatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.characterclass.CharacterClassDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.globalban.GlobalBanDatabaseReader;

public interface PlayerAccountDatabaseReader extends DatabaseReader {
	GlobalBanDatabaseReader getBanInfo();

	CharacterClassDatabaseReader getWarriorClassInfo();
	CharacterClassDatabaseReader getBarbarianClassInfo();
	CharacterClassDatabaseReader getMagClassInfo();
	CharacterClassDatabaseReader getSlayerClassInfo();
	
	String getHashedPassword();
	boolean getIsWoman();
	boolean getIsModerator();
	Rank getRank();
}
