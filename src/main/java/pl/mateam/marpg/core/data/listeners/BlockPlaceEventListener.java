package pl.mateam.marpg.core.data.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;

public class BlockPlaceEventListener implements Listener {	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockPlaceEvent event){
		Gamemaster gamemaster = Core.getServer().getUsers().getGamemasterObject(event.getPlayer());
		if(gamemaster != null){
			if(event.getPlayer().getGameMode() == GameMode.CREATIVE) {
				int ogarnacKomende, niewidzialnosc;
				if(!gamemaster.hasInteractionEnabled())
					event.getPlayer().sendMessage(CoreUtils.chat.getCasualAdminMessage("Interakcja z blokami jest wyłączona. TODO: Ogarnąć komendę."));
				else return;
			}
		}
		event.setCancelled(true);
	}
}
