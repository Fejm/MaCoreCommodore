package pl.mateam.marpg.api.regular.modules;

import pl.mateam.marpg.api.regular.modules.sub.server.BansManager;
import pl.mateam.marpg.api.regular.modules.sub.server.CommandsManager;
import pl.mateam.marpg.api.regular.modules.sub.server.EnvironmentManager;
import pl.mateam.marpg.api.regular.modules.sub.server.ItemsManager;
import pl.mateam.marpg.api.regular.modules.sub.server.MechanicsManager;
import pl.mateam.marpg.api.regular.modules.sub.server.MotdManager;
import pl.mateam.marpg.api.regular.modules.sub.server.OresManager;
import pl.mateam.marpg.api.regular.modules.sub.server.RanksManager;
import pl.mateam.marpg.api.regular.modules.sub.server.SkinsManager;
import pl.mateam.marpg.api.regular.modules.sub.server.SongsManager;
import pl.mateam.marpg.api.regular.modules.sub.server.UsersManager;
import pl.mateam.marpg.api.regular.modules.sub.server.WorldsManager;

public interface Server {
	BansManager getBans();
	CommandsManager getCommands();
	EnvironmentManager getEnvironment();
	ItemsManager getItems();
	MechanicsManager getMechanics();
	MotdManager getMotd();
	OresManager getOres();
	RanksManager getRanks();
	SkinsManager getSkins();
	SongsManager getSongs();
	UsersManager getUsers();
	WorldsManager getWorlds();
}
