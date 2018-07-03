package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerShearEntityEvent;

public class PlayerShearEntityEventListener implements Listener{	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(final PlayerShearEntityEvent event) {
		event.setCancelled(true);
	}
}
