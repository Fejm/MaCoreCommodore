package pl.mateam.marpg.core.data.listeners;

import org.bukkit.GameMode;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.enums.items.Tier;
import pl.mateam.marpg.api.regular.modules.sub.server.ItemsManager;
import pl.mateam.marpg.api.regular.objects.items.CommodoreItem;
import pl.mateam.marpg.api.regular.objects.items.special.CommodoreHandwear;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlate;
import pl.mateam.marpg.core.internal.hardcoded.OtherHardcoded;
import pl.mateam.marpg.core.objects.worlds.tectonic.TectonicPlateOresManagerImplementation;

public class BlockBreakEventListener implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
	public void on(BlockBreakEvent event)	{
		Gamemaster gamemaster = Core.getServer().getUsers().getGamemasterObject(event.getPlayer());
		if(gamemaster != null) {
			if(event.getPlayer().getGameMode() == GameMode.CREATIVE) {
				int ogarnacKomende, niewidzialnosc;
				if(!gamemaster.hasInteractionEnabled())
					event.getPlayer().sendMessage(CoreUtils.chat.getCasualAdminMessage("Interakcja z blokami jest wyłączona. TODO: Ogarnąć komendę."));
				else return;
			}
			event.setCancelled(true);
		} else {
			event.setCancelled(true);
			TectonicPlate plate = Core.getServer().getWorlds().getTectonicPlate(event.getBlock().getLocation());
			for(Tier tier : Tier.values()){
				if(tier.getOreMaterial() == event.getBlock().getType() && tier.getOreDurability() == event.getBlock().getData()) {
					((TectonicPlateOresManagerImplementation) plate.getOresManager()).notifyThatOreGotMined(event.getBlock());
					ItemsManager manager = Core.getServer().getItems();
					ItemStack item = event.getPlayer().getInventory().getItemInMainHand();
					CommodoreItem cItem = manager.getCommodoreLayer(item);
					if(cItem instanceof CommodoreHandwear)
						if(Math.random() < ((CommodoreHandwear) cItem).getChanceToMineSuccesfully()) {
							ItemStack drop = manager.getItem(tier.getUpgradingItem()).craftItemStack();
							event.getPlayer().getWorld().dropItemNaturally(event.getBlock().getLocation(), drop);
						}
					event.getBlock().setType(OtherHardcoded.MINED_ORE_MATERIAL);
					return;
				}
			}
		}
	}
}
