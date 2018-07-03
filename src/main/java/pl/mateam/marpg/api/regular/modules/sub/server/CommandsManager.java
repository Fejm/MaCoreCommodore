package pl.mateam.marpg.api.regular.modules.sub.server;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.regular.classes.commands.CommodoreCommand;

public interface CommandsManager {
	int getCommandsCount(CommodoreComponent component);
	int getRegisteredNamesCount(CommodoreComponent component);
	void unregister(String commandName);
	void register(Class<? extends CommodoreCommand> commandClass);
	void registerAll(CommodoreComponent component);
	void unregisterAll(CommodoreComponent component);
	void addAlias(String commandName, String alias);
}
