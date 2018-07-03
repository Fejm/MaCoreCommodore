package pl.mateam.marpg.core.data.commands;

import org.bukkit.entity.Player;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.classes.commands.CommodoreCommand;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;

@CommodoreCommand.CommandName("vanish")
@CommodoreCommand.Aliases(values={"v"})
public class Vanish extends CommodoreCommand {
	@Override public void trigger() {
		switch(args.length) {
			case 0:		handleZeroArgumentsCase(); 		break;
			
			case 1:
			case 2:		handleRegularCase(); 			break;

			default:	handleTooManyArgumentCase();	break;
		}
	}
	
	private void handleZeroArgumentsCase() {
		Gamemaster gamemaster = Core.getServer().getUsers().getGamemasterObject((Player) executor);
		String message = gamemaster.isVisible()? "widoczny." : "niewidoczny.";
		sendMessageToExecutor("Jesteś " + message);
	}
	
	private void handleRegularCase() {
		String arg = args[0].toLowerCase();
		if(arg.equals("t") || arg.equals("n")) {
			Gamemaster gamemaster = Core.getServer().getUsers().getGamemasterObject((Player) executor);
			boolean visible = arg.equals("t")? true : false;
			boolean silent = false;
			if(args.length == 2)
				if(args[1].toLowerCase().equals("-s"))
					silent = true;
				else {
					informAboutCommandUsage(ErrorReason.WRONG_SYNTAX);
					return;
				}
			gamemaster.setVisible(visible, silent);
		} else informAboutCommandUsage(ErrorReason.WRONG_SYNTAX);
	}

	private void handleTooManyArgumentCase() 	{	informAboutCommandUsage(ErrorReason.TOO_MANY_ARGS);		}
	
	@Override public boolean canBeUsedByModerators()	{	return false;	}
	@Override public boolean canBeUsedFromTheConsole() 	{	return false;	}
	@Override protected String correctUsage() 			{	return putParametersIntoBrackets(false, "t", "n") + " " + putParametersIntoBrackets(false, "-s");	}
}
