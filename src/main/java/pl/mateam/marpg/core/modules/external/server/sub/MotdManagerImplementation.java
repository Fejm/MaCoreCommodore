package pl.mateam.marpg.core.modules.external.server.sub;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;
import pl.mateam.marpg.api.regular.modules.sub.server.MotdManager;

import com.comphenix.protocol.wrappers.WrappedGameProfile;

public class MotdManagerImplementation implements MotdManager {
	private String firstLine;
	private String secondLine;
	
	private boolean motdSavesAutomatically;
	private boolean hoverMotdSavesAutomatically;
	
	private List<String> rawHoverMotdLines = new ArrayList<>();
	private List<WrappedGameProfile> hoverMotd = new ArrayList<>();
	private Map<String, String> hoverMotdMetaLines = new LinkedHashMap<>();
	
	@Override public boolean doesMotdSaveAutomatically()					{	return motdSavesAutomatically;						}
	@Override public boolean doesHoverMotdSaveAutomatically()				{	return hoverMotdSavesAutomatically;					}
	@Override public void setMotdSavingAutomatically(boolean state)			{	motdSavesAutomatically = state;						}
	@Override public void setHoverMotdSavingAutomatically(boolean state)	{	hoverMotdSavesAutomatically = state;				}
	
	@Override public String getFirstLine()									{	return firstLine;									}
	@Override public String getSecondLine()									{	return secondLine;									}
	@Override public String getMotd()										{	return firstLine + "\n" + secondLine;				}
	@Override public void setFirstLine(String line)							{	firstLine = line == null? "" : line;				}
	@Override public void setSecondLine(String line)						{	secondLine = line == null? "" : line;				}

	
	@Override public String getHoverMotdMetaLine(String metaName)	{	return hoverMotdMetaLines.getOrDefault(metaName, null);		}
	@Override public List<WrappedGameProfile> getHoverMotd()		{	return hoverMotd;											}



	@Override public String getHoverMotdLine(int indexOfLine) {
		if(indexOfLine > rawHoverMotdLines.size())	return null;
		return rawHoverMotdLines.get(indexOfLine-1);
	}
	
	@Override public void setHoverMotd(int indexOfLine, String line){
		line = ChatColor.translateAlternateColorCodes('&', line);
		if(indexOfLine > rawHoverMotdLines.size()){
			if(indexOfLine > 20){
				CoreUtils.io.sendConsoleMessageWarning("Kto� pr�bowa� doda� wiele linii do HoverMotd! Ze wzgl�d�w bezpiecze�stwa operacja zosta�a zablokowana.");
				return;
			}
			int emptyLinesToAdd = indexOfLine - rawHoverMotdLines.size();
			for(int i = 1; i < emptyLinesToAdd; i++)
				rawHoverMotdLines.add("");
			rawHoverMotdLines.add(line);
		} else
			rawHoverMotdLines.set(indexOfLine-1, line);
		assembleHover();
	}
	@Override public void setHoverMotd(List<String> newMotd){
		rawHoverMotdLines.clear();
		if(newMotd != null)
			for(String line : newMotd)
				rawHoverMotdLines.add(line.replace('&', ChatColor.COLOR_CHAR));
		assembleHover();
	}
	@Override public void setHoverMotdMetaLine(String metaName, String line){
		hoverMotdMetaLines.put(metaName, line);
		assembleHover();
	}
	@Override public void removeHoverMotdMetaLine(String metaName){
		hoverMotdMetaLines.remove(metaName);
		assembleHover();
	}
	@Override public void purgeHoverMotdMeta(){
		hoverMotdMetaLines.clear();
		assembleHover();
	}
	@Override public void removeHoverMotdLine(int indexOfLine){
		if(indexOfLine > rawHoverMotdLines.size())	return;
		rawHoverMotdLines.remove(indexOfLine - 1);
		assembleHover();
	}
	@Override public void restoreMotdFromConfig(){
		FileConfiguration config = Core.getFiles().getConfig(ConfigurationPath.GENERIC_SETTINGS);
		firstLine = ChatColor.translateAlternateColorCodes('&', config.getString("Motd.PierwszaLinijka"));
		secondLine = ChatColor.translateAlternateColorCodes('&', config.getString("Motd.DrugaLinijka"));
	}
	@Override public void restoreHoverMotdFromConfig(){
		FileConfiguration config = Core.getFiles().getConfig(ConfigurationPath.GENERIC_SETTINGS);
		rawHoverMotdLines.clear();
        for (String str : config.getStringList("HoverMotd.Linie"))
        	rawHoverMotdLines.add(str.replace('&', ChatColor.COLOR_CHAR));
        assembleHover();
	}
	
	@Override public void saveCurrentMotdToConfig(){
		YamlConfiguration config = Core.getFiles().getConfig(ConfigurationPath.GENERIC_SETTINGS);
		config.set("Motd.PierwszaLinijka", firstLine.replace(ChatColor.COLOR_CHAR, '&'));
		config.set("Motd.DrugaLinijka", secondLine.replace(ChatColor.COLOR_CHAR, '&'));
		Core.getFiles().saveConfig(config, ConfigurationPath.GENERIC_SETTINGS);
	}
	
	@Override public void saveCurrentHoverMotdToConfig(){
		YamlConfiguration config = Core.getFiles().getConfig(ConfigurationPath.GENERIC_SETTINGS);
		config.set("HoverMotd.Linie", rawHoverMotdLines.stream()
												.map(line -> line.replace(ChatColor.COLOR_CHAR, '&'))
												.collect(Collectors.toList()));
		Core.getFiles().saveConfig(config, ConfigurationPath.GENERIC_SETTINGS);
	}
	
	@SuppressWarnings("deprecation")
	private void assembleHover(){
		hoverMotd.clear();
		rawHoverMotdLines.forEach(line -> hoverMotd.add(new WrappedGameProfile("1", line)));
		if(hoverMotdMetaLines.size() > 0){
			hoverMotd.add(new WrappedGameProfile("1", ""));
			hoverMotdMetaLines.values().forEach(line -> hoverMotd.add(new WrappedGameProfile("1", line)));
		}
	}
}
