package pl.mateam.marpg.core.data.listeners;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.modules.sub.server.ItemsManager;
import pl.mateam.marpg.api.regular.objects.items.CommodoreItem;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo.ItemSlotType;
import pl.mateam.marpg.core.objects.items.CommodoreSpecialItemImplementation;
import pl.mateam.marpg.core.objects.users.AnyUserImplementation;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;

public class InventoryClickEventListener implements Listener {
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(InventoryClickEvent event) {
		Player player = (Player) event.getWhoClicked();
		AnyUser playerObject = Core.getServer().getUsers().getGenericObject(player);
		ItemsManager manager = Core.getServer().getItems();
		if(playerObject == null)
			return;
		
		Inventory clickedInv = event.getClickedInventory();
		InventoryType type = clickedInv == null? null : clickedInv.getType();
		if(type == InventoryType.PLAYER) {
			if(playerObject instanceof ExtendedPlayerImplementation) {
				event.setCancelled(true);
				ExtendedPlayerImplementation extPlayer = (ExtendedPlayerImplementation) playerObject;
				
				CommodoreItem newItemLayer, clickedItemLayer = manager.getCommodoreLayer(event.getCurrentItem());
				
				ItemSlotType clickedSlotType = ItemSlotType.getUsingNumber(event.getSlot());
				if(event.isShiftClick()) {
					if(clickedSlotType == null) {
						if(customHandledShiftclicks(event, playerObject))
							return;
						if(clickedItemLayer instanceof CommodoreSpecialItemImplementation) {
							newItemLayer = clickedItemLayer;
							ItemSlotType correspondingSlotType = ((CommodoreSpecialItemImplementation) clickedItemLayer).getValidSlotType();
							for(int i : correspondingSlotType.getAllowedSlots())
								if(manager.getCommodoreLayer(clickedInv.getItem(i)) == null)
									if(handleEquipping(extPlayer, correspondingSlotType, (CommodoreSpecialItemImplementation) newItemLayer)) {
										ItemStack item = event.getCurrentItem();
										event.setCurrentItem(null);
										extPlayer.recalculateStats(null, newItemLayer);
										extPlayer.getBukkitPlayer().getInventory().setItem(i, item);
										return;
									}
						}

						performDefaultShiftclickAction(event);
					} else {
						if(clickedItemLayer != null)
							if(performDefaultShiftclickAction(event)) {
								if(clickedSlotType == ItemSlotType.HANDWEAR && player.getInventory().getHeldItemSlot() == 8)
									player.getInventory().setHeldItemSlot(7);
								event.setCurrentItem(clickedSlotType.getEmptySlotElement());
								extPlayer.recalculateStats(clickedItemLayer, null);
							}
					}
					return;
				} else {
					if(clickedSlotType == null) {
						event.setCancelled(false);
						((AnyUserImplementation) playerObject).handleInventoryClicking(event);
						return;
					}
					else {
						ItemStack cursor = event.getCursor();
						newItemLayer = manager.getCommodoreLayer(cursor);
						
						if(newItemLayer instanceof CommodoreSpecialItemImplementation) {
							if(((CommodoreSpecialItemImplementation) newItemLayer).getValidSlotType() != clickedSlotType)
								return;
							if(clickedItemLayer == null) {
								if(handleEquipping(extPlayer, clickedSlotType, (CommodoreSpecialItemImplementation) newItemLayer)) {
									event.getView().setCursor(null);
									event.setCurrentItem(cursor);
									extPlayer.recalculateStats(null, newItemLayer);
								}
							} else {
								if(handleEquipping(extPlayer, clickedSlotType, (CommodoreSpecialItemImplementation) newItemLayer)) {
									ItemStack item = event.getCurrentItem();
									event.setCurrentItem(cursor);
									extPlayer.recalculateStats(clickedItemLayer, newItemLayer);
									event.getView().setCursor(item);
								}
							}
							return;
						} else {
							if(cursor.getType() == Material.AIR && clickedItemLayer != null) {
								ItemStack item = event.getCurrentItem();
								if(clickedSlotType == ItemSlotType.HANDWEAR && player.getInventory().getHeldItemSlot() == 8)
									player.getInventory().setHeldItemSlot(7);
								event.setCurrentItem(clickedSlotType.getEmptySlotElement());
								extPlayer.recalculateStats(clickedItemLayer, null);
								event.getView().setCursor(item);
							}
							return;
						}
					}
				}
			}
		}
		((AnyUserImplementation) playerObject).handleInventoryClicking(event);
	}
	
	private boolean customHandledShiftclicks(InventoryClickEvent event, AnyUser clicker) {
		CommodoreInventory cInv = ((AnyUserImplementation) clicker).getOpenInventory();
		if(cInv.areShiftclicksCustomlyHandled()) {
			cInv.handleClicking(event);
			return true;
		}
		return false;
	}
	
	private boolean performDefaultShiftclickAction(InventoryClickEvent event) {
		Inventory clickedInv = event.getClickedInventory();
		if(clickedInv.firstEmpty() != -1) {
			ItemStack clicked = event.getCurrentItem();
			event.setCurrentItem(null);
			clickedInv.addItem(clicked);
			return true;
		}
		return false;
	}
	
	private boolean handleEquipping(ExtendedPlayerImplementation extPlayer, ItemSlotType correspondingSlotType, CommodoreSpecialItemImplementation newItemLayer) {	//Returns if item got equipped
		if(newItemLayer.getItemLevel() <= extPlayer.getBukkitPlayer().getLevel() && newItemLayer.hasProperClass(extPlayer.getCharacterClass())) {
			if(extPlayer.canChangeItemYet(correspondingSlotType)) {
				extPlayer.notifyThatItemGotEquipped(correspondingSlotType);
				return true;
			} else {
				CoreUtils.ingame.playSoundPrivate(extPlayer.getBukkitPlayer(), true, "inventory.change_item");
				extPlayer.getBukkitPlayer().sendMessage(CoreUtils.chat.getErrorMessage("Poczekaj chwilę, zanim zmienisz przedmiot tego typu ponownie."));
			}
		} else {
			CoreUtils.ingame.playSoundPrivate(extPlayer.getBukkitPlayer(), true, "inventory.cant_use");
			extPlayer.getBukkitPlayer().sendMessage(CoreUtils.chat.getErrorMessage("Nie możesz korzystać z tego przedmiotu."));
		}
		return false;
	}
}
