package pl.mateam.marpg.core.modules.external.utils;

import net.md_5.bungee.api.ChatColor;
import pl.mateam.marpg.api.utils.ChatUtils;

public class ChatUtilsImplementation implements ChatUtils {
	@Override public String getCasualMessage(String message)		{	return getMaRPGmessageStart() + getMaRPGcolorCasual() + message;			}
	@Override public String getInfoMessage(String message)			{	return getMaRPGmessageStart() + getMaRPGcolorInfo() + message;				}
	@Override public String getSuccessMessage(String message)		{	return getMaRPGmessageStart() + getMaRPGcolorSuccess() + message;			}
	@Override public String getErrorMessage(String message)			{	return getMaRPGmessageStart() + getMaRPGcolorError() + message;				}
	@Override public String getCasualAdminMessage(String message) 	{	return getGamemasterMessageStart() + getGamemasterColorCasual() + message;	}

	
	
	@Override public String getCasualMessageWithHighlight(String part1, String highlight, String part2) {
		return getMaRPGmessageStart() + getMaRPGcolorCasual() + part1
				+ getMaRPGcolorCasualHighlighted() + highlight
				+ getMaRPGcolorCasual() + part2;
	}
	
	@Override public String getInfoMessageWithHighlight(String part1, String highlight, String part2) {
		return getMaRPGmessageStart() + getMaRPGcolorInfo() + part1
				+ getMaRPGcolorInfoHighlighted() + highlight
				+ getMaRPGcolorInfo() + part2;
	}
	
	@Override public String getCasualAdminMessageWithHighlight(String part1, String highlight, String part2) {
		return getGamemasterMessageStart() + getGamemasterColorCasual() + part1
				+ getGamemasterColorCasualHighlighted() + highlight
				+ getGamemasterColorCasual() + part2;
	}
	
	@Override public String getBroadcastMessageWithHighlight(String part1, String highlight, String part2) {
		return getBroadcastColor() + part1
				+ getBroadcastColorHighlighted() + highlight
				+ getBroadcastColor() + part2;
	}

	
	@Override public String removePolishSymbols(String message){
		return message	.replace("�", "a")
						.replace("�", "A")
						.replace("�", "c")
						.replace("�", "C")
						.replace("�", "e")
						.replace("�", "E")
						.replace("�", "l")
						.replace("�", "L")
						.replace("�", "n")
						.replace("�", "N")
						.replace("�", "o")
						.replace("�", "O")
						.replace("�", "s")
						.replace("�", "S")
						.replace("�", "z")
						.replace("�", "Z")
						.replace("�", "z")
						.replace("�", "Z");
	}
	
	
	
	@Override public String getConsoleColorWarning() 				{	return ChatColor.RED.toString();												}
	@Override public String getConsoleColorWarningHighlighted() 	{	return ChatColor.DARK_RED.toString();											}
	@Override public String getConsoleColorSuccess() 				{	return ChatColor.GREEN.toString();												}
	@Override public String getConsoleColorSuccessHighlighted() 	{	return ChatColor.DARK_GREEN.toString();											}
	@Override public String getConsoleColorImportant() 				{	return ChatColor.YELLOW.toString();												}
	@Override public String getConsoleColorImportantHighlighted() 	{	return ChatColor.GOLD.toString();												}

	@Override public String getMaRPGcolorCasual() 					{	return ChatColor.GRAY.toString();												}
	@Override public String getMaRPGcolorCasualHighlighted() 		{	return ChatColor.WHITE.toString();												}
	@Override public String getMaRPGcolorInfo() 					{	return ChatColor.YELLOW.toString();												}
	@Override public String getMaRPGcolorInfoHighlighted() 			{	return ChatColor.GOLD.toString();												}
	@Override public String getMaRPGcolorError() 					{	return ChatColor.RED.toString();												}
	@Override public String getMaRPGcolorErrorHighlighted() 		{	return ChatColor.DARK_RED.toString();											}
	@Override public String getMaRPGcolorSuccess() 					{	return ChatColor.GREEN.toString();												}
	@Override public String getMaRPGcolorSuccessHighlighted() 		{	return ChatColor.DARK_GREEN.toString();											}
	@Override public String getMaRPGmessageStart()					{	return ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "[MaRPG] ";		}
	
	@Override public String getGamemasterColorCasual() 				{	return ChatColor.DARK_AQUA.toString();											}
	@Override public String getGamemasterColorCasualHighlighted() 	{	return ChatColor.AQUA.toString();												}
	@Override public String getGamemasterMessageStart() 			{	return ChatColor.AQUA.toString() + ChatColor.BOLD.toString() + "[GM helper] ";	}
	
	@Override public String getBroadcastColor()						{	return ChatColor.YELLOW.toString() + ChatColor.BOLD.toString();					}
	@Override public String getBroadcastColorHighlighted()			{	return ChatColor.GOLD.toString() + ChatColor.BOLD.toString();					}
	
	@Override public String getBroadcastMessage(String message) 	{	return getBroadcastColor() + message;											}
}
