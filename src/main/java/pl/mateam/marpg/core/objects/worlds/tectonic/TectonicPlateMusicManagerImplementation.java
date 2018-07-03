package pl.mateam.marpg.core.objects.worlds.tectonic;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlate;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlateMusicManager;

public class TectonicPlateMusicManagerImplementation extends AbstractTectonicPlateManager<TectonicPlateMusicManager> implements TectonicPlateMusicManager {
	private final List<String> daytimeAllowedSongsNames = new ArrayList<>();
	private final List<String> nighttimeAllowedSongsNames = new ArrayList<>();
	private final List<String> excludedDaytimeAllowedSongsNames = new ArrayList<>();
	private final List<String> excludedNighttimeAllowedSongsNames = new ArrayList<>();
	
	public TectonicPlateMusicManagerImplementation(TectonicPlate parentPlate) {
		super(parentPlate, TectonicPlate::getMusicManager);
	}

	@Override public void setParentController(TectonicPlateMusicManager parentController)	{	super.setParentController(parentController);	}
	@Override public void restoreNaturalParent() 											{		super.restoreNaturalParent();				}

	@Override public void addDaytimeSong(String songName, boolean updateSubplates) {
		if(parent != null && parent.playsAtDaytime(songName)) {
			if(excludedDaytimeAllowedSongsNames.contains(songName))
				excludedDaytimeAllowedSongsNames.remove(songName);
		}

		if(!daytimeAllowedSongsNames.contains(songName))				//Manually added songs will be present on the list even after removing of parent.
			daytimeAllowedSongsNames.add(songName);
		
		if(updateSubplates)
			updateSubplates(plate -> plate.getMusicManager().addDaytimeSong(songName, true));
	}

	@Override public void addNighttimeSong(String songName, boolean updateSubplates) {
		if(parent != null && parent.playsAtNighttime(songName)){
			if(excludedNighttimeAllowedSongsNames.contains(songName))
				excludedNighttimeAllowedSongsNames.remove(songName);
		}

		if(!nighttimeAllowedSongsNames.contains(songName))
			nighttimeAllowedSongsNames.add(songName);
		
		if(updateSubplates)
			updateSubplates(plate -> plate.getMusicManager().addNighttimeSong(songName, true));
	}

	@Override public void removeDaytimeSong(String songName, boolean updateSubplates) {
		if(parent != null && parent.playsAtDaytime(songName)){
			if(!excludedDaytimeAllowedSongsNames.contains(songName))
				excludedDaytimeAllowedSongsNames.add(songName);
		}
		if(daytimeAllowedSongsNames.contains(songName))
			daytimeAllowedSongsNames.remove(songName);
		
		if(updateSubplates)
			updateSubplates(plate -> plate.getMusicManager().removeDaytimeSong(songName, true));
	}

	@Override public void removeNighttimeSong(String songName, boolean updateSubplates) {
		if(parent != null && parent.playsAtDaytime(songName)){
			if(!excludedNighttimeAllowedSongsNames.contains(songName))
				excludedNighttimeAllowedSongsNames.add(songName);
		}
		if(nighttimeAllowedSongsNames.contains(songName))
			nighttimeAllowedSongsNames.remove(songName);
		
		if(updateSubplates)
			updateSubplates(plate -> plate.getMusicManager().addNighttimeSong(songName, true));
	}

	@Override public List<String> getDaytimeAllowedSongsNames() {
		if(parent != null){
			List<String> list = parent.getDaytimeAllowedSongsNames();
			list.stream().filter(v -> !excludedDaytimeAllowedSongsNames.contains(v));
			list.addAll(daytimeAllowedSongsNames);
			return new ArrayList<>(list);
		} else
			return new ArrayList<>(daytimeAllowedSongsNames);
	}

	@Override public List<String> getNighttimeAllowedSongsNames() {
		if(parent != null){
			List<String> list = parent.getNighttimeAllowedSongsNames();
			list.stream().filter(v -> !excludedNighttimeAllowedSongsNames.contains(v));
			list.addAll(nighttimeAllowedSongsNames);
			return new ArrayList<>(list);
		} else
			return new ArrayList<>(nighttimeAllowedSongsNames);
	}

	@Override public String getRandomSongName(){
		boolean isDay = CoreUtils.ingame.isDay();
		List<String> songNames = isDay? getDaytimeAllowedSongsNames() : getNighttimeAllowedSongsNames();
		if(songNames.size() == 0)
			return null;
		int random = new Random().nextInt(songNames.size());
		return songNames.get(random);
	}

	@Override public boolean playsAtDaytime(String songName){
		if(excludedDaytimeAllowedSongsNames.contains(songName))
			return false;
		if(daytimeAllowedSongsNames.contains(songName))
			return true;
		if(parent != null)
			return parent.playsAtDaytime(songName);
		else return false;
	}
	
	@Override public boolean playsAtNighttime(String songName){
		if(excludedNighttimeAllowedSongsNames.contains(songName))
			return false;
		if(nighttimeAllowedSongsNames.contains(songName))
			return true;
		if(parent != null)
			return parent.playsAtNighttime(songName);
		else return false;
	}

	@Override public boolean plays(String songName)		{	return playsAtDaytime(songName) || playsAtNighttime(songName);	}
}
