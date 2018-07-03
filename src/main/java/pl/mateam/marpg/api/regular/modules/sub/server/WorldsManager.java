package pl.mateam.marpg.api.regular.modules.sub.server;

import org.bukkit.Location;
import org.bukkit.World;

import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlate;
import pl.mateam.marpg.core.objects.worlds.mobs.MobFamily;

public interface WorldsManager {
	int getTectonicPlatesCount(World world);
	
	TectonicPlate getTectonicPlate(Location location);
	
	void spawnMobFreely(MobFamily family, int level, Location location);
	
	void putWarp(String locationName, Location location);
	Location getWarp(String locationName);

	void replenishAllOres(boolean playEffect);
}
