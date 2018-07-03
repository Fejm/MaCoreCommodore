package pl.mateam.marpg.core.data.listeners;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;
import org.bukkit.event.entity.CreatureSpawnEvent.SpawnReason;

import pl.mateam.marpg.api.Core;

public class CreatureSpawnEventListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(CreatureSpawnEvent event) {
		SpawnReason reason = event.getSpawnReason();
		if(reason.equals(SpawnReason.CUSTOM))
			return;
		if(reason.equals(SpawnReason.SPAWNER)) {
			event.setCancelled(true);
			Location location = event.getLocation();
			World world = location.getWorld();
			
			for(int i = location.getBlockY(); i < 256-location.getBlockY(); i++) {
				location.setY(location.getY() + 1);
				if(!world.getBlockAt(location).isEmpty()){
					Location location2 = location.clone();
					location2.setY(location2.getY() + 3);
					if(world.getBlockAt(location2).isEmpty()) {
						Core.getServer().getWorlds().getTectonicPlate(location2).spawnRandomMob();
						return;		
					}
				}
			}
		}
	}
}
