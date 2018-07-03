package pl.mateam.marpg.core.data.configurators;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;
import pl.mateam.marpg.core.internal.utils.Parsers;

@ReloaderName(name = CONFIGURATION_RELOADER.MUSIC)
public class MusicConfig extends CommodoreConfigurationReloader {
	@Override public void action() {
		YamlConfiguration config = Core.getFiles().getConfig(ConfigurationPath.MUSIC);
		
		Core.getServer().getSongs().setIntervalBetweenSongs(config.getInt("PrzerwaMiedzyUtworami"));
		ConfigurationSection songs = config.getConfigurationSection("Utwory");
		int registeredSongs = 0;
		for(String song : songs.getKeys(false)){
			Core.getServer().getSongs().add(song, songs.getInt(song));
			registeredSongs++;
		}
		
		if(registeredSongs > 0){
			String word = Parsers.getProperForm(registeredSongs, " piosenkę", " piosenki", " piosenek");
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(registeredSongs), word);
		}
	}
}
