package pl.mateam.marpg.core.modules.external.database.explorer;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.DatabaseExplorer;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.EveryAccountDatabaseExplorer;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.gamemasteracc.GamemasterAccountDatabaseExplorer;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseExplorer;
import pl.mateam.marpg.core.modules.external.database.explorer.collections.everyacc.EveryAccountDatabaseExplorerImplementation;
import pl.mateam.marpg.core.modules.external.database.explorer.collections.gamemasteracc.GamemasterAccountDatabaseExplorerImplementation;
import pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.PlayerAccountDatabaseExplorerImplementation;

public class DatabaseExplorerImplementation implements DatabaseExplorer {
	private final EveryAccountDatabaseExplorerImplementation everyAccount = new EveryAccountDatabaseExplorerImplementation();
	private final GamemasterAccountDatabaseExplorerImplementation gamemasterAccount = new GamemasterAccountDatabaseExplorerImplementation();
	private final PlayerAccountDatabaseExplorerImplementation playerAccount = new PlayerAccountDatabaseExplorerImplementation();
	
	@Override public EveryAccountDatabaseExplorer getEveryAccountCollection() 				{	return everyAccount;		}
	@Override public GamemasterAccountDatabaseExplorer getGamemasterAccountCollection() 	{	return gamemasterAccount;	}
	@Override public PlayerAccountDatabaseExplorer getPlayerAccountCollection()				{	return playerAccount;		}
}
