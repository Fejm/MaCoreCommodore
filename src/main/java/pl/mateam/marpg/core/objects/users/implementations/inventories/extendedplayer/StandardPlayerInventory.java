package pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;

import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;

public class StandardPlayerInventory extends CommodoreInventory {
	public StandardPlayerInventory(ExtendedPlayerImplementation object) 	{	super(object);	}

	@Override public void handleClicking(InventoryClickEvent event) {
		Inventory inv = event.getClickedInventory();
		if(inv != null && inv.getType() == InventoryType.CRAFTING) {
			event.setCancelled(true);
			if(event.getSlot() == 0)
				object.forceOpenInventory(new ChatManagementInventory((ExtendedPlayerImplementation) object));
		}
	}

	@Override public void open() {}
	@Override public void close(Player whoClosed) {}
}
