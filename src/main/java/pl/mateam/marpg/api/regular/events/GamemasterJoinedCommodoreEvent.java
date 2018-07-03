package pl.mateam.marpg.api.regular.events;

import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.regular.classes.CommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;

public class GamemasterJoinedCommodoreEvent extends CommodoreEvent {
	@Override public HandlerList getHandlers() {	return handlers;	}
	public static HandlerList getHandlerList() {	return handlers;	}
	private static final HandlerList handlers = new HandlerList();
	
	private final Gamemaster gamemaster;
	public GamemasterJoinedCommodoreEvent(Gamemaster gamemaster) {
		this.gamemaster = gamemaster;
	}
	
	public Gamemaster getGamemaster()	{	return gamemaster;	}
}
