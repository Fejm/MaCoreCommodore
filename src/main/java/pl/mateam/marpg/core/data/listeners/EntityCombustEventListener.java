package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityCombustEvent;

public class EntityCombustEventListener implements Listener{
	@EventHandler(priority = EventPriority.HIGH)
	public void on(EntityCombustEvent event){
		event.setCancelled(true);
		event.setDuration(0);
	}
}
