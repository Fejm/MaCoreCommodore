package pl.mateam.marpg.core.data.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class EntityDamageEventListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
	public void on(EntityDamageEvent event) {
		EntityDamageEventProcessor processor = new EntityDamageEventProcessor(event);
		if(!(processor.causeIsValid()))		return;
	}
	
	private static class EntityDamageEventProcessor {
		private final EntityDamageEvent event;
		private EntityDamageEventProcessor(EntityDamageEvent event) {
			this.event = event;
		}
		
		private boolean causeIsValid() {
			DamageCause cause = event.getCause();
			if(cause == DamageCause.STARVATION) {
				event.setCancelled(true);
				return false;
			} else return true;
		}
	}
	
	todo;
}
