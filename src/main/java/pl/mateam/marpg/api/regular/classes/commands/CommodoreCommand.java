package pl.mateam.marpg.api.regular.classes.commands;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.utils.DeveloperUtils.ReflectiveField;

/*
 * All this crazy stuff and reflections makes writing command classes very nice and simple.
 * As commands aren't executed often, I allowed myself to do such thing.
 */
public abstract class CommodoreCommand {
	@ReflectiveField(purpose = "command executor") 	 protected final CommandSender executor = null;		//This field is set via reflection
	@ReflectiveField(purpose = "command arguments")	 protected final String[] args = null;				//This field will be set via reflection

	public abstract void trigger();	
	public abstract boolean canBeUsedByModerators();
	public abstract boolean canBeUsedFromTheConsole();
	
	protected abstract String correctUsage();
	protected void informAboutCommandUsage(ErrorReason reason) 	{	informAboutCommandUsage(reason.message);	}
	protected void informAboutCommandUsage(String firstMessage) {
		sendMessageToExecutor(firstMessage);
		sendMessageToExecutor("Wzorzec komendy: /" + getClass().getAnnotation(CommandName.class).value() + " " + correctUsage());
	}
	
	
	protected String putParametersIntoBrackets(boolean obligatoryArgs, String... args){
		char lBracket, rBracket;
		if(obligatoryArgs){
			lBracket = '<';
			rBracket = '>';
		} else {
			lBracket = '[';
			rBracket = ']';
		}
		StringBuilder values = new StringBuilder(lBracket);
		for(String allowedValue : args)
			values.append(allowedValue + "/");
		values.setLength(values.length() - 1);
		values.append(rBracket);
		return values.toString();
	}


	protected void sendMessageToExecutor(String message){
		if(executor instanceof Player)
			executor.sendMessage(CoreUtils.chat.getCasualAdminMessage(message));
		else
			CoreUtils.io.sendConsoleMessageImportant(message);
	}
	protected void sendMessageWithHighlightToExecutor(String prefix, String highlightedMessage, String postfix){
		if(executor instanceof Player)
			executor.sendMessage(CoreUtils.chat.getCasualAdminMessageWithHighlight(prefix, highlightedMessage, postfix));
		else
			CoreUtils.io.sendConsoleMessageImportantWithHighlight(prefix, highlightedMessage, postfix);
	}
	
	protected static enum ErrorReason {
		TOO_MANY_ARGS("Podano za dużo argumentów."), NOT_ENOUGH_ARGS("Podano za mało argumentów."), WRONG_SYNTAX("Niepoprawna składnia komendy lub błędne argumenty.");
		
		private String message;
		private ErrorReason(String message) {
			this.message = message;
		}
	}
	
	@Target(ElementType.TYPE)	 @Retention(RetentionPolicy.RUNTIME)
	public @interface CommandName 	{	String value();		}
	
	@Target(ElementType.TYPE)	 @Retention(RetentionPolicy.RUNTIME)
	public @interface Aliases 		{	String[] values();	}
}
