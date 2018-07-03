package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;

public class PlayerDeathEventListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(PlayerDeathEvent event){
		AnyUser user = Core.getServer().getUsers().getGenericObject(event.getEntity());
		if(user instanceof ExtendedPlayerImplementation)
			((ExtendedPlayerImplementation) user).handle(event);
		event.setDeathMessage(null);
	}
}
