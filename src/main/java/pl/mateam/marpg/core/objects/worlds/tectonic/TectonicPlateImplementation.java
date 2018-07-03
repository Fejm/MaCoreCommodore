package pl.mateam.marpg.core.objects.worlds.tectonic;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationDirectory;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlate;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlateMusicManager;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlateOresManager;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlateSettingsManager;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;

public class TectonicPlateImplementation implements TectonicPlate {
	private final Map<Integer, TectonicPlate> subplates = new HashMap<>();
	public Collection<TectonicPlate> getAllSubplates()	{	return subplates.values();	}
	
	private final TectonicPlateImplementation parent;
	
	private TectonicPlateMobsManager mobs;
	private TectonicPlateSettingsManager settings;
	private TectonicPlateMusicManager music;
	private TectonicPlateOresManager ores;
	
	public TectonicPlateImplementation(TectonicPlateImplementation parent, String pathToPlate) {
		this.parent = parent;
		
		YamlConfiguration config = Core.getFiles().getConfig(MaCoreCommodoreEngine.getReference(), ConfigurationDirectory.TECTONIC.getPath() + "Warstwa " + getLayersCount() + ".yml");
		ConfigurationSection section = config.getConfigurationSection(pathToPlate);
		
		setupSettings(section);
		setupMusic(section);
		setupOres(section);
		
		setupSubPlates(pathToPlate);
	}

	
	@Override public TectonicPlateMobsManager getMobsManager(	) 		{	return mobs;		}
	@Override public TectonicPlateSettingsManager getSettingsManager() 	{	return settings;	}
	@Override public TectonicPlateMusicManager getMusicManager() 		{	return music;		}
	@Override public TectonicPlateOresManager getOresManager() 			{	return ores;		}

	@Override public int getLayersCount() {
		if(parent == null)
			return 0;
		else return parent.getLayersCount() + 1;
	}
	
	@Override public void spawnRandomMob() {
		todo;
	}
	
	@Secret public TectonicPlateImplementation getPlate(Location location){
		location.setY(getLayersCount());
		@SuppressWarnings("deprecation")
		Integer blockType = location.getBlock().getTypeId();
		if(!subplates.containsKey(blockType))
			return this;
		else
			return ((TectonicPlateImplementation) subplates.get(blockType)).getPlate(location);
	}
	
	@Secret public int getPlatesCount()	{
		if(subplates.size() == 0)	return 1;
		AtomicInteger count = new AtomicInteger();
		subplates.values().stream().forEach(v -> count.addAndGet(((TectonicPlateImplementation) v).getPlatesCount()));
		return count.get();
	}
	
	
	
	
	private void setupSettings(ConfigurationSection section){
		this.settings = new TectonicPlateSettingsManagerImplementation(this);
		if(section.contains("Ustawienia")){
			ConfigurationSection cs = section.getConfigurationSection("Ustawienia");
			if(cs.contains("Pojedynki"))
				settings.setDuelingAllowed(cs.getBoolean("Pojedynki"), false);
			if(cs.contains("Deathmatch"))
				settings.setDeathmatchAllowed(cs.getBoolean("Deathmatch"), false);
			if(cs.contains("Respawn"))
				settings.setRespawnPoint(cs.getString("Respawn"), false);
			if(cs.contains("Nazwa"))
				settings.setName(cs.getString("Nazwa"), false);
		}
	}
	
	private void setupMusic(ConfigurationSection section){
		this.music = new TectonicPlateMusicManagerImplementation(this);
		if(section.contains("Muzyka")){
			ConfigurationSection settings = section.getConfigurationSection("Muzyka");
			if(settings.contains("Dzien")) {
				for(String songName : settings.getStringList("Dzien")) {
					if(songName.charAt(0) == '-')
						music.removeDaytimeSong(songName, false);
					else
						music.addDaytimeSong(songName, false);
				}
			}
			if(settings.contains("Noc")){
				for(String songName : settings.getStringList("Noc")) {
					if(songName.charAt(0) == '-')
						music.removeNighttimeSong(songName, false);
					else
						music.addNighttimeSong(songName, false);
				}
			}
		}
	}
	
	private void setupOres(ConfigurationSection section){
		this.ores = new TectonicPlateOresManagerImplementation(this);
		if(section.contains("Rudy")) {
			ConfigurationSection settings = section.getConfigurationSection("Rudy");
			boolean relative = settings.getBoolean("WartosciWzgledne");
			int[] chances = new int[8];
			for(String line : settings.getStringList("Spis")) {
				String[] info = line.split(" ");
				Integer type = Integer.parseInt(info[0]);
				Integer chance = Integer.parseInt(info[1]);
				chances[type-1] = chance;
			}
			ores.setOreChances(chances, relative);
		}
	}
	
	private void setupSubPlates(String pathToPlate) {
		YamlConfiguration newConfig = Core.getFiles().getConfig(MaCoreCommodoreEngine.getReference(), ConfigurationDirectory.TECTONIC.getPath() + "Warstwa " + (getLayersCount() + 1) + ".yml");
		if(newConfig != null && newConfig.getConfigurationSection(pathToPlate) != null){
			ConfigurationSection subSection = newConfig.getConfigurationSection(pathToPlate);
			for(String subplateID : subSection.getKeys(false)){
				int subplateBlockID = Integer.parseInt(subSection.getString(subplateID + "." + "ID"));
				TectonicPlate newPlate = new TectonicPlateImplementation(this, pathToPlate + "." + subplateID);
				subplates.put(subplateBlockID, newPlate);
			}
		}
	}
}
