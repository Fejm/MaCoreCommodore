package pl.mateam.marpg.api.regular.events;

import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.regular.classes.CommodoreEvent;

public class PlayerBannedCommodoreEvent extends CommodoreEvent {
	@Override public HandlerList getHandlers() {	return handlers;	}
	public static HandlerList getHandlerList() {	return handlers;	}
	private static final HandlerList handlers = new HandlerList();

	private final String playerName;
	private final boolean wasOnline;
	public PlayerBannedCommodoreEvent(String playerName, boolean wasOnline) {
		this.playerName = playerName;
		this.wasOnline = wasOnline;
	}
	
	public String getPlayerName()		{	return playerName;	}
	public boolean wasPlayerOnline()	{	return wasOnline;	}
}
