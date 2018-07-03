package pl.mateam.marpg.core;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.core.modules.external.database.DatabaseManager;
import pl.mateam.marpg.core.objects.users.AnyUserImplementation;

class MaCoreCommodoreShutdowner {
	private final MaCoreCommodoreEngine ENGINE;
	
	public MaCoreCommodoreShutdowner(MaCoreCommodoreEngine engine){
		this.ENGINE = engine;
		
		CoreUtils.io.sendConsoleMessageImportant("-------------------------------");
		CoreUtils.io.sendConsoleMessageImportant("Wyłączanie MaCoreCommodore...");
		CoreUtils.io.sendConsoleMessageImportant("-------------------------------");
		
		//Invoking disabler method manually. Normally this methods triggers automatically on CommodoreAddOn's final onDisable() method.
		CommodoreCore.getHiddenMethods().startDisabling(ENGINE);
		
		CommodoreCore.getHiddenMethods().getHiddenData().isServerDown = true;
		saveConfiguration();
		replenishOres();
		savePlayersInfo();
		closeDatabaseConnection();
		
		CommodoreCore.getHiddenMethods().disablingFinished(ENGINE);
		
		CoreUtils.io.sendConsoleMessageImportant("MaCoreCommodore - zakończono pracę!");
		CoreUtils.io.sendConsoleMessageImportant("-------------------------------");
	}
	
	private void saveConfiguration() {
		CoreUtils.io.sendConsoleMessageImportantWithHighlight("", "Zapisywanie konfiguracji...", "");
		if(Core.getServer().getMotd().doesMotdSaveAutomatically())
			Core.getServer().getMotd().saveCurrentMotdToConfig();

		if(Core.getServer().getMotd().doesHoverMotdSaveAutomatically())
			Core.getServer().getMotd().saveCurrentHoverMotdToConfig();
	}
	
	private void replenishOres() {
		CoreUtils.io.sendConsoleMessageImportantWithHighlight("", "Regenerowanie rud...", "");

		Core.getServer().getWorlds().replenishAllOres(false);
	}
	
	private void savePlayersInfo() 	{
		CoreUtils.io.sendConsoleMessageImportantWithHighlight("", "Zapisywanie danych graczy...", "");

		Core.getServer().getUsers().getAllUsersObjects().forEach(user -> ((AnyUserImplementation) user).hasQuit(null));
	}

	private void closeDatabaseConnection()	{	((DatabaseManager) Core.getDatabase()).sendShutdownSignal();	}
}
