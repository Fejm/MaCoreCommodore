package pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.classes.observers.ExtendedPlayerActionLocalObserver;
import pl.mateam.marpg.api.regular.classes.observers.ExtendedPlayerActionObserver;
import pl.mateam.marpg.api.regular.enums.items.CommonInterfaceElement;
import pl.mateam.marpg.api.regular.events.PlayerInventoryChangeCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPFinishedCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPRequestExpiredCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPRequestSendCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerTradeRequestExpiredCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerTradeRequestSendCommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;
import pl.mateam.marpg.core.objects.users.AnyUserImplementation;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;

public class InteractionWithAnotherPlayerInventory extends CommodoreInventory {
	private final ExtendedPlayerImplementation observedPlayer;
	private InventoryView view;

	public InteractionWithAnotherPlayerInventory(ExtendedPlayerImplementation object, ExtendedPlayerImplementation observedPlayer) {
		super(object);
		this.observedPlayer = observedPlayer;
	}

	@Override public void open() {
		Inventory inventory = Bukkit.createInventory(null, InventoryType.HOPPER, ChatColor.DARK_GRAY.toString() + "Interakcja z "
				+ observedPlayer.getRank().getColoredNickname(observedPlayer.getBukkitPlayer().getName()));
		this.view = object.getBukkitPlayer().openInventory(inventory);
		updatePvPButton(0);
		updateTradeButton();
		observedPlayer.addObserver((ExtendedPlayerImplementation) object, ObserverInventoryActionObserver.class);
		((ExtendedPlayerImplementation) object).addLocalObserver(PlayerPvPRequestSendCommodoreEvent.class, ObserverInventoryActionLocalObserver.class);
		((ExtendedPlayerImplementation) object).addLocalObserver(PlayerPvPRequestExpiredCommodoreEvent.class, ObserverInventoryActionLocalObserver.class);
		((ExtendedPlayerImplementation) object).addLocalObserver(PlayerTradeRequestSendCommodoreEvent.class, ObserverInventoryActionLocalObserver.class);
		((ExtendedPlayerImplementation) object).addLocalObserver(PlayerTradeRequestExpiredCommodoreEvent.class, ObserverInventoryActionLocalObserver.class);
	}
	
	private LatestPvPButtonState latestPvPState = null;
	private LatestTradeButtonState latestTradeState = null;
	public void updatePvPButton(int levelBonus) {
		ExtendedPlayerImplementation extPlayer = (ExtendedPlayerImplementation) object;
		LatestPvPButtonState latestPvPState;
		if(object.getBukkitPlayer().getLevel() + levelBonus >= 20)
			if(observedPlayer.getBukkitPlayer().getLevel() >= 20)
				if(extPlayer.getPvPOpponent() != observedPlayer)
					if(!observedPlayer.hasProposedPvP(extPlayer.getBukkitPlayer().getName()))
						if(extPlayer.hasProposedPvP(observedPlayer.getBukkitPlayer().getName()))
							if(observedPlayer.canInterract(extPlayer.getBukkitPlayer().getName()))
								latestPvPState = LatestPvPButtonState.REQUESTED_SECOND;
							else latestPvPState = LatestPvPButtonState.BUSY_SECOND;
						else latestPvPState = LatestPvPButtonState.DEFAULT;
					else latestPvPState = LatestPvPButtonState.REQUESTED_OWNER;
				else latestPvPState = LatestPvPButtonState.ALREADY_IN;
			else latestPvPState = LatestPvPButtonState.NO_LEVEL_SECOND;
		else latestPvPState = LatestPvPButtonState.NO_LEVEL_OWNER;
		
		if(latestPvPState == this.latestPvPState)
			return;
		this.latestPvPState = latestPvPState;
		CommonInterfaceElement element = null;
		switch(latestPvPState) {
			case NO_LEVEL_OWNER:	element = CommonInterfaceElement.PROPOSE_PVP_SENDER_NO_LEVEL;	break;
			case NO_LEVEL_SECOND:	element = CommonInterfaceElement.PROPOSE_PVP_RECEIVER_NO_LEVEL;	break;
			case ALREADY_IN:		element = CommonInterfaceElement.PROPOSE_PVP_ALREADY_IN;		break;
			case DEFAULT:			element = CommonInterfaceElement.PROPOSE_PVP_SEND;				break;
			case REQUESTED_OWNER:	element = CommonInterfaceElement.PROPOSE_PVP_SENT;				break;
			case REQUESTED_SECOND:	element = CommonInterfaceElement.PROPOSE_PVP_RECEIVED;			break;
			case BUSY_SECOND:		element = CommonInterfaceElement.PROPOSE_PVP_UNABLE;			break;
		}
		
		view.setItem(0, Core.getServer().getItems().getInterfaceElement(element));
	}
	
