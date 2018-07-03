package pl.mateam.marpg.api.regular.events;

import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.regular.classes.CommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;

public class PlayerPvPRequestExpiredCommodoreEvent extends CommodoreEvent {
	@Override public HandlerList getHandlers() {	return handlers;	}
	public static HandlerList getHandlerList() {	return handlers;	}
	private static final HandlerList handlers = new HandlerList();
	
	private final ExtendedPlayer requestSender;
	private final ExtendedPlayer requestedUser;
	public PlayerPvPRequestExpiredCommodoreEvent(ExtendedPlayer requestSender, ExtendedPlayer requested) {
		this.requestSender = requestSender;
		this.requestedUser = requested;
	}
	
	public ExtendedPlayer getRequestSender()	{	return requestSender;	}
	public ExtendedPlayer getRequestedUser()	{	return requestedUser;	}
}
