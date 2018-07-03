package pl.mateam.marpg.core.modules.external.server.sub;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.CommodoreComponent.CommodoreCommands;
import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.commands.CommodoreCommand;
import pl.mateam.marpg.api.regular.classes.commands.CommodoreCommand.Aliases;
import pl.mateam.marpg.api.regular.classes.commands.CommodoreCommand.CommandName;
import pl.mateam.marpg.api.regular.modules.sub.server.CommandsManager;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;

public class CommandsManagerImplementation implements CommandsManager {
	private static class ErrorsInfo{
		private boolean errorOccured;
		private boolean annotationErrorOccured;
		private boolean constructorErrorOccured;
	}
	
	private static class CommandHandler extends BukkitCommand {
		private Class<? extends CommodoreCommand> handlingClass;
		
		protected CommandHandler(Class<? extends CommodoreCommand> handlingClass, String name, List<String> aliases) {
			super(name);
			this.handlingClass = handlingClass;
			if(aliases != null)
				this.setAliases(aliases);
		}

		@Override public boolean execute(CommandSender sender, String label, String[] args) {
			try {
				CommodoreCommand commandInstance = handlingClass.newInstance();
				if(sender instanceof Player){
					if(Core.getServer().getUsers().getGamemasterObject((Player)sender) == null && !(commandInstance.canBeUsedByModerators())) {
						sender.sendMessage(CoreUtils.chat.getCasualAdminMessage("Zaiste bardzo sprytne m�j m�ody padawanie..."));
						return false;
					}
				} else if (!commandInstance.canBeUsedFromTheConsole()) {
					CoreUtils.io.sendConsoleMessageWarning("Ta komenda nie mo�e by� wykonana z konsoli!");
					return false;
				}
				CoreUtils.developer.overrideReflectiveFinalField(CommodoreCommand.class, commandInstance, "command executor", sender);
				CoreUtils.developer.overrideReflectiveFinalField(CommodoreCommand.class, commandInstance, "command arguments", args);
				commandInstance.trigger();
			} catch (InstantiationException | IllegalAccessException e) {
				e.printStackTrace();
			}
			return false;
		}
	}
	
	private final Map<String, CommodoreComponent> commandRegisterers = new HashMap<>();
	
	//Stores info about all known commands and it's aliases so they can't be overwritten.
	private final Map<String, String> usedNames = new HashMap<>();

