package pl.mateam.marpg.api.regular.classes.commands;

import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils.ErrorProne;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.DatabaseCollectionExplorer;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.DatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.everyacc.EveryAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.gamemasteracc.GamemasterAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseWriter;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;
import pl.mateam.marpg.api.regular.objects.users.PlayerBasedUser;

public abstract class CommodoreSimpleModificationCommand extends CommodoreCommand {
	protected abstract String[] getAllowedValues();
	protected abstract void handleOneArgumentCase();
	protected abstract void handleTwoArgumentCase();

	@Override public final void trigger() {
		switch(args.length){
			case 0:		informAboutCommandUsage(ErrorReason.NOT_ENOUGH_ARGS);	break;
			case 1:		handleOneArgumentCase();								break;
			case 2:		handleTwoArgumentCase();								break;
			default:	informAboutCommandUsage(ErrorReason.TOO_MANY_ARGS);		break;
		}
	}
	
	@Override public final String correctUsage() 	{	return "<nick>" + " " + putParametersIntoBrackets(false, getAllowedValues());	}
	
	protected void handleOneArgumentCasePlayerBased(Consumer<PlayerBasedUser> ingameAction, Consumer<PlayerAccountDatabaseReader> actionWithReader){
		Function<Player, Object> supplier = player -> Core.getServer().getUsers().getPlayerBasedUser(player);
		Consumer<Object> consumer = object -> ingameAction.accept((PlayerBasedUser) object);
		handleOne(supplier, consumer, "Nie możesz wykonać tej komendy na Mistrzu Gry!", Core.getDatabase().getExplorer().getPlayerAccountCollection()::getReader,
				"Podany gracz nie istnieje lub jest Mistrzem Gry.", actionWithReader);
	}

	protected void handleOneArgumentCaseGamemasterBased(Consumer<Gamemaster> ingameAction, Consumer<GamemasterAccountDatabaseReader> actionWithReader){
		Function<Player, Object> supplier = player -> Core.getServer().getUsers().getGamemasterObject(player);
		Consumer<Object> consumer = object -> ingameAction.accept((Gamemaster) object);
		handleOne(supplier, consumer, "Tą komendę możesz wywołać tylko na Mistrzu Gry!", Core.getDatabase().getExplorer().getGamemasterAccountCollection()::getReader,
				"Podany gracz nie istnieje lub nie jest Mistrzem Gry.", actionWithReader);
	}
	
	protected void handleOneArgumentCaseEveryPlayerBased(Consumer<AnyUser> ingameAction, Consumer<EveryAccountDatabaseReader> actionWithReader){
		Function<Player, Object> supplier = player -> Core.getServer().getUsers().getGenericObject(player);
		Consumer<Object> consumer = object -> ingameAction.accept((AnyUser) object);
		handleOne(supplier, consumer, "Wystąpił nieznany błąd! Koniecznie skontaktuj się z Marcoralem!", Core.getDatabase().getExplorer().getEveryAccountCollection()::getReader,
				"Podany gracz nigdy nie pojawił się na serwerze.", actionWithReader);
	}
	
	protected void handleTwoArgumentsCasePlayerBased(Consumer<PlayerBasedUser> ingameAction, Consumer<PlayerAccountDatabaseWriter> actionWithWriter, Runnable actionToPerformAfterDatabaseUpdate) {
		Function<Player, Object> supplier = player -> Core.getServer().getUsers().getPlayerBasedUser(player);
		Consumer<Object> consumer = object -> ingameAction.accept((PlayerBasedUser) object);
		Supplier<Document> updateOperationSupplier = actionWithWriter == null? null : () -> {
			Document document = new Document();
			PlayerAccountDatabaseWriter writer = Core.getDatabase().getExplorer().getPlayerAccountCollection().getWriter(document);
			actionWithWriter.accept(writer);
			return document;
		};
		
		handleTwo(supplier, consumer, "Nie możesz wykonać tej komendy na Mistrzu Gry!", updateOperationSupplier,
				Core.getDatabase().getExplorer().getPlayerAccountCollection(), "Podany gracz nie istnieje lub jest Mistrzem Gry.", actionToPerformAfterDatabaseUpdate);
	}
	
