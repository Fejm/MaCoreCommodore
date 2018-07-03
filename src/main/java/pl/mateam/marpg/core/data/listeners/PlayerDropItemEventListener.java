package pl.mateam.marpg.core.data.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;

public class PlayerDropItemEventListener implements Listener {	
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(PlayerDropItemEvent event){
		Player player = event.getPlayer();		
		if(Core.getServer().getUsers().getGamemasterObject(player) != null){
			event.setCancelled(true);
			player.getInventory().setItem(player.getInventory().getHeldItemSlot(), new ItemStack(Material.AIR));
			player.sendMessage(CoreUtils.chat.getCasualAdminMessage("Twój przedmiot został usunięty."));
		}
		else if(player.getInventory().getItem(8) == null) {
			if(Core.getServer().getItems().getCommodoreLayer(event.getItemDrop().getItemStack()) != null) {
				player.sendMessage(CoreUtils.chat.getInfoMessage("Aby uchronić Cię przed przypadkowym wyrzuceniem broni, najpierw musi ona zostać wyjęta ze slotu na broń."));
				CoreUtils.ingame.playSoundPrivate(player, true, "other.prevent_throw");
			}
			event.setCancelled(true);
		}
	}
}
