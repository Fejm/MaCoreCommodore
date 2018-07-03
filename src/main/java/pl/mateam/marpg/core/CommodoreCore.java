package pl.mateam.marpg.core;

import pl.mateam.marpg.api.Commodore;
import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.regular.modules.Files;
import pl.mateam.marpg.api.regular.modules.Server;
import pl.mateam.marpg.api.regular.modules.sub.database.Database;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.modules.external.database.DatabaseManager;
import pl.mateam.marpg.core.modules.external.files.FilesImplementation;
import pl.mateam.marpg.core.modules.external.server.ServerImplementation;
import pl.mateam.marpg.core.modules.internal.AddOnManager;
import pl.mateam.marpg.core.modules.internal.HiddenDataManager;

public class CommodoreCore implements Commodore {
	private static CommodoreCore singleton = null;
	
	//Instance of CommodoreCore is created only once - in MaCoreCommodore's onEnable() method.
	CommodoreCore()		{	singleton = this;	}

	
	/* ------------------------------------------------------------------------------
	 * Only classes inside MaCoreCommodore knows about this class and methods below.
	 * ------------------------------------------------------------------------------ */

	private final AddOnManager addons = new AddOnManager();
	private final DatabaseManager database = new DatabaseManager();
	private final HiddenDataManager hiddenDataManager = new HiddenDataManager();

	
	//Provides access to hidden methods listed below
	public static CommodoreCore getHiddenMethods()																{	return singleton;	}
	
	@Secret public HiddenDataManager getHiddenData()															{	return hiddenDataManager;									}

	//These methods are visible in the API, so they have to be set in this class directly
	@Secret public boolean startEnabling(CommodoreComponent component) 											{	return addons.startEnabling(component);						}
	@Secret public boolean startDisabling(CommodoreComponent component) 										{	return addons.startDisabling(component);					}
	@Secret public void enablingFinished(CommodoreComponent component) 											{	addons.enablingFinished(component);							}
	@Secret public void disablingFinished(CommodoreComponent component) 										{	addons.disablingFinished(component);						}
	@Secret public void unpackResources(CommodoreComponent component, boolean override)							{	addons.manageResources(component, override);				}
	@Secret public void unpackResource(CommodoreComponent component, String relativePath, boolean override)		{	addons.manageResource(component, relativePath, override);	}
	

	/* ---------------------------------------------------
	 * Implementation part - addons will use below methods
	 * --------------------------------------------------- */
	
	@Override public Database getDatabase() {
		return database;
	}
	
	private final FilesImplementation files = new FilesImplementation();
	@Override public Files getFiles() 								{	return files;		}
	
	private final ServerImplementation server = new ServerImplementation();
	@Override public Server getServer()								{	return server;		}
}
