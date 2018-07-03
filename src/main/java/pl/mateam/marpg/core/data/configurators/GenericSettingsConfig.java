package pl.mateam.marpg.core.data.configurators;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;
import pl.mateam.marpg.api.regular.modules.sub.server.EnvironmentManager;
import pl.mateam.marpg.api.regular.modules.sub.server.MotdManager;
import pl.mateam.marpg.api.regular.modules.sub.server.OresManager;

@ReloaderName(name = CONFIGURATION_RELOADER.GENERIC_SETTINGS)
public class GenericSettingsConfig extends CommodoreConfigurationReloader {
	@Override public void action() {
		EnvironmentManager environment = Core.getServer().getEnvironment();
		YamlConfiguration config = Core.getFiles().getConfig(ConfigurationPath.GENERIC_SETTINGS);
		
		CoreUtils.io.setPolishSymbolsInConsole(config.getBoolean("Konsola.PolskieZnaki"));
		config.addDefault("CzestotliwoscZapisywania.DanePostaci", 30);
		environment.setCharacterInfoSavingFrequency(config.getInt("CzestotliwoscZapisywania.DanePostaci"));

		environment.setRegistrationAllowed(config.getBoolean("Ogolne.RejestracjaDozwolona"));
		environment.setStartOfTheNightHour(config.getInt("Ogolne.RozpoczecieNocy"));
		environment.setEndOfTheNightHour(config.getInt("Ogolne.KoniecNocy"));
		
		ConfigurationSection section = config.getConfigurationSection("Czat");
		environment.setMessageSendingDelayAtNight(section.getInt("Ustawienia.OgraniczenieNoc"));
		environment.setMessageSendingDelayAtDay(section.getInt("Ustawienia.OgraniczenieDzien"));
		
		environment.setChatMessagePrefix(ChatChanel.NORMAL, ChatColor.translateAlternateColorCodes('&', section.getString("Prefiksy.Glowny")));
		environment.setChatMessagePrefix(ChatChanel.TRADE, ChatColor.translateAlternateColorCodes('&', section.getString("Prefiksy.Handlowy")));
		environment.setChatMessagePrefix(ChatChanel.MODERATOR, ChatColor.translateAlternateColorCodes('&', section.getString("Prefiksy.Moderatorow")));
		environment.setChatMessagePrefix(ChatChanel.GAMEMASTER, ChatColor.translateAlternateColorCodes('&', section.getString("Prefiksy.Mistrzow")));
		environment.setChatMessagePrefix(ChatChanel.PRIVATE, ChatColor.translateAlternateColorCodes('&', section.getString("Prefiksy.Prywatny")));
				
		MotdManager motd = Core.getServer().getMotd();
		if(!motd.doesMotdSaveAutomatically())
			motd.restoreMotdFromConfig();
		if(!motd.doesHoverMotdSaveAutomatically())
			motd.restoreHoverMotdFromConfig();
		
		motd.setMotdSavingAutomatically(config.getBoolean("Motd.Dynamiczne"));
		motd.setHoverMotdSavingAutomatically(config.getBoolean("HoverMotd.Dynamiczne"));
		
		OresManager ores = Core.getServer().getOres();
		ores.setMinimalRegenerationTimeInSeconds(config.getInt("Gornictwo.MinCzasRegeneracjiRudy"));
		ores.setMaximalRegenerationTimeInSeconds(config.getInt("Gornictwo.MaksCzasRegeneracjiRudy"));
	}
}
