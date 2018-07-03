package pl.mateam.marpg.core.data.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerSwapHandItemsEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.modules.sub.server.UsersManager;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;

public class PlayerSwapHandItemsEventListener implements Listener{	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void shield(PlayerSwapHandItemsEvent event){
		event.setCancelled(true);
		Player player = event.getPlayer();
		UsersManager users = Core.getServer().getUsers();
		ExtendedPlayer exPlayer = users.getExtendedPlayerObject(player);
		if(exPlayer != null){
			if(!exPlayer.isInBattleMode())
				exPlayer.switchToBattleMode();
			return;
		}
	}
}
