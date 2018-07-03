package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class PlayerJoinEventListener implements Listener {	
	@EventHandler(priority = EventPriority.HIGH)
	public void on(PlayerJoinEvent event){
		event.setJoinMessage(null);
	}
}