	protected <T> void handleTwoArgumentsCaseGamemasterBased(Consumer<Gamemaster> ingameAction, Consumer<PlayerAccountDatabaseWriter> actionWithWriter, Runnable actionToPerformAfterDatabaseUpdate) {
		Function<Player, Object> supplier = player -> Core.getServer().getUsers().getGamemasterObject(player);
		Consumer<Object> consumer = object -> ingameAction.accept((Gamemaster) object);
		Supplier<Document> updateOperationSupplier = actionWithWriter == null? null : () -> {
			Document document = new Document();
			PlayerAccountDatabaseWriter writer = Core.getDatabase().getExplorer().getPlayerAccountCollection().getWriter(document);
			actionWithWriter.accept(writer);
			return document;
		};
		
		handleTwo(supplier, consumer, "Tą komendę możesz wywołać tylko na Mistrzu Gry!", updateOperationSupplier,
				Core.getDatabase().getExplorer().getPlayerAccountCollection(), "Podany gracz nie istnieje lub nie jest Mistrzem Gry.", actionToPerformAfterDatabaseUpdate);
	}
	
	protected <T> void handleTwoArgumentsCaseEveryPlayerBased(Consumer<AnyUser> ingameAction, Consumer<PlayerAccountDatabaseWriter> actionWithWriter, Runnable actionToPerformAfterDatabaseUpdate) {
		Function<Player, Object> supplier = player -> Core.getServer().getUsers().getGenericObject(player);
		Consumer<Object> consumer = object -> ingameAction.accept((AnyUser) object);
		Supplier<Document> updateOperationSupplier = actionWithWriter == null? null : () -> {
			Document document = new Document();
			PlayerAccountDatabaseWriter writer = Core.getDatabase().getExplorer().getPlayerAccountCollection().getWriter(document);
			actionWithWriter.accept(writer);
			return document;
		};
		
		handleTwo(supplier, consumer, "Wystąpił nieznany błąd! Koniecznie skontaktuj się z Marcoralem!", updateOperationSupplier,
				Core.getDatabase().getExplorer().getPlayerAccountCollection(), "Podany gracz nigdy nie pojawił się na serwerze.", actionToPerformAfterDatabaseUpdate);
	}
	





	private <T extends DatabaseReader> void handleOne(Function<Player, Object> supplier, Consumer<Object> ingameAction, String notValidTypeMessage, Function<String, T> documentReader, String nullDocumentMessage, Consumer<T> actionWithReader) {		
		if(!handlePlayerOnline(supplier, ingameAction, notValidTypeMessage) && actionWithReader != null) {
			@ErrorProne Plugin plugin = Bukkit.getPluginManager().getPlugin("MaCoreCommodore");
			Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
				T reader = documentReader.apply(args[0]);
				if(reader.isValid()) {
	            	if(actionWithReader != null) {
	            		Bukkit.getScheduler().runTask(plugin, () -> actionWithReader.accept(reader));
	            	}		
	            } else 
	    			sendMessageToExecutor(nullDocumentMessage);
			});
		}
	}
	
	private boolean handlePlayerOnline(Function<Player, Object> supplier, Consumer<Object> ingameAction, String notValidTypeMessage){
		Player player = Bukkit.getPlayer(args[0]);
		if(player == null)
			return false;
		Object object = supplier.apply(player);
		if(object != null)
			ingameAction.accept(object);
		else sendMessageToExecutor(notValidTypeMessage);
		return true;
	}
	
	private <T> void handleTwo(Function<Player, Object> supplier, Consumer<Object> ingameAction, String notValidTypeMessage, Supplier<Document> updateOperationSupplier, DatabaseCollectionExplorer collection, String nullDocumentMessage, Runnable actionToPerformAfterDatabaseUpdate){
		for(String value : getAllowedValues())
			if(value.equalsIgnoreCase(args[1])) {
				if(!handlePlayerOnline(supplier, ingameAction, notValidTypeMessage) && updateOperationSupplier != null) {
					Document document = updateOperationSupplier.get();
					@ErrorProne Plugin plugin = Bukkit.getPluginManager().getPlugin("MaCoreCommodore");
					Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
						boolean updated = collection.updateBySetting(args[0], document).getMatchedCount() > 0;
						Bukkit.getScheduler().runTask(plugin, () -> {
							if(updated)
								if(actionToPerformAfterDatabaseUpdate != null)
									actionToPerformAfterDatabaseUpdate.run();
							else
				    			sendMessageToExecutor(nullDocumentMessage);
						});
					});
				}
				return;
			}
		informAboutCommandUsage(ErrorReason.WRONG_SYNTAX);
	}
}
