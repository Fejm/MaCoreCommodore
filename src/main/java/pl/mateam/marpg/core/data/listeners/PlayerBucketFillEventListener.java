package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBucketFillEvent;

public class PlayerBucketFillEventListener implements Listener {	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(PlayerBucketFillEvent event){
		event.setCancelled(true);
	}
}
