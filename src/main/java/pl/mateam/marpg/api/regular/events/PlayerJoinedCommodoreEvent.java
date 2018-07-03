package pl.mateam.marpg.api.regular.events;

import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.regular.classes.CommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.PlayerInLobby;

public class PlayerJoinedCommodoreEvent extends CommodoreEvent {
	@Override public HandlerList getHandlers() {	return handlers;	}
	public static HandlerList getHandlerList() {	return handlers;	}
	private static final HandlerList handlers = new HandlerList();
	
	private final PlayerInLobby user;
	public PlayerJoinedCommodoreEvent(PlayerInLobby user) {
		this.user = user;
	}
	
	public PlayerInLobby getUser()	{	return user;	}
}
