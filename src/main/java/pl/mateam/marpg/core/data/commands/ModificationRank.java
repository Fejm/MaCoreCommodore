package pl.mateam.marpg.core.data.commands;

import java.util.function.Consumer;

import pl.mateam.marpg.api.regular.classes.commands.CommodoreCommand;
import pl.mateam.marpg.api.regular.classes.commands.CommodoreSimpleModificationCommand;
import pl.mateam.marpg.api.regular.enums.players.Rank;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseReader;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseWriter;
import pl.mateam.marpg.api.regular.objects.users.PlayerBasedUser;

@CommodoreCommand.CommandName("ranga")
@CommodoreCommand.Aliases(values={"rank"})
public class ModificationRank extends CommodoreSimpleModificationCommand {
	@Override protected String[] getAllowedValues() {
		String[] allowedValues = {"gracz", "premium", "szlachcic"};
		return allowedValues;
	}
	
	@Override public boolean canBeUsedByModerators()	{	return false;	}
	@Override public boolean canBeUsedFromTheConsole() 	{	return true;	}
	
	@Override protected void handleOneArgumentCase() {
		Consumer<PlayerBasedUser> ingameAction = object -> {
			String messageToSend = null;
			switch(object.getRank()){
				case PLAYER:	messageToSend = " jest zwykłym szarym graczem";		break;
				case PREMIUM:	messageToSend = " jest graczem premium";			break;
				case NOBLEMAN:	messageToSend = " jest szlachcicem";				break;
			
			}
			sendMessageWithHighlightToExecutor(args[0], messageToSend, ".");
		};
		
		Consumer<PlayerAccountDatabaseReader> actionWithReader = reader -> {
			String messageToSend = null;
			switch(reader.getRank()){
				case PLAYER:	messageToSend = " jest zwykłym szarym graczem";		break;
				case PREMIUM:	messageToSend = " jest graczem premium";			break;
				case NOBLEMAN:	messageToSend = " jest szlachcicem";				break;
			}
			sendMessageWithHighlightToExecutor(args[0], messageToSend, ".");
		};
		
		
		handleOneArgumentCasePlayerBased(ingameAction, actionWithReader);
	}

	@Override protected void handleTwoArgumentCase() {
		Rank newRank = Rank.PLAYER;
		String messageToSend = null;
		switch(args[1].toLowerCase()){
			case "gracz":
				newRank = Rank.PLAYER;
				messageToSend = "zwykłym szarym graczem";
				break;
			case "premium":
				newRank = Rank.PREMIUM;
				messageToSend = "graczem premium";
				break;
			case "szlachcic":
				newRank = Rank.NOBLEMAN;
				messageToSend = "szlachcicem";
				break;
		}

		String messageToSendCopy = messageToSend;
		Runnable actionToPerformAfterDatabaseUpdate = () -> sendMessageWithHighlightToExecutor(args[0] + " jest od teraz ", messageToSendCopy, ".");
		
		Rank newRankCopy = newRank;
		Consumer<PlayerBasedUser> ingameAction = object -> {
			object.setRank(newRankCopy);
			actionToPerformAfterDatabaseUpdate.run();;
		};
		
		Consumer<PlayerAccountDatabaseWriter> actionWithWriter = writer -> {
			writer.setRank(newRankCopy);
		};
		
		handleTwoArgumentsCasePlayerBased(ingameAction, actionWithWriter, actionToPerformAfterDatabaseUpdate);
	}
}
