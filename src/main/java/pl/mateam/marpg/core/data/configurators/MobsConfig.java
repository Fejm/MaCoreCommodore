package pl.mateam.marpg.core.data.configurators;

import java.util.function.Consumer;
import java.util.function.Supplier;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.EntityType;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationDirectory;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.modules.external.server.sub.WorldsManagerImplementation;
import pl.mateam.marpg.core.objects.worlds.mobs.MobFamily;
import pl.mateam.marpg.core.objects.worlds.mobs.MobFamily.DropElement;

@ReloaderName(name = CONFIGURATION_RELOADER.MOBS)
public class MobsConfig extends CommodoreConfigurationReloader {
	@Override public void action() {
		WorldsManagerImplementation worlds = (WorldsManagerImplementation) Core.getServer().getWorlds();
		int layerNumber = 0;
		YamlConfiguration config = Core.getFiles().getConfig(MaCoreCommodoreEngine.getReference(), ConfigurationDirectory.MOBS.getPath() + "Warstwa " + layerNumber + ".yml");
		while(config != null) {
			final YamlConfiguration configCopy = config;
			config.getKeys(false).forEach(name -> {
				final ConfigurationSection section = configCopy.getConfigurationSection(name);
				MobFamily family = null;
				String parentName = section.getString("Dziedziczenie");
				String type = section.getString("Typ");
				Integer appearance = section.getInt("Wyglad");
				if(parentName != null) {
					if(type != null)
						family = worlds.buildMobFamily(name, parentName, EntityType.valueOf(type.toUpperCase()));
					else
						if(appearance != null)
							family = worlds.buildMobFamily(name, parentName, appearance);
						else
							family = worlds.buildMobFamily(name, parentName);
				} else {
					if(type != null)
						family = worlds.buildMobFamily(name, EntityType.valueOf(type.toUpperCase()));
					else
						family = worlds.buildMobFamily(name, appearance);
				}
				assignValue(section, () -> section.getDouble("MnoznikPoteflonow"), family::setMoneyMultiplier);
				assignValue(section, () -> section.getDouble("MnoznikZycia"), family::setHealthMultiplier);
				assignValue(section, () -> section.getDouble("MnoznikAtaku"), family::setDamageMultiplier);
				ConfigurationSection drops = section.getConfigurationSection("Drop");
				if(drops != null) {
					drops.getKeys(false).forEach(ID -> {
						ConfigurationSection dropsection = drops.getConfigurationSection(ID);
						float dropChance = (float) dropsection.getDouble("Szansa");
						DropElement drop = new MobFamily.DropElement(Integer.parseInt(ID), dropChance);
						assignValue(dropsection, () -> dropsection.getInt("BonusDoBonusow"), drop::setBonusesBonus);
						assignValue(dropsection, () -> dropsection.getInt("BonusDoTieru"), drop::setTierBonus);
					});
				}
			});
			layerNumber++;
			config = Core.getFiles().getConfig(MaCoreCommodoreEngine.getReference(), ConfigurationDirectory.MOBS.getPath() + "Warstwa " + layerNumber + ".yml");
		}
	}
	
	private <T> void assignValue(ConfigurationSection section, Supplier<T> supplier, Consumer<T> action) {
		T value = supplier.get();
		if(value != null)
			action.accept(value);
	}
}
