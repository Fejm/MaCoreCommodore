package pl.mateam.marpg.core.data.listeners.commodoreevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import pl.mateam.marpg.api.regular.events.PlayerInventoryChangeCommodoreEvent;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;

public class PlayerInventoryChangeCommodoreEventListener implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(PlayerInventoryChangeCommodoreEvent event) {
		if(event.getUser() instanceof ExtendedPlayerImplementation)
			((ExtendedPlayerImplementation) event.getUser()).handle(event);
	}
}
