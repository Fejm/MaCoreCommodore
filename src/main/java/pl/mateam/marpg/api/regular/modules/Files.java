package pl.mateam.marpg.api.regular.modules;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationFile;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;

public interface Files {
	YamlConfiguration getConfig(ConfigurationPath config);
	YamlConfiguration getConfig(CommodoreComponent component, String relativePath);
	YamlConfiguration getConfig(String componentName, String relativePath);
	
	void saveConfig(FileConfiguration editedFileToSave, ConfigurationPath config);
	void saveConfig(FileConfiguration editedFileToSave, CommodoreComponent component, String relativePathToFile);
	void saveConfig(FileConfiguration editedFileToSave, String componentName, String relativePath);
	
	void reloadConfiguration(String pluginName, String configurationKey);
	void reloadConfiguration(ConfigurationFile engineConfigurationFile);
	void reloadConfiguration(CommodoreComponent component, Class<? extends CommodoreConfigurationReloader> reloader);
	void reloadConfiguration(String pluginName);
	void reloadWholeConfiguration();

	void restoreDefaultConfigurationFile(ConfigurationPath file);
	void regenerateDefaultConfigurationFile(ConfigurationPath file);
	void regenerateWholeDefaultConfiguration();
	void restoreWholeDefaultConfiguration();
	
	int getReloadersAmount(CommodoreComponent component);
	
	void addConfiguratorToRegistry(CommodoreComponent component, Class<? extends CommodoreConfigurationReloader> configurator);
	void addConfiguratorToRegistry(String componentName, Class<? extends CommodoreConfigurationReloader> configurator);
	void removeConfiguratorFromRegistry(CommodoreComponent component, String configurator);
	void removeConfiguratorFromRegistry(String componentName, String configurator);
	void removeConfiguratorFromRegistry(CommodoreComponent component, Class<? extends CommodoreConfigurationReloader> configurator);
	void removeAllConfiguratorsFromRegistry(CommodoreComponent component);
	void removeAllConfiguratorsFromRegistry(String componentName);
	void addAllConfiguratorsToRegistry(CommodoreComponent component);
	void addAllConfiguratorsToRegistryUsingName(String componentName);
}