package pl.mateam.marpg.api.regular.objects.worlds.tectonic;

import java.util.List;


public interface TectonicPlateMusicManager extends TectonicPlateModule<TectonicPlateMusicManager> {
	void addDaytimeSong(String songName, boolean updateSubplates);
	void addNighttimeSong(String songName, boolean updateSubplates);
	void removeDaytimeSong(String songName, boolean updateSubplates);
	void removeNighttimeSong(String songName, boolean updateSubplates);

	List<String> getDaytimeAllowedSongsNames();
	List<String> getNighttimeAllowedSongsNames();
	String getRandomSongName();
	boolean playsAtDaytime(String songName);
	boolean playsAtNighttime(String songName);
	boolean plays(String songName);
}
