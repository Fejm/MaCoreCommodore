package pl.mateam.marpg.core.modules.external.utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

import org.bukkit.Bukkit;

import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.utils.IOUtils;

public class IOUtilsImplementation implements IOUtils {
	private boolean polishSymbolsConsoleAllowed = false;
	
	@Override public void setPolishSymbolsInConsole(boolean toAllow)	{	polishSymbolsConsoleAllowed = toAllow;	}
	@Override public boolean isPolishSymbolsFlag()					{	return polishSymbolsConsoleAllowed;		}
	
	@Override public void sendConsoleMessage(String message){
		message = isPolishSymbolsFlag()?	message	: CoreUtils.chat.removePolishSymbols(message);
		Bukkit.getServer().getConsoleSender().sendMessage(message);
	}
	
	@Override public void sendConsoleMessageSuccess(String message){
		message = CoreUtils.chat.getConsoleColorSuccess() + message;
		message = isPolishSymbolsFlag()?	message	: CoreUtils.chat.removePolishSymbols(message);
		Bukkit.getServer().getConsoleSender().sendMessage(message);
	}
	
	@Override public void sendConsoleMessageSuccessWithHighlight(String prefix, String highlight, String suffix){
		if(!isPolishSymbolsFlag()){
			prefix = CoreUtils.chat.removePolishSymbols(prefix);
			highlight = CoreUtils.chat.removePolishSymbols(highlight);
			suffix = CoreUtils.chat.removePolishSymbols(suffix);
		}
		Bukkit.getServer().getConsoleSender().sendMessage(
				CoreUtils.chat.getConsoleColorSuccess() + prefix +
				CoreUtils.chat.getConsoleColorSuccessHighlighted() + highlight +
				CoreUtils.chat.getConsoleColorSuccess() + suffix);
	}
	
	@Override public void sendConsoleMessageWarning(String message){
		message = CoreUtils.chat.getConsoleColorWarning() + message;
		message = isPolishSymbolsFlag()?	message	: CoreUtils.chat.removePolishSymbols(message);
		Bukkit.getServer().getConsoleSender().sendMessage(message);
	}

	@Override public void sendConsoleMessageWarningWithHighlight(String prefix, String highlight, String suffix){
		if(!isPolishSymbolsFlag()){
			prefix = CoreUtils.chat.removePolishSymbols(prefix);
			highlight = CoreUtils.chat.removePolishSymbols(highlight);
			suffix = CoreUtils.chat.removePolishSymbols(suffix);
		}
		Bukkit.getServer().getConsoleSender().sendMessage(
				CoreUtils.chat.getConsoleColorWarning() + prefix +
				CoreUtils.chat.getConsoleColorWarningHighlighted() + highlight +
				CoreUtils.chat.getConsoleColorWarning() + suffix);
	}

	@Override public void sendConsoleMessageImportant(String message){
		message = CoreUtils.chat.getConsoleColorImportant() + message;
		message = isPolishSymbolsFlag()?	message	: CoreUtils.chat.removePolishSymbols(message);
		Bukkit.getServer().getConsoleSender().sendMessage(message);
	}
	
	@Override public void sendConsoleMessageImportantWithHighlight(String prefix, String highlight, String suffix){
		if(!isPolishSymbolsFlag()){
			prefix = CoreUtils.chat.removePolishSymbols(prefix);
			highlight = CoreUtils.chat.removePolishSymbols(highlight);
			suffix = CoreUtils.chat.removePolishSymbols(suffix);
		}
		Bukkit.getServer().getConsoleSender().sendMessage(
				CoreUtils.chat.getConsoleColorImportant() + prefix +
				CoreUtils.chat.getConsoleColorImportantHighlighted() + highlight +
				CoreUtils.chat.getConsoleColorImportant() + suffix);
	}
	
	@Override public ArrayList<String> getRawTextFromURL(URL url){
		try{
			ArrayList<String> result = new ArrayList<String>();
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(5000);
			InputStreamReader input = new InputStreamReader(conn.getInputStream());
			BufferedReader br = new BufferedReader(input);
			String s;
			while((s = br.readLine()) != null)
			    result.add(s);
			input.close();
			return result;
		} catch(Exception e){
		}
		return null;
	}
	
	@Override public String getUUIDofPlayerPremium(String nick) {
		try {
			ArrayList<String> text = getRawTextFromURL(new URL("https://api.mojang.com/users/profiles/minecraft/" + nick));
			if(text == null || text.size() == 0)	return null;
			String[] splitText = text.get(0).split("\"");
			return splitText[3];
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
		return null;
	}
}
