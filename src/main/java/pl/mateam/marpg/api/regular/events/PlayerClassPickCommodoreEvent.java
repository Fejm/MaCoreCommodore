package pl.mateam.marpg.api.regular.events;

import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.regular.classes.CommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;

public class PlayerClassPickCommodoreEvent extends CommodoreEvent {
	@Override public HandlerList getHandlers() {	return handlers;	}
	public static HandlerList getHandlerList() {	return handlers;	}
	private static final HandlerList handlers = new HandlerList();

	private final ExtendedPlayer extPlayer;
	public PlayerClassPickCommodoreEvent(ExtendedPlayer extPlayer) {
		this.extPlayer = extPlayer;
	}
	
	public ExtendedPlayer getUser()	{	return extPlayer;	}
}
