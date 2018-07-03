package pl.mateam.marpg.api.regular.events;

import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.regular.classes.CommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;

public class PlayerInventoryChangeCommodoreEvent extends CommodoreEvent {
	@Override public HandlerList getHandlers() {	return handlers;	}
	public static HandlerList getHandlerList() {	return handlers;	}
	private static final HandlerList handlers = new HandlerList();
	
	private final AnyUser user;
	public PlayerInventoryChangeCommodoreEvent(AnyUser user) {
		this.user = user;
	}
	
	public AnyUser getUser()	{	return user;	}
}
