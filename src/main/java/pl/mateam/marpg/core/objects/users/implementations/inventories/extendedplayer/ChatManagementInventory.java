package pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.enums.chat.ChatState;
import pl.mateam.marpg.api.regular.enums.items.CommonInterfaceElement;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;

public class ChatManagementInventory extends CommodoreInventory {
	private InventoryView view;
	
	public ChatManagementInventory(ExtendedPlayerImplementation object) {
		super(object);
	}

	@Override public void open() {
		Inventory inv = Bukkit.createInventory(null, 18, ChatColor.DARK_GRAY.toString() + "Zarządzanie czatem");
		this.view = object.getBukkitPlayer().openInventory(inv);

		refreshChatState(ChatChanel.NORMAL);
		refreshChatState(ChatChanel.TRADE);
		refreshChatState(ChatChanel.MODERATOR);
	}

	@Override public void handleClicking(InventoryClickEvent event) {
		if(event.getClickedInventory() != view.getTopInventory())
			return;
		event.setCancelled(true);
		
		int czatPrywatny;

		int slot = event.getSlot();
		boolean rightclick = event.isRightClick();
		ExtendedPlayerImplementation extPlayer = (ExtendedPlayerImplementation) object;
		if(rightclick) {
			if(slot < 3) {
				switch(extPlayer.primaryInfo.chatStates[ChatChanel.NORMAL.ordinal()]) {
					case TYPING:			extPlayer.setChatState(ChatChanel.NORMAL, ChatState.ACTIVE);			break;
					case ACTIVE:			extPlayer.setChatState(ChatChanel.NORMAL, ChatState.MUTED);				break;
					case BLOCKED_ACTIVE:	extPlayer.setChatState(ChatChanel.NORMAL, ChatState.BLOCKED_MUTED);		break;
					default:	return;
				}
			}
			else if(slot < 6) {
				switch(extPlayer.primaryInfo.chatStates[ChatChanel.TRADE.ordinal()]) {
					case TYPING:			extPlayer.setChatState(ChatChanel.TRADE, ChatState.ACTIVE);				break;
					case ACTIVE:			extPlayer.setChatState(ChatChanel.TRADE, ChatState.MUTED);				break;
					case BLOCKED_ACTIVE:	extPlayer.setChatState(ChatChanel.TRADE, ChatState.BLOCKED_MUTED);		break;
					default:	return;
				}
			}
			else if(slot < 9) {
				switch(extPlayer.primaryInfo.chatStates[ChatChanel.MODERATOR.ordinal()]) {
					case TYPING:			extPlayer.setChatState(ChatChanel.MODERATOR, ChatState.ACTIVE);			break;
					case ACTIVE:			CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.error"); 	return;
					default:	return;
				}
			} else return;
			CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.discard");
		} else {
			if(slot < 3) {
				switch(extPlayer.primaryInfo.chatStates[ChatChanel.NORMAL.ordinal()]) {
					case ACTIVE:			extPlayer.setChatState(ChatChanel.NORMAL, ChatState.TYPING);			break;
					case MUTED:				extPlayer.setChatState(ChatChanel.NORMAL, ChatState.ACTIVE);			break;
					case BLOCKED_MUTED:		extPlayer.setChatState(ChatChanel.NORMAL, ChatState.BLOCKED_ACTIVE);	break;
					case BLOCKED_ACTIVE:	int todo;	break;
					default:	return;
				}
			}
			else if(slot < 6) {
				switch(extPlayer.primaryInfo.chatStates[ChatChanel.TRADE.ordinal()]) {
					case ACTIVE:			extPlayer.setChatState(ChatChanel.TRADE, ChatState.TYPING);				break;
					case MUTED:				extPlayer.setChatState(ChatChanel.TRADE, ChatState.ACTIVE);				break;
					case BLOCKED_MUTED:		extPlayer.setChatState(ChatChanel.TRADE, ChatState.BLOCKED_ACTIVE);		break;
					case BLOCKED_ACTIVE:	int todo;	break;
					default:	return;
				}
			}
			else if(slot < 9) {
				switch(extPlayer.primaryInfo.chatStates[ChatChanel.MODERATOR.ordinal()]) {
					case ACTIVE:			extPlayer.setChatState(ChatChanel.MODERATOR, ChatState.TYPING);			break;
					default:	return;
				}
			} else return;
			CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.casual");
		}
	}

	public void refreshChatState(ChatChanel chanel) {
		ExtendedPlayerImplementation extPlayer = (ExtendedPlayerImplementation) object;
		int correspondingSlot = 0;
		CommonInterfaceElement element = null;

		switch(chanel) {
			case NORMAL:
				switch(extPlayer.primaryInfo.chatStates[ChatChanel.NORMAL.ordinal()]) {
					case TYPING:			element = CommonInterfaceElement.CHAT_MAIN_TYPING;			break;
					case ACTIVE:			element = CommonInterfaceElement.CHAT_MAIN_ACTIVE;			break;
					case MUTED:				element = CommonInterfaceElement.CHAT_MAIN_MUTED;			break;
					case BLOCKED_ACTIVE:	element = CommonInterfaceElement.CHAT_MAIN_ACTIVE_BANNED;	break;
					case BLOCKED_MUTED:		element = CommonInterfaceElement.CHAT_MAIN_MUTED_BANNED;	break;
					default: 				break;
				}
				correspondingSlot = 0;
				break;
			case TRADE:
				switch(extPlayer.primaryInfo.chatStates[ChatChanel.TRADE.ordinal()]) {
					case TYPING:			element = CommonInterfaceElement.CHAT_TRADE_TYPING;			break;
					case ACTIVE:			element = CommonInterfaceElement.CHAT_TRADE_ACTIVE;			break;
					case MUTED:				element = CommonInterfaceElement.CHAT_TRADE_MUTED;			break;
					case BLOCKED_ACTIVE:	element = CommonInterfaceElement.CHAT_TRADE_ACTIVE_BANNED;	break;
					case BLOCKED_MUTED:		element = CommonInterfaceElement.CHAT_TRADE_MUTED_BANNED;	break;
					default: 				break;
				}
				correspondingSlot = 3;
				break;
			case MODERATOR:
				switch(extPlayer.primaryInfo.chatStates[ChatChanel.MODERATOR.ordinal()]) {
					case TYPING:			element = CommonInterfaceElement.CHAT_MODERATORS_TYPING;	break;
					case ACTIVE:			element = CommonInterfaceElement.CHAT_MODERATORS_ACTIVE;	break;
					case NOT_VISIBLE:		element = null;
					default:				break;
				}
				correspondingSlot = 6;
				break;
			case PRIVATE:
				int todo;
				break;
			default:	return;
		}
		
		if(element != null) {
			ItemStack interfaceElement = Core.getServer().getItems().getInterfaceElement(element);
			view.setItem(correspondingSlot, interfaceElement);
			ItemStack invisible = Core.getServer().getItems().getInvisibleInterfaceElement(interfaceElement);
			view.setItem(correspondingSlot + 1, invisible);
			view.setItem(correspondingSlot + 2, invisible);
		} else {
			view.setItem(correspondingSlot, null);
			view.setItem(correspondingSlot + 1, null);
			view.setItem(correspondingSlot + 2, null);
		}
	}
	
	@Override public void close(Player whoClosed) {}
}
