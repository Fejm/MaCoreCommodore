package pl.mateam.marpg.core.objects.users.implementations.inventories.lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;

public class LobbyInventory extends CommodoreInventory {
	public LobbyInventory(AnyUser object)	{	super(object);	}

	@Override public void handleClicking(InventoryClickEvent event) 	{	event.setCancelled(true);	}
	@Override public void open() {}
	@Override public void close(Player whoClosed) {}
}
