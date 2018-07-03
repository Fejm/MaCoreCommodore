package pl.mateam.marpg.api.regular.events;

import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.regular.classes.CommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;

public class GamemasterVisibilityChangeCommodoreEvent extends CommodoreEvent {
	@Override public HandlerList getHandlers() {	return handlers;	}
	public static HandlerList getHandlerList() {	return handlers;	}
	private static final HandlerList handlers = new HandlerList();
	
	private final Gamemaster gamemaster;
	private final boolean silently;
	public GamemasterVisibilityChangeCommodoreEvent(Gamemaster gamemaster, boolean silently) {
		this.gamemaster = gamemaster;
		this.silently = silently;
	}
	
	public Gamemaster getGamemaster()	{	return gamemaster;	}
	public boolean silently()			{	return silently;	}
}
