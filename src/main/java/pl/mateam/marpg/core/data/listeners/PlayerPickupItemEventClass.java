package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerPickupItemEvent;

import pl.mateam.marpg.api.Core;

@SuppressWarnings("deprecation")
public class PlayerPickupItemEventClass implements Listener {
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)	//Make sure that GM won't pickup any items anyway!
    public void on(PlayerPickupItemEvent event) {
    	if(Core.getServer().getUsers().getGamemasterObject(event.getPlayer()) != null){
    		event.setCancelled(true);
    		return;
    	}
    }
}
