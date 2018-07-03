package pl.mateam.marpg.core.data.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;
import pl.mateam.marpg.core.objects.users.AnyUserImplementation;

public class InventoryCloseEventListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(InventoryCloseEvent event) {
		Player player = (Player) event.getPlayer();
		AnyUser user = Core.getServer().getUsers().getGenericObject(player);
		if(user == null)
			return;
		((AnyUserImplementation) user).closeInventory((Player) event.getPlayer());
	}
}
