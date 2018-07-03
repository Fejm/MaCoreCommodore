package pl.mateam.marpg.core.data.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLevelChangeEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;

public class PlayerLevelChangeEventListener implements Listener {	
	@EventHandler(priority = EventPriority.HIGH)
	public void onLevelChange(PlayerLevelChangeEvent event){
		Player player = event.getPlayer();
		ExtendedPlayer e = Core.getServer().getUsers().getExtendedPlayerObject(player);
		if(!(e instanceof ExtendedPlayerImplementation))
			return;
		ExtendedPlayerImplementation exPlayer = (ExtendedPlayerImplementation) e;
		exPlayer.handle(event);
	}
}
