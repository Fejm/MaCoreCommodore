package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.world.StructureGrowEvent;

public class BlockGrowEventListener implements Listener {
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(StructureGrowEvent event){
		event.setCancelled(true);
	}
}
