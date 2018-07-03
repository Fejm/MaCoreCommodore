package pl.mateam.marpg.core.data.listeners;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.inventory.ItemStack;

public class PrepareItemCraftEventListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PrepareItemCraftEvent event) {
		event.getInventory().setResult(new ItemStack(Material.AIR));
	}
}
