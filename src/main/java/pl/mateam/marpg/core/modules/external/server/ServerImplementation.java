package pl.mateam.marpg.core.modules.external.server;

import pl.mateam.marpg.api.regular.modules.Server;
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
import pl.mateam.marpg.core.modules.external.server.sub.BansManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.CommandsManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.EnvironmentManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.ItemsManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.MechanicsManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.MotdManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.OresManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.RanksManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.SkinsManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.SongsManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.UsersManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.WorldsManagerImplementation;

public class ServerImplementation implements Server {
	private final BansManagerImplementation bans = new BansManagerImplementation();
	private final CommandsManagerImplementation commands = new CommandsManagerImplementation();
	private final EnvironmentManagerImplementation environment = new EnvironmentManagerImplementation();
	private final ItemsManagerImplementation items = new ItemsManagerImplementation();
	private final MechanicsManagerImplementation mechanics = new MechanicsManagerImplementation();
	private final MotdManagerImplementation motd = new MotdManagerImplementation();
	private final OresManagerImplementation ores = new OresManagerImplementation();
	private final RanksManagerImplementation ranks = new RanksManagerImplementation();
	private final SkinsManagerImplementation skins = new SkinsManagerImplementation();
	private final SongsManagerImplementation songs = new SongsManagerImplementation();
	private final UsersManagerImplementation users = new UsersManagerImplementation();
	private final WorldsManagerImplementation worlds = new WorldsManagerImplementation();

	@Override public BansManager getBans()					{	return bans;		}
	@Override public CommandsManager getCommands() 			{	return commands;	}
	@Override public EnvironmentManager getEnvironment()	{	return environment;	}
	@Override public ItemsManager getItems()				{	return items;		}
	@Override public MechanicsManager getMechanics()		{	return mechanics;	}
	@Override public MotdManager getMotd() 					{	return motd;		}
	@Override public OresManager getOres()					{	return ores;		}
	@Override public RanksManager getRanks() 				{	return ranks;		}
	@Override public SkinsManager getSkins() 				{	return skins;		}	
	@Override public SongsManager getSongs() 				{	return songs;		}
	@Override public UsersManager getUsers() 				{	return users;		}
	@Override public WorldsManager getWorlds()				{	return worlds;		}
}