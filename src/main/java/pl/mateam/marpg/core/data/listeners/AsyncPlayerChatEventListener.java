package pl.mateam.marpg.core.data.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;
import pl.mateam.marpg.core.objects.users.AnyUserImplementation;

public class AsyncPlayerChatEventListener implements Listener {	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)	//ignoreCancelled = true allows add-ons to skip this event
	public void on(AsyncPlayerChatEvent event) {
		event.setCancelled(true);
		Player player = event.getPlayer();

		AnyUser playerObject = Core.getServer().getUsers().getGenericObject(player);
		if(playerObject != null)
			((AnyUserImplementation) playerObject).handleMessageSending(event);
	}
}

