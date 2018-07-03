package pl.mateam.marpg.core.data.listeners;

import java.util.HashSet;
import java.util.Set;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;
import pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer.InteractionWithAnotherPlayerInventory;

public class PlayerInteractEntityEventListener implements Listener {
	private static int bugWorkaround;
	private static Set<Player> bugPreventingSet = new HashSet<>();		//Normally packets are sent twice
	
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(PlayerInteractEntityEvent event) {
		if(!(event.getRightClicked() instanceof Player) || bugPreventingSet.add(event.getPlayer()))
			return;
		
		bugPreventingSet.remove(event.getPlayer());
		
		
		Player clicked = (Player) event.getRightClicked();
		if(clicked.hasMetadata("NPC"))
			return;


		Player clicker = event.getPlayer();
		if(clicker.isSneaking()) {
			ExtendedPlayer extClicked = Core.getServer().getUsers().getExtendedPlayerObject(clicked);
			if(extClicked == null) 	return;
			ExtendedPlayer extClicker = Core.getServer().getUsers().getExtendedPlayerObject(event.getPlayer());
			if(extClicker == null) 	return;
			
			extClicker.openInventoryIfPossible(new InteractionWithAnotherPlayerInventory((ExtendedPlayerImplementation) extClicker, (ExtendedPlayerImplementation) extClicked));
		}
	}
}
