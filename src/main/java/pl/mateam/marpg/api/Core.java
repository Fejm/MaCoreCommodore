package pl.mateam.marpg.api;

import pl.mateam.marpg.api.regular.modules.Files;
import pl.mateam.marpg.api.regular.modules.Server;
import pl.mateam.marpg.api.regular.modules.sub.database.Database;
import pl.mateam.marpg.api.utils.DeveloperUtils.ReflectiveField;

public class Core {
	@ReflectiveField(purpose = "initialize commodore")		//Do not change this flag. It makes Commodore know how to detect the field below.
	private static final Commodore commodoreCore = null;  	//This field is overwritten on engine startup using reflection system.
	
	/*
	 * Allows communication with implementation of Commodore without using public methods
	 * (for example to register add-ons - there shouldn't be a method to do it manually)
	 * Do not use in your add-ons!
	 */
	static Commodore communicate()					{	return commodoreCore;							}
	
	public static Database getDatabase()			{	return commodoreCore.getDatabase();				}
	public static Files getFiles()					{	return commodoreCore.getFiles();				}
	public static Server getServer()				{	return commodoreCore.getServer();				}
}
