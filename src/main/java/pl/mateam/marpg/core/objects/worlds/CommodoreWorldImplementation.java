package pl.mateam.marpg.core.objects.worlds;

import org.bukkit.Location;
import org.bukkit.World;

import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlate;
import pl.mateam.marpg.core.objects.worlds.tectonic.TectonicPlateImplementation;

public class CommodoreWorldImplementation {
	private final World bukkitWorld;
	private TectonicPlateImplementation corePlate;
	private int platesCount;
	
	public CommodoreWorldImplementation(World bukkitWorld) {
		this.bukkitWorld = bukkitWorld;
		this.corePlate = new TectonicPlateImplementation(null, bukkitWorld.getName());
		platesCount = corePlate.getPlatesCount();
	}

	public TectonicPlate getTectonicPlate(Location location)	{	return corePlate.getPlate(location);	}
	public TectonicPlate getMainTectonicPlate()					{	return corePlate;						}
	public int getTectonicPlatesCount()							{	return platesCount;						}
	
	public void reloadTectonicInfoFromConfig() {
		corePlate = new TectonicPlateImplementation(null, bukkitWorld.getName());
		platesCount = corePlate.getPlatesCount();
	}
}