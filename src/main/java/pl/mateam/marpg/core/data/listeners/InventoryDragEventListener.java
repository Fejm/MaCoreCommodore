package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryDragEvent;


public class InventoryDragEventListener implements Listener {	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(InventoryDragEvent e) {	//e.getInventory() seems to be bugged - it can not detect whether items were dragged on bottom or top inventory (always points to top)
		e.setCancelled(true);
	}
}
