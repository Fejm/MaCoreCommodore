package pl.mateam.marpg.api.regular.events;

import org.bukkit.event.HandlerList;

import pl.mateam.marpg.api.regular.classes.CommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;

public class PlayerPvPFinishedCommodoreEvent extends CommodoreEvent {
	@Override public HandlerList getHandlers() {	return handlers;	}
	public static HandlerList getHandlerList() {	return handlers;	}
	private static final HandlerList handlers = new HandlerList();
	
	private final ExtendedPlayer winner;
	private final ExtendedPlayer loser;
	private final boolean hasFled;
	public PlayerPvPFinishedCommodoreEvent(ExtendedPlayer winner, ExtendedPlayer loser, boolean hasFled) {
		this.winner = winner;
		this.loser = loser;
		this.hasFled = hasFled;
	}
	
	public ExtendedPlayer getWinner()	{	return winner;	}
	public ExtendedPlayer getLoser()	{	return loser;	}
	public boolean hasFled()			{	return hasFled;	}
}
