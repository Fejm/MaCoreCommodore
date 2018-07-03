package pl.mateam.marpg.core.data.commands;

import java.util.function.Consumer;

import pl.mateam.marpg.api.regular.classes.commands.CommodoreCommand;
import pl.mateam.marpg.api.regular.classes.commands.CommodoreSimpleModificationCommand;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseWriter;
import pl.mateam.marpg.api.regular.objects.users.PlayerBasedUser;

@CommodoreCommand.CommandName("moderator")
@CommodoreCommand.Aliases(values={"mod"})
public class ModificationModerator extends CommodoreSimpleModificationCommand {
	@Override protected String[] getAllowedValues() {
		String[] allowedValues = {"t", "n"};
		return allowedValues;
	}
	
	@Override public boolean canBeUsedByModerators()	{	return false;	}
	@Override public boolean canBeUsedFromTheConsole() 	{	return true;	}
	
	@Override protected void handleOneArgumentCase() {
		Consumer<PlayerBasedUser> ingameAction = object -> {
			String messageToSend = object.isModerator()? " jest " : " nie jest ";
			sendMessageWithHighlightToExecutor(args[0], messageToSend, "moderatorem.");
		};
		Consumer<PlayerAccountDatabaseReader> actionWithReader = reader -> {
			boolean isModerator = reader.getIsModerator();
			String messageToSend = isModerator? " jest " : " nie jest ";
			sendMessageWithHighlightToExecutor(args[0], messageToSend, "moderatorem.");
		};
		handleOneArgumentCasePlayerBased(ingameAction, actionWithReader);
	}

	@Override protected void handleTwoArgumentCase() {
		Boolean isModerator = args[1].equalsIgnoreCase("t")? true : false;
		Runnable actionToPerformAfterDatabaseUpdate = () -> {
			String messageToSend = isModerator? " jest od teraz " : " od teraz nie jest ";
			sendMessageWithHighlightToExecutor(args[0], messageToSend, "moderatorem.");
		};
		Consumer<PlayerAccountDatabaseWriter> actionWithWriter = reader -> reader.setIsModerator(isModerator);
		Consumer<PlayerBasedUser> ingameAction = object -> {
			object.setModerator(isModerator);
			actionToPerformAfterDatabaseUpdate.run();
		};
		handleTwoArgumentsCasePlayerBased(ingameAction, actionWithWriter, actionToPerformAfterDatabaseUpdate);
	}
}
