package pl.mateam.marpg.core.data.listeners;

import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityInteractEvent;

public class EntityInteractEventListener implements Listener {	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(EntityInteractEvent event){
		if (event.getEntityType() != EntityType.PLAYER)
			if(event.getBlock().getType().equals(Material.SOIL))
				event.setCancelled(true);
	}
}
