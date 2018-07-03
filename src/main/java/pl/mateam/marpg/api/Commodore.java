package pl.mateam.marpg.api;

import pl.mateam.marpg.api.regular.modules.Files;
import pl.mateam.marpg.api.regular.modules.Server;
import pl.mateam.marpg.api.regular.modules.sub.database.Database;

//Describes functionality of root of MaCoreCommodore
public interface Commodore {

	Database getDatabase();
	Files getFiles();
	Server getServer();
	
	/*
	 * These method are not present on Core class in it's static form as
	 * commodore's add-ons developers shouldn't be able to use such method.
	 * All of the add-ons are detected and managed automatically.
	 */
	boolean startEnabling(CommodoreComponent addOn);					//Returns false if add-on is already enabled
	boolean startDisabling(CommodoreComponent addOn);					//Returns false if add-on is already disabled

	void enablingFinished(CommodoreComponent addOn);
	void disablingFinished(CommodoreComponent addOn);
	void unpackResource(CommodoreComponent component, String relativePath, boolean override);
	void unpackResources(CommodoreComponent addOn, boolean override);
}