	public void updateTradeButton() {
		ExtendedPlayerImplementation extPlayer = (ExtendedPlayerImplementation) object;
		LatestTradeButtonState latestTradeState;
		if(!observedPlayer.hasProposedTrade(extPlayer.getBukkitPlayer().getName()))
			if(extPlayer.hasProposedTrade(observedPlayer.getBukkitPlayer().getName()))
				if(observedPlayer.canInterract(extPlayer.getBukkitPlayer().getName()))
					latestTradeState = LatestTradeButtonState.REQUESTED_SECOND;
				else latestTradeState = LatestTradeButtonState.BUSY_SECOND;
			else latestTradeState = LatestTradeButtonState.DEFAULT;
		else latestTradeState = LatestTradeButtonState.REQUESTED_OWNER;
		
		if(latestTradeState == this.latestTradeState)
			return;
		this.latestTradeState = latestTradeState;
		CommonInterfaceElement element = null;
		switch(latestTradeState) {
			case DEFAULT:			element = CommonInterfaceElement.PROPOSE_TRADE_SEND;			break;
			case REQUESTED_OWNER:	element = CommonInterfaceElement.PROPOSE_TRADE_SENT;			break;
			case REQUESTED_SECOND:	element = CommonInterfaceElement.PROPOSE_TRADE_RECEIVED;		break;
			case BUSY_SECOND:		element = CommonInterfaceElement.PROPOSE_TRADE_UNABLE;			break;
		}
		
		view.setItem(1, Core.getServer().getItems().getInterfaceElement(element));
	}
	
	public ExtendedPlayerImplementation getObservedPlayer()		{	return observedPlayer;	}

	@Override public void handleClicking(InventoryClickEvent event) {
		if(event.getClickedInventory() != view.getTopInventory())
			return;
		event.setCancelled(true);
		
		switch(event.getSlot()){
			case 0:
				switch(latestPvPState) {
					case NO_LEVEL_OWNER:	CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.error");	return;
					case NO_LEVEL_SECOND:	CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.error");	return;
					case ALREADY_IN:		CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.error");	return;
					case DEFAULT:
						observedPlayer.proposePvP((ExtendedPlayerImplementation) object);
						CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.casual");						return;
					case REQUESTED_OWNER:	return;
					case REQUESTED_SECOND:
						observedPlayer.proposePvP((ExtendedPlayerImplementation) object);
						CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.accept");						return;
					case BUSY_SECOND:		CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.error");	return;			
				}
			case 1:
				switch(latestTradeState) {
					case DEFAULT:
						observedPlayer.proposeTrade((ExtendedPlayerImplementation) object);
						CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.casual");						return;
					case REQUESTED_OWNER:	return;
					case REQUESTED_SECOND:
						observedPlayer.proposeTrade((ExtendedPlayerImplementation) object);
						CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.accept");						return;
					case BUSY_SECOND:		CoreUtils.ingame.playSoundPrivate(object.getBukkitPlayer(), false, "click.error");	return;			
				}
		}
	}
 
	@Override public void close(Player whoClosed) {
		observedPlayer.removeObserver((ExtendedPlayerImplementation) object, ObserverInventoryActionObserver.class);
		((ExtendedPlayerImplementation) object).removeLocalObserver(PlayerPvPRequestSendCommodoreEvent.class, ObserverInventoryActionLocalObserver.class);
		((ExtendedPlayerImplementation) object).removeLocalObserver(PlayerPvPRequestExpiredCommodoreEvent.class, ObserverInventoryActionLocalObserver.class);
		((ExtendedPlayerImplementation) object).removeLocalObserver(PlayerTradeRequestSendCommodoreEvent.class, ObserverInventoryActionLocalObserver.class);
		((ExtendedPlayerImplementation) object).removeLocalObserver(PlayerTradeRequestExpiredCommodoreEvent.class, ObserverInventoryActionLocalObserver.class);
	}
	
