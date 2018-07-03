package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.users.PlayerBasedUser;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;

public class PlayerItemHeldEventListener implements Listener{	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerItemHeldEvent event){
		PlayerBasedUser pbu = Core.getServer().getUsers().getPlayerBasedUser(event.getPlayer());
		if(pbu == null)
			return;
		if(event.getNewSlot() == 8) {
			ItemStack item = event.getPlayer().getInventory().getItem(8);
			if(Core.getServer().getItems().getCommodoreLayer(item) == null)
				event.setCancelled(true);
			return;
		}
		if(pbu instanceof ExtendedPlayerImplementation) {
			if(!((ExtendedPlayerImplementation) pbu).isInBattleMode() || event.getNewSlot() > 5)
				return;
			int todo;	//Skill casting
		}
	}
}
