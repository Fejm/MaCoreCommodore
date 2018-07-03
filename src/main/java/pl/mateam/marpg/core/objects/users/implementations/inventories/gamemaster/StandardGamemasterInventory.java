package pl.mateam.marpg.core.objects.users.implementations.inventories.gamemaster;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;

public class StandardGamemasterInventory extends CommodoreInventory {
	public StandardGamemasterInventory(AnyUser object)	{	super(object);	}

	@Override public void handleClicking(InventoryClickEvent event) {}
	@Override public void open() {}
	@Override public void close(Player whoClosed) {}
}