	private Class<? extends CommodoreCommand>[] getAssociatedClasses(CommodoreComponent addOn){
		try {
			Method method = addOn.getClass().getDeclaredMethod("turnedOn", (Class<?>[]) null);
			method.setAccessible(true);
			if(method.isAnnotationPresent(CommodoreCommands.class))
				return method.getAnnotation(CommodoreCommands.class).commandClasses();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}
	
	@Override public int getCommandsCount(CommodoreComponent component){
		int count = 0;
		Iterator<CommodoreComponent> iterator = commandRegisterers.values().iterator();
		while(iterator.hasNext()){
			if(iterator.next() == component)
				count++;
		}
		return count;
	}
	
	@Override public int getRegisteredNamesCount(CommodoreComponent component){
		int count = 0;
		Iterator<Entry<String, CommodoreComponent>> iterator = commandRegisterers.entrySet().iterator();
		Map<String, String[]> aliases = Bukkit.getServer().getCommandAliases();
		while(iterator.hasNext()){
			Entry<String, CommodoreComponent> entry = iterator.next();
			if(entry.getValue() == component)
				count += aliases.get(entry.getKey()).length;
		}
		return count;
	}
	

	@Override public void registerAll(CommodoreComponent component){
		unregisterAll(component);
		Class<? extends CommodoreCommand>[] commands = getAssociatedClasses(component);
		if(commands != null){
			ErrorsInfo info = new ErrorsInfo();
			for(Class<? extends CommodoreCommand> commandClass : commands)
				register(component, commandClass, info);
			if(info.annotationErrorOccured)
				CoreUtils.io.sendConsoleMessageWarning("Adnotacja @CommandName jest wymagana jako nag��wek klasy komendy.");
			if(info.constructorErrorOccured)
				CoreUtils.io.sendConsoleMessageWarning("Klasy komend nie mog� definiowa� w�asnego konstruktora!");
		}
	}
	
	@Override public void register(Class<? extends CommodoreCommand> commandClass){
		register(MaCoreCommodoreEngine.getReference(), commandClass, null);
	}
	
	private void register(CommodoreComponent component, Class<? extends CommodoreCommand> commandClass, ErrorsInfo errorsInfo){
		boolean single = errorsInfo == null;
		errorsInfo = errorsInfo == null? new ErrorsInfo() : errorsInfo;
		try {
			commandClass.newInstance();
			if(commandClass.isAnnotationPresent(CommandName.class)){
				String commandName = commandClass.getAnnotation(CommandName.class).value();
				
				String previousRegisterer = usedNames.get(commandName);
				if(previousRegisterer != null && previousRegisterer != commandClass.getName()){
					CoreUtils.io.sendConsoleMessageWarning("W rejestrze komend znajdowa�a si� ju� komenda " + commandName + "!");
					CoreUtils.io.sendConsoleMessageWarning("Klasy komend:");
					CoreUtils.io.sendConsoleMessageWarning("  - Stara: " + previousRegisterer);
					CoreUtils.io.sendConsoleMessageWarning("  - Nowa: " + commandClass.getName());
					CoreUtils.io.sendConsoleMessageWarning("Nowa komenda NIE zosta�a zarejestrowana.");
					return;
				}
				commandRegisterers.put(commandName, component);
				
				ArrayList<String> aliasesList = new ArrayList<>();
				if(commandClass.isAnnotationPresent(Aliases.class)){
					String[] aliases = commandClass.getAnnotation(Aliases.class).values();
					aliasesList = new ArrayList<>(Arrays.asList(aliases));
					boolean aliasesCollides = false;
					Set<String> collidingClasses = new HashSet<>();
					for(String alias : aliases){
						if(usedNames.containsKey(alias)){
							aliasesCollides = true;
							collidingClasses.add(usedNames.get(alias));
							aliasesList.remove(alias);
						}
					}
					if(aliasesCollides){
						CoreUtils.io.sendConsoleMessageWarning("Klasa komendy: " + commandClass.getName() + " - niekt�re aliasy koliduj� z komendami z rejestru!");
						CoreUtils.io.sendConsoleMessageWarning("Klasy, kt�re zarejestrowa�y koliduj�ce aliasy: ");
						collidingClasses.forEach(v -> CoreUtils.io.sendConsoleMessageWarning("- " + v));
						CoreUtils.io.sendConsoleMessageWarning("Koliduj�ce aliasy NIE zosta�y zarejestrowane.");
					}
				}
				
				aliasesList.forEach(v -> usedNames.put(v, commandClass.getName()));
				CommandHandler handler = new CommandHandler(commandClass, commandName, aliasesList);
				usedNames.put(commandName, commandClass.getName());
				SimpleCommandMap commandMap = ((CraftServer) Bukkit.getServer()).getCommandMap();
				commandMap.register(commandName, handler);
			} else {
				if(!errorsInfo.errorOccured)
					CoreUtils.io.sendConsoleMessageWarning("Plugin " + component.getName() + ":");
				errorsInfo.errorOccured = true;
				errorsInfo.annotationErrorOccured = true;
				CoreUtils.io.sendConsoleMessageWarning("- Klasa komendy: " + commandClass.getName() + " - brak adnotacji @CommandName!");
			}
		} catch(Exception e){
			if(!errorsInfo.errorOccured)
				CoreUtils.io.sendConsoleMessageWarning("Plugin " + component.getName() + ":");
			errorsInfo.errorOccured = true;
			errorsInfo.constructorErrorOccured = true;						
			CoreUtils.io.sendConsoleMessageWarning("- Klasa komendy " + commandClass.getName() + " posiada w�asny konstruktor!");
		}
		if(single){
			if(errorsInfo.annotationErrorOccured)
				CoreUtils.io.sendConsoleMessageWarning("Adnotacja @CommandName jest wymagana jako nag��wek klasy komendy.");
			if(errorsInfo.constructorErrorOccured)
				CoreUtils.io.sendConsoleMessageWarning("Klasy komend nie mog� definiowa� w�asnego konstruktora!");
		}
	}

	@Override public void addAlias(String commandName, String alias){
		if(commandRegisterers.get(commandName) != null){
			SimpleCommandMap commandMap = ((CraftServer) Bukkit.getServer()).getCommandMap();
			Command command = commandMap.getCommand(commandName);
			List<String> aliases = command.getAliases();
			if(usedNames.containsKey(alias)){
				if(!aliases.contains(alias) && alias != commandName)
					CoreUtils.io.sendConsoleMessageWarning("Co� pr�bowa�o zarejestrowa� dodatkowy alias dla komendy " + commandName + ", ale ten funkcjonuje ju� jako inna komenda lub jej alias!");
				return;
			}
			aliases.add(alias);
			command.setAliases(aliases);
			command.unregister(commandMap);
			command.register(commandMap);
			Object map = CoreUtils.developer.getFieldValue(commandMap, "knownCommands");
			@SuppressWarnings("unchecked")
			HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
			knownCommands.put(alias, command);
			usedNames.put(alias, "Brak informacji - alias dodany r�cznie");
		}
	}
	
	@Override public void unregisterAll(CommodoreComponent addOn) {
		if(!commandRegisterers.values().contains(addOn))	return;
		Iterator<Entry<String, CommodoreComponent>> iterator = commandRegisterers.entrySet().iterator();
		SimpleCommandMap commandMap = ((CraftServer) Bukkit.getServer()).getCommandMap();
		Object map = CoreUtils.developer.getFieldValue(commandMap, "knownCommands");
		@SuppressWarnings("unchecked")
		HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
		while(iterator.hasNext()){
			Entry<String, CommodoreComponent> entry = iterator.next();
			if(entry.getValue() == addOn){
				String commandName = entry.getKey();
				massiveUnregistering(commandName, commandMap, knownCommands);
			}
		}
	}
	
	private void massiveUnregistering(String commandName, SimpleCommandMap commandMap, HashMap<String, Command> knownCommands){
		for (String alias : commandMap.getCommand(commandName).getAliases())
			if(knownCommands.containsKey(alias)){
				usedNames.remove(alias);
				knownCommands.remove(alias);
			}
		knownCommands.remove(commandName);
		usedNames.remove(commandName);
		commandRegisterers.remove(commandName);
	}
	
	@Override public void unregister(String commandName) {
		if(commandRegisterers.get(commandName) != null){
			SimpleCommandMap commandMap = ((CraftServer) Bukkit.getServer()).getCommandMap();
			Object map = CoreUtils.developer.getFieldValue(commandMap, "knownCommands");
			@SuppressWarnings("unchecked")
			HashMap<String, Command> knownCommands = (HashMap<String, Command>) map;
			massiveUnregistering(commandName, commandMap, knownCommands);
		}
	}
}