	public void handlePlayerLevelChangeEvent(PlayerLevelChangeEvent event) 	{	updatePvPButton(event.getNewLevel() - object.getBukkitPlayer().getLevel());	}
	public void handlePlayerLevelChangeEvent() 								{	updatePvPButton(0);								}
	public void handle(PlayerPvPRequestExpiredCommodoreEvent event) 		{	updatePvPButton(0);								}
	public void handle(PlayerPvPRequestSendCommodoreEvent event) 			{	updatePvPButton(0); 							}
	public void handle(PlayerTradeRequestExpiredCommodoreEvent event) 		{	updateTradeButton();							}
	public void handle(PlayerTradeRequestSendCommodoreEvent event) 			{	updateTradeButton(); 							}
	public void handle(PlayerInventoryChangeCommodoreEvent event)			{	updatePvPButton(0);		updateTradeButton(); 	}
	public void handle(PlayerPvPFinishedCommodoreEvent event)				{	updatePvPButton(0); 							}

	private enum LatestPvPButtonState {
		NO_LEVEL_OWNER, NO_LEVEL_SECOND, ALREADY_IN, DEFAULT, REQUESTED_OWNER, REQUESTED_SECOND, BUSY_SECOND;
	}
	
	private enum LatestTradeButtonState {
		DEFAULT, REQUESTED_OWNER, REQUESTED_SECOND, BUSY_SECOND;
	}
	
	public static class ObserverInventoryActionObserver extends ExtendedPlayerActionObserver {
		@Override public void handle(PlayerLevelChangeEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {
			((InteractionWithAnotherPlayerInventory) ((ExtendedPlayerImplementation) observer).getOpenInventory()).handlePlayerLevelChangeEvent();
		}
		
		@Override public void handle(PlayerPvPRequestExpiredCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer){
			((InteractionWithAnotherPlayerInventory) ((ExtendedPlayerImplementation) observer).getOpenInventory()).handle(event);
		}
		
		@Override public void handle(PlayerPvPRequestSendCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer){
			((InteractionWithAnotherPlayerInventory) ((ExtendedPlayerImplementation) observer).getOpenInventory()).handle(event);
			if(event.wasJustAccepted()) {
				event.getRequestSender().closeInventory();
				event.getRequestedUser().closeInventory();
			}
		}
		
		@Override public void handle(PlayerTradeRequestExpiredCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer){
			((InteractionWithAnotherPlayerInventory) ((ExtendedPlayerImplementation) observer).getOpenInventory()).handle(event);
		}
		
		@Override public void handle(PlayerTradeRequestSendCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer){
			((InteractionWithAnotherPlayerInventory) ((ExtendedPlayerImplementation) observer).getOpenInventory()).handle(event);
		}
		
		@Override public void handle(PlayerInventoryChangeCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer){
			((InteractionWithAnotherPlayerInventory) ((ExtendedPlayerImplementation) observer).getOpenInventory()).handle(event);
		}
		
		@Override public void handle(PlayerQuitEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {
			((ExtendedPlayerImplementation) observer).closeInventory();
		};
		
		@Override public void handle(PlayerPvPFinishedCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {
			((InteractionWithAnotherPlayerInventory) ((ExtendedPlayerImplementation) observer).getOpenInventory()).handle(event);
		}
	}
	
	public static class ObserverInventoryActionLocalObserver extends ExtendedPlayerActionLocalObserver {
		@Override public void handle(PlayerPvPRequestExpiredCommodoreEvent event) {
			((InteractionWithAnotherPlayerInventory) ((AnyUserImplementation) event.getRequestSender()).getOpenInventory()).handle(event);
		}

		@Override public void handle(PlayerPvPRequestSendCommodoreEvent event) {
			((InteractionWithAnotherPlayerInventory) ((AnyUserImplementation) event.getRequestSender()).getOpenInventory()).handle(event);
		}
		
		@Override public void handle(PlayerTradeRequestExpiredCommodoreEvent event) {
			((InteractionWithAnotherPlayerInventory) ((AnyUserImplementation) event.getRequestSender()).getOpenInventory()).handle(event);
		}

		@Override public void handle(PlayerTradeRequestSendCommodoreEvent event) {
			((InteractionWithAnotherPlayerInventory) ((AnyUserImplementation) event.getRequestSender()).getOpenInventory()).handle(event);
		}
	}
}
