package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.core.objects.users.AnyUserImplementation;

public class PlayerQuitEventListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerQuitEvent event){
		AnyUserImplementation user = (AnyUserImplementation) Core.getServer().getUsers().getGenericObject(event.getPlayer());
		user.hasQuit(event);
		event.setQuitMessage(null);
	}
}
