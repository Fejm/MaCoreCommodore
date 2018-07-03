package pl.mateam.marpg.api.regular.objects.worlds.tectonic;

import org.bukkit.Location;

public interface TectonicPlateSettingsManager extends TectonicPlateModule<TectonicPlateSettingsManager> {
	Boolean isDeathmatchAllowed();
	Boolean isDuelingAllowed();
	Location getRespawnPoint();
	String getName();
	void setDeathmatchAllowed(Boolean state, boolean updateSubplates);	//null value for state informs that value will be retrivered from parent plate
	void setDuelingAllowed(Boolean state, boolean updateSubplates);		//null value for state informs that value will be retrivered from parent plate
	void setRespawnPoint(String respawnPointName, boolean updateSubplates);
	void setName(String name, boolean updateSubplates);
	void setDeathmatchAllowedParentPlateValue();
	void setDuelingAllowedParentPlateValue();
	void setRespawnLocationParentPlateValue();
	void setNameParentPlateValue();
}