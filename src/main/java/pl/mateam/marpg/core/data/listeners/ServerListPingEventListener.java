package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

import pl.mateam.marpg.api.Core;

public class ServerListPingEventListener implements Listener {	
	/*
	 * Motd should be able to be changed manually for EventPriority.HIGHEST.
	 * Otherwise, if motd changes concerns every player, then the motd should be changed using Core.getServer().setMotd() method.
	 */
	@EventHandler(priority = EventPriority.HIGH)
	public void on(ServerListPingEvent event){
		event.setMotd(Core.getServer().getMotd().getMotd());
	}
}
