package pl.mateam.marpg.core.data.listeners.commodoreevents;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;

import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.events.GamemasterVisibilityChangeCommodoreEvent;
import pl.mateam.marpg.core.objects.effects.GamemasterJoinCommodoreEffect;
import pl.mateam.marpg.core.objects.effects.GamemasterQuitCommodoreEffect;

public class GamemasterVisibilityChangeCommodoreEventListener implements Listener {
	@EventHandler(priority = EventPriority.LOWEST)
	public void on(GamemasterVisibilityChangeCommodoreEvent event) {
		String playername = event.getGamemaster().getBukkitPlayer().getName();
		if(event.getGamemaster().isVisible()) {
			Bukkit.broadcastMessage(CoreUtils.chat.getBroadcastMessageWithHighlight("Mistrz Gry, ", playername, ", dołączył do gry!"));
			new GamemasterJoinCommodoreEffect(event.getGamemaster().getBukkitPlayer().getLocation()).playInternal();
		}
		else new GamemasterQuitCommodoreEffect(event.getGamemaster().getBukkitPlayer().getLocation()).playInternal(playername, () -> 
		Bukkit.broadcastMessage(CoreUtils.chat.getBroadcastMessageWithHighlight("Mistrz Gry, ", playername, ", opuścił grę!")));
	}
}
