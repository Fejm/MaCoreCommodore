package pl.mateam.marpg.core.data.configurators;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;
import pl.mateam.marpg.core.internal.utils.Parsers;

@ReloaderName(name = CONFIGURATION_RELOADER.LOCATIONS)
public class LocationsConfig extends CommodoreConfigurationReloader {
	@Override public void action() {		
		register(ConfigurationPath.LOCATIONS, ConfigurationPath.LOCATIONS_OTHER);
	}
	
	private void register(ConfigurationPath... configPaths){
		AtomicInteger counter = new AtomicInteger();
		for(ConfigurationPath configPath : configPaths){
			YamlConfiguration config = Core.getFiles().getConfig(configPath);
			config.getKeys(false).forEach(worldName -> {
				ConfigurationSection section = config.getConfigurationSection(worldName);
				World world = Bukkit.getServer().getWorld(worldName);
				section.getKeys(false).forEach(locationName -> {
					ConfigurationSection local = section.getConfigurationSection(locationName);
					double x = local.getDouble("x");
					double y = local.getDouble("y");
					double z = local.getDouble("z");
					float yaw = (float) local.getDouble("yaw");
					float pitch = (float) local.getDouble("pitch");
					Location location = new Location(world, x, y, z, yaw, pitch);
					Core.getServer().getWorlds().putWarp(locationName, location);
					counter.incrementAndGet();
				});
			});
		}
		int count = counter.get(); 
		if(count != 0){
			String word = Parsers.getProperForm(count, " lokację", " lokacje", " lokacji");
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(count), word);
		}
	}
}
