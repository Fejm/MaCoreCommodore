package pl.mateam.marpg.core.data.configurators;

import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;
import pl.mateam.marpg.core.internal.utils.Parsers;

@ReloaderName(name = CONFIGURATION_RELOADER.SKINS)
public class SkinsConfig extends CommodoreConfigurationReloader {
	@Override public void action() {
		YamlConfiguration config = Core.getFiles().getConfig(ConfigurationPath.SKINS);
		try{
			Set<String> skins = config.getKeys(false);
			int renderedSkins = 0;
			for(String skin : skins){
				String value = config.getString(skin + ".Tekstura");
				String signature = config.getString(skin + ".Sygnatura");
				if(value != null && signature != null){
					Core.getServer().getSkins().render(skin, value, signature, true);
					renderedSkins++;
				}
				else
					CoreUtils.io.sendConsoleMessageWarningWithHighlight("Błąd podczas renderowania skina ", skin, "!");
			}
			if(renderedSkins != 0){
				String word = Parsers.getProperForm(renderedSkins, " skin", " skiny", " skinów");					
				CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Wyrenderowano ", String.valueOf(renderedSkins), word);
			}
		} catch(Exception e){
			e.printStackTrace();
		}
	}
}
