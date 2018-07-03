package pl.mateam.marpg.core.data.listeners;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.modules.sub.server.UsersManager;

public class PlayerCommandPreProcessEventListener implements Listener{	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(PlayerCommandPreprocessEvent event){
		//TODO: Add detection of clickable-message commands
		//TODO: Add support for moderator commands
		UsersManager manager = Core.getServer().getUsers();
		if(manager.getGamemasterObject(event.getPlayer()) == null){
			if(!(manager.getPlayerBasedUser(event.getPlayer()).isModerator())){
				event.setCancelled(true);
				Bukkit.getPluginManager().callEvent(new AsyncPlayerChatEvent(false, event.getPlayer(), event.getMessage(), null));
			}
		}
	}
}
