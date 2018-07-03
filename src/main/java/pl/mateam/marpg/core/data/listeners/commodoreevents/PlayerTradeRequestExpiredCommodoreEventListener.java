package pl.mateam.marpg.core.data.listeners.commodoreevents;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import pl.mateam.marpg.api.regular.events.PlayerTradeRequestExpiredCommodoreEvent;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;

public class PlayerTradeRequestExpiredCommodoreEventListener implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(PlayerTradeRequestExpiredCommodoreEvent event) {
		((ExtendedPlayerImplementation) event.getRequestSender()).handle(event);
	}
}
