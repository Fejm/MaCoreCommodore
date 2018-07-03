package pl.mateam.marpg.api.utils;

public interface ChatUtils {
	String getConsoleColorWarning();
	String getConsoleColorWarningHighlighted();
	String getConsoleColorSuccess();
	String getConsoleColorSuccessHighlighted();
	String getConsoleColorImportant();
	String getConsoleColorImportantHighlighted();

	String getMaRPGmessageStart();
	String getMaRPGcolorCasual();
	String getMaRPGcolorCasualHighlighted();
	String getMaRPGcolorInfo();
	String getMaRPGcolorInfoHighlighted();
	String getMaRPGcolorError();
	String getMaRPGcolorErrorHighlighted();
	String getMaRPGcolorSuccess();
	String getMaRPGcolorSuccessHighlighted();
	String getGamemasterColorCasual();
	String getGamemasterColorCasualHighlighted();
	String getGamemasterMessageStart();
	
	String getCasualMessage(String message);
	String getInfoMessage(String message);
	String getSuccessMessage(String message);
	String getErrorMessage(String message);

	String getCasualMessageWithHighlight(String part1, String highlight, String part2);
	String getInfoMessageWithHighlight(String part1, String highlight, String part2);
	
	String getCasualAdminMessage(String message);
	String getCasualAdminMessageWithHighlight(String part1, String highlight, String part2);
	
	String getBroadcastColor();
	String getBroadcastColorHighlighted();
	String getBroadcastMessage(String message);
	String getBroadcastMessageWithHighlight(String part1, String highlight, String part2);
	
	String removePolishSymbols(String message);
}
