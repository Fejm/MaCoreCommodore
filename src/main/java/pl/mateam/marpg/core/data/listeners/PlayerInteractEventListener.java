package pl.mateam.marpg.core.data.listeners;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;

public class PlayerInteractEventListener implements Listener {	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerInteractEvent event){
		if (event.getAction() == Action.PHYSICAL) {
			if(event.getClickedBlock().getType() == Material.SOIL)
				event.setCancelled(true);
			return;
		}
		
		if ((event.getAction() == Action.RIGHT_CLICK_BLOCK) || (event.getAction() == Action.RIGHT_CLICK_AIR)) {
			Gamemaster gamemaster = Core.getServer().getUsers().getGamemasterObject(event.getPlayer());
			if(gamemaster != null){
				if(event.getPlayer().getGameMode() == GameMode.CREATIVE) {
					if(gamemaster.hasInteractionEnabled())
						return;
					else
						event.getPlayer().sendMessage(CoreUtils.chat.getCasualAdminMessage("Interakcja z blokami jest wyłączona. TODO: Ogarnąć komendę."));
				}
				event.setCancelled(true);
				return;
			}
			//Prevent from opening containters in lobby
			if(Core.getServer().getUsers().getExtendedPlayerObject(event.getPlayer()) == null)
				event.setCancelled(true);

			Material material = event.getMaterial();
			if(	material == Material.LEATHER_HELMET ||
				material == Material.LEATHER_CHESTPLATE ||
				material == Material.LEATHER_LEGGINGS ||
				material == Material.LEATHER_BOOTS){
				event.setCancelled(true);
			}
		}
	}
}
