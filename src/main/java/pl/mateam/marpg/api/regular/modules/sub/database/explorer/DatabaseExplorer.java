package pl.mateam.marpg.api.regular.modules.sub.database.explorer;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.EveryAccountDatabaseExplorer;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.gamemasteracc.GamemasterAccountDatabaseExplorer;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseExplorer;

public interface DatabaseExplorer {
	EveryAccountDatabaseExplorer getEveryAccountCollection();
	GamemasterAccountDatabaseExplorer getGamemasterAccountCollection();
	PlayerAccountDatabaseExplorer getPlayerAccountCollection();
}