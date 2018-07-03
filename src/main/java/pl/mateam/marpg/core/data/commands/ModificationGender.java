package pl.mateam.marpg.core.data.commands;

import java.util.function.Consumer;

import pl.mateam.marpg.api.regular.classes.commands.CommodoreCommand;
import pl.mateam.marpg.api.regular.classes.commands.CommodoreSimpleModificationCommand;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseWriter;
import pl.mateam.marpg.api.regular.objects.users.PlayerBasedUser;

@CommodoreCommand.CommandName("plec")
@CommodoreCommand.Aliases(values={"sex", "gender"})
public class ModificationGender extends CommodoreSimpleModificationCommand {
	@Override protected String[] getAllowedValues() {
		String[] allowedValues = {"k", "m"};
		return allowedValues;
	}
	
	@Override public boolean canBeUsedByModerators()	{	return false;	}
	@Override public boolean canBeUsedFromTheConsole() 	{	return true;	}
	
	@Override protected void handleOneArgumentCase() {
		Consumer<PlayerBasedUser> ingameAction = object -> {
			String messageToSend = object.isWoman()? "kobietą" : "mężczyzną";
			sendMessageWithHighlightToExecutor(args[0] + " jest ", messageToSend, ".");
		};
		Consumer<PlayerAccountDatabaseReader> actionWithReader = reader -> {
			boolean isWoman = reader.getIsWoman();
			String messageToSend = isWoman? "kobietą" : "mężczyzną";
			sendMessageWithHighlightToExecutor(args[0] + " jest ", messageToSend, ".");
		};
		handleOneArgumentCasePlayerBased(ingameAction, actionWithReader);
	}

	@Override protected void handleTwoArgumentCase() {
		Boolean isWoman = args[1].equalsIgnoreCase("k")? true : false;
		Runnable actionToPerformAfterDatabaseUpdate = () -> {
			String messageToSend = isWoman? "kobietą" : "mężczyzną";
			sendMessageWithHighlightToExecutor(args[0] + " jest od teraz ", messageToSend, ".");
		};
		Consumer<PlayerAccountDatabaseWriter> actionWithWriter = reader -> reader.setIsWoman(isWoman);
		Consumer<PlayerBasedUser> ingameAction = object -> {
			object.setWoman(isWoman);
			actionToPerformAfterDatabaseUpdate.run();;
		};
		handleTwoArgumentsCasePlayerBased(ingameAction, actionWithWriter, actionToPerformAfterDatabaseUpdate);
	}
}
