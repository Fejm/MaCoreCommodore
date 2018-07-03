package pl.mateam.marpg.core.objects.worlds.tectonic;

import org.bukkit.Location;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlate;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlateSettingsManager;

public class TectonicPlateSettingsManagerImplementation extends AbstractTectonicPlateManager<TectonicPlateSettingsManager> implements TectonicPlateSettingsManager {
	private static boolean coreDeathmatchAllowed = false;
	private static boolean coreDuelingAllowed = true;
	private static String coreRespawnLocationName = null;
	private static String coreName = "Nieznane Kresy";
	
	private Boolean deathmatchAllowed = false;
	private Boolean duelingAllowed = true;
	private String respawnLocationName = null;
	private String name = "Nieznane Kresy";
	
	public TectonicPlateSettingsManagerImplementation(TectonicPlate parentPlate) {
		super(parentPlate, TectonicPlate::getSettingsManager);
	}

	@Override public Boolean isDeathmatchAllowed() 	{	return getValue(deathmatchAllowed, parent::isDeathmatchAllowed, coreDeathmatchAllowed);		}
	@Override public Boolean isDuelingAllowed() 	{	return getValue(duelingAllowed, parent::isDuelingAllowed, coreDuelingAllowed);				}
	@Override public String getName() 				{	return getValue(name, parent::getName, coreName);											}

	@Override public Location getRespawnPoint(){
		if(respawnLocationName != null)
			return Core.getServer().getWorlds().getWarp(respawnLocationName);
		else
			if(parent == null)
				throw new RuntimeException("Nie określono punktu respawnu dla głównej płyty!");
			else
				return parent.getRespawnPoint();
	}


	@Override public void setDeathmatchAllowed(Boolean state, boolean updateSubplates) {
		this.deathmatchAllowed = state;
		if(updateSubplates)
			updateSubplates(plate -> plate.getSettingsManager().setDeathmatchAllowed(state, true));
	}
	@Override public void setDuelingAllowed(Boolean state, boolean updateSubplates) {
		this.duelingAllowed = state;
		if(updateSubplates)
			updateSubplates(plate -> plate.getSettingsManager().setDuelingAllowed(state, true));
	}
	@Override public void setRespawnPoint(String respawnPointName, boolean updateSubplates){
		this.respawnLocationName = respawnPointName;
		if(updateSubplates)
			updateSubplates(plate -> plate.getSettingsManager().setRespawnPoint(respawnPointName, true));
	}
	@Override public void setName(String name, boolean updateSubplates)	{
		this.name = name;
		if(updateSubplates)
			updateSubplates(plate -> plate.getSettingsManager().setName(name, true));
	}

	@Override public void setDeathmatchAllowedParentPlateValue() 	{	this.deathmatchAllowed = getParentValue(parent::isDeathmatchAllowed, coreDeathmatchAllowed);	}
	@Override public void setDuelingAllowedParentPlateValue() 		{	this.duelingAllowed = getParentValue(parent::isDuelingAllowed, coreDuelingAllowed);				}
	@Override public void setNameParentPlateValue() 				{	this.name = getParentValue(parent::getName, coreName);											}
	
	@Override public void setRespawnLocationParentPlateValue() {
		if(parent == null)
			this.respawnLocationName = coreRespawnLocationName;
		else
			this.respawnLocationName = ((TectonicPlateSettingsManagerImplementation) parent).respawnLocationName;
	}
	
	@Override public void setParentController(TectonicPlateSettingsManager parent)	{	super.setParentController(parent); 	}
	@Override public void restoreNaturalParent() 									{	super.restoreNaturalParent();		}
}
