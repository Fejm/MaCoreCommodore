package pl.mateam.marpg.api.utils;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public interface IOUtils {	

	void setPolishSymbolsInConsole(boolean toAllow);
	boolean isPolishSymbolsFlag();
	
	void sendConsoleMessage(String message);
	void sendConsoleMessageSuccess(String message);
	void sendConsoleMessageSuccessWithHighlight(String prefix, String highlight, String suffix);
	void sendConsoleMessageWarning(String message);
	void sendConsoleMessageWarningWithHighlight(String prefix, String highlight, String suffix);
	void sendConsoleMessageImportant(String message);
	void sendConsoleMessageImportantWithHighlight(String prefix, String highlight, String suffix);

	List<String> getRawTextFromURL(URL url);
	String getUUIDofPlayerPremium(String nick);
}
