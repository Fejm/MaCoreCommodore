package pl.mateam.marpg.core.modules.external.files;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationFile;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;
import pl.mateam.marpg.api.regular.modules.Files;
import pl.mateam.marpg.core.CommodoreCore;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;

public class FilesImplementation implements Files {
	@Override public void regenerateWholeDefaultConfiguration() 																				{	CommodoreCore.getHiddenMethods().unpackResources(MaCoreCommodoreEngine.getReference(), false);		}
	@Override public void restoreWholeDefaultConfiguration()																					{	CommodoreCore.getHiddenMethods().unpackResources(MaCoreCommodoreEngine.getReference(), true);		}
	@Override public void restoreDefaultConfigurationFile(ConfigurationPath file) 																{	MaCoreCommodoreEngine.getReference().restoreOriginalResource(file.getPath());						}
	@Override public void regenerateDefaultConfigurationFile(ConfigurationPath file) 															{	MaCoreCommodoreEngine.getReference().regenerateResource(file.getPath());							}

	
	private final Map<String, AddOnConfigurationStorage> addOnsConfiguration = new HashMap<>();
	private AddOnConfigurationStorage forceGetStorage(CommodoreComponent component){
		AddOnConfigurationStorage storage = addOnsConfiguration.get(component.getName());
		if(storage == null){
			storage = new AddOnConfigurationStorage(component);
			addOnsConfiguration.put(component.getName(), storage);
		}
		return storage;
	}
	
	
	@Override public int getReloadersAmount(CommodoreComponent component)		{
		if(addOnsConfiguration.get(component.getName()) == null)	return 0;
		return addOnsConfiguration.get(component.getName()).getSize();
	}
	
	@Override public YamlConfiguration getConfig(CommodoreComponent component, String relativePathToFile){
		File configFile = new File(component.getDataFolder().getPath(), relativePathToFile);
		if(!configFile.exists()) return null;
		return YamlConfiguration.loadConfiguration(configFile);
	}
	
	@Override public YamlConfiguration getConfig(String component, String relativePathToFile){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(component);
		if(plugin != null){
			File configFile = new File(plugin.getDataFolder().getPath(), relativePathToFile);
			if(!configFile.exists()) return null;
			return YamlConfiguration.loadConfiguration(configFile);
		}
		return null;
	}
	
	@Override public YamlConfiguration getConfig(ConfigurationPath config) 																		{	return getConfig(MaCoreCommodoreEngine.getReference(), config.getPath());							}
	
	@Override public void saveConfig(FileConfiguration editedFileToSave, CommodoreComponent component, String relativePathToFile) {
		try {
			File file = new File(component.getDataFolder().getPath(), relativePathToFile);
			if(!file.exists()){
				file.getParentFile().mkdirs();
				file.createNewFile();
			}
			editedFileToSave.save(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	@Override public void saveConfig(FileConfiguration editedFileToSave, String component, String relativePathToFile) {
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(component);
		if(plugin != null && plugin instanceof CommodoreComponent)
			saveConfig(editedFileToSave, (CommodoreComponent) plugin, relativePathToFile);
	}
	@Override public void saveConfig(FileConfiguration editedFileToSave, ConfigurationPath config) {
		saveConfig(editedFileToSave, MaCoreCommodoreEngine.getReference(), config.getPath());
	}
	
	@Override public void addConfiguratorToRegistry(CommodoreComponent component, Class<? extends CommodoreConfigurationReloader> file){
		AddOnConfigurationStorage storage = forceGetStorage(component);
		storage.add(file, null);
	}
	
	@Override public void addConfiguratorToRegistry(String componentName, Class<? extends CommodoreConfigurationReloader> file){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(componentName);
		if(plugin instanceof CommodoreComponent)
			addConfiguratorToRegistry((CommodoreComponent) plugin, file);
	}
	
	@Override public void addAllConfiguratorsToRegistryUsingName(String componentName){
		Plugin plugin = Bukkit.getServer().getPluginManager().getPlugin(componentName);
		if(plugin instanceof CommodoreComponent)
			addAllConfiguratorsToRegistry((CommodoreComponent) plugin);
	}
	
	@Override public void removeConfiguratorFromRegistry(CommodoreComponent component, String reloader)											{	removeConfiguratorFromRegistry(component.getName(), reloader);							}

	@Override public void removeConfiguratorFromRegistry(String componentName, String fileName){
		AddOnConfigurationStorage storage = addOnsConfiguration.get(componentName);
		if(storage != null)
			if(storage.remove(fileName))
				addOnsConfiguration.remove(componentName);
	}
	
	@Override public void removeConfiguratorFromRegistry(CommodoreComponent component, Class<? extends CommodoreConfigurationReloader> file){
		AddOnConfigurationStorage storage = addOnsConfiguration.get(component.getName());
		if(storage != null)
			if(file.isAnnotationPresent(ReloaderName.class))
				if(storage.remove(file.getAnnotation(ReloaderName.class).name()))
					addOnsConfiguration.remove(component.getName());
	}

	@Override public void removeAllConfiguratorsFromRegistry(CommodoreComponent component)														{	removeAllConfiguratorsFromRegistry(component.getName());								}

	@Override public void removeAllConfiguratorsFromRegistry(String componentName){
		addOnsConfiguration.remove(componentName);
	}
	@Override public void addAllConfiguratorsToRegistry(CommodoreComponent addOn){
		AddOnConfigurationStorage storage = forceGetStorage(addOn);
		storage.addAll();
	}
	
	
	@Override public void reloadConfiguration(ConfigurationFile engineConfigurationFile){
		reloadConfiguration(MaCoreCommodoreEngine.getReference().getName(), engineConfigurationFile.getReloaderName());
	}

	@Override public void reloadConfiguration(String pluginName, String configurationKey){
		AddOnConfigurationStorage storage = addOnsConfiguration.get(pluginName);
		if(storage != null)
			storage.reload(configurationKey);
	}
	@Override public void reloadConfiguration(CommodoreComponent component, Class<? extends CommodoreConfigurationReloader> reloader){
		AddOnConfigurationStorage storage = addOnsConfiguration.get(component.getName());
		if(storage != null){
			if(reloader.isAnnotationPresent(ReloaderName.class))
				storage.reload(reloader.getAnnotation(ReloaderName.class).name());
		}
	}
	@Override public void reloadConfiguration(String pluginName){
		AddOnConfigurationStorage storage = addOnsConfiguration.get(pluginName);
		if(storage != null)
			storage.reloadAll();
	}
	@Override public void reloadWholeConfiguration(){
		addOnsConfiguration.values().forEach(storage -> storage.reloadAll());
	}
}