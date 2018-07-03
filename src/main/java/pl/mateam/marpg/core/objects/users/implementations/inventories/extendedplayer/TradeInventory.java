package pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreSharedInventory;
import pl.mateam.marpg.api.regular.enums.items.CommonInterfaceElement;
import pl.mateam.marpg.api.regular.modules.sub.server.ItemsManager;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.internal.utils.Parsers;
import pl.mateam.marpg.core.objects.users.AnyUserImplementation;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation;

public class TradeInventory extends CommodoreSharedInventory {
	private static final Set<Integer> tradeSlots = new HashSet<>(Arrays.asList(0, 1, 2, 3, 9, 10, 11, 12, 18, 19, 20, 21));
	private static ItemStack[] contents = new ItemStack[54];
	
	@Secret public static void init() {
		ItemsManager items = Core.getServer().getItems();
		ItemStack center = items.getInterfaceElement(CommonInterfaceElement.MENU_DEFAULT_ELEMENT_CENTER);
		ItemStack left = items.getInterfaceElement(CommonInterfaceElement.MENU_DEFAULT_ELEMENT_LEFT);
		ItemStack right = items.getInterfaceElement(CommonInterfaceElement.MENU_DEFAULT_ELEMENT_RIGHT);

		contents[4] = center;
		contents[13] = items.getInterfaceElement(CommonInterfaceElement.MENU_TRADE_ARROWS);
		contents[22] = center;
		
		contents[27] = left;
		for(int i = 28; i<35; i++)
			contents[i] = center;
		contents[35] = right;

		contents[36] = items.getInterfaceElement(CommonInterfaceElement.MENU_TRADE_MONEY_1);
		contents[37] = items.getInterfaceElement(CommonInterfaceElement.MENU_TRADE_MONEY_10);
		contents[38] = items.getInterfaceElement(CommonInterfaceElement.MENU_TRADE_MONEY_100);
		contents[39] = items.getInterfaceElement(CommonInterfaceElement.MENU_TRADE_MONEY_1000);
		contents[40] = center;
		contents[41] = center;
		contents[42] = center;
		contents[43] = items.getInterfaceElement(CommonInterfaceElement.MENU_TRADE_ACCEPT_SINGLE);
		contents[44] = items.getInterfaceElement(CommonInterfaceElement.MENU_TRADE_DISCARD);
		
		contents[45] = left;
		for(int i = 46; i<53; i++)
			contents[i] = center;
		contents[53] = right;
	}
	
	public TradeInventory(ExtendedPlayerImplementation object1, ExtendedPlayerImplementation object2) {
		super(object1, object2);
	}

	private InventoryView[] views = new InventoryView[2];
	private int[] moneyToGet = new int[2];
	private boolean[] acceptationStatus = new boolean[2];

	@Override public void open() {
		Inventory inventory = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY.toString() + "Handel z "
				+ ((ExtendedPlayerImplementation) object2).getRank().getColoredNickname(((ExtendedPlayerImplementation) object2).getBukkitPlayer().getName()));
		inventory.setContents(contents);
		views[0] = object.getBukkitPlayer().openInventory(inventory);
		Inventory inventory2 = Bukkit.createInventory(null, 54, ChatColor.DARK_GRAY.toString() + "Handel z "
				+ ((ExtendedPlayerImplementation) object).getRank().getColoredNickname(((ExtendedPlayerImplementation) object).getBukkitPlayer().getName()));
		inventory2.setContents(contents);
		views[1] = object2.getBukkitPlayer().openInventory(inventory2);
	}

	@Override public void handleClicking(InventoryClickEvent event) {
		int clickedSlot = event.getRawSlot();
		boolean shiftclick = event.isShiftClick();
		boolean rightclick = event.isRightClick();
		InventoryView view = event.getView();
		Player whoClicked = (Player) event.getWhoClicked();
		
		if(event.getClickedInventory() == view.getBottomInventory()) {
			if(!shiftclick || view.getItem(clickedSlot) == null || view.getItem(clickedSlot).getType() == Material.AIR)
				return;
			event.setCancelled(true);
			int firstEmpty = firstEmpty(event.getView().getTopInventory());
			if(firstEmpty == -1)
				return;
			update(firstEmpty, view.getItem(clickedSlot), whoClicked);
			view.setItem(clickedSlot, new ItemStack(Material.AIR));
		} else {
			if(tradeSlots.contains(clickedSlot)) {
				new BukkitRunnable() {
					@Override
					public void run() {
						update(clickedSlot, view.getItem(clickedSlot), whoClicked);
					}
				}.runTask(MaCoreCommodoreEngine.getReference());
			} else {
				event.setCancelled(true);
				if(clickedSlot >= 36 && clickedSlot <= 39)
					moneyChange(clickedSlot, rightclick, whoClicked);
				else {
					if(clickedSlot == 43){
						if(getAcceptationStatus(whoClicked))
							return;
						Player secondPlayer = getSecondUser(whoClicked).getBukkitPlayer();
						setAccepted(whoClicked);
						if(getAcceptationStatus(secondPlayer)){
							setAccepted(whoClicked);
							object.closeInventory();
							CoreUtils.ingame.playSoundPrivate(whoClicked, false, "click.accept");
							CoreUtils.ingame.playSoundPrivate(secondPlayer, true, "trade.success");
							whoClicked.sendMessage(CoreUtils.chat.getCasualMessage("Wymiana zakończona powodzeniem."));
							secondPlayer.getPlayer().sendMessage(CoreUtils.chat.getCasualMessage("Wymiana zakończona powodzeniem."));
						} else {
							view.setItem(43, Core.getServer().getItems().getInterfaceElement(CommonInterfaceElement.MENU_TRADE_ACCEPTED));
							secondPlayer.getOpenInventory().setItem(43, Core.getServer().getItems().getInterfaceElement(CommonInterfaceElement.MENU_TRADE_ACCEPT_BOTH));
							CoreUtils.ingame.playSoundPrivate(whoClicked, false, "click.casual");
							CoreUtils.ingame.playSoundPrivate(secondPlayer, true, "trade.second_accepted");
							return;
						}
					} else if(clickedSlot == 44){
						CoreUtils.ingame.playSoundPrivate(whoClicked, false, "click.decline");
						((AnyUserImplementation) object).closeInventory(object.getBukkitPlayer());
						object.getBukkitPlayer().getOpenInventory().close();
						return;
					}
				}
			}
		}
	}
	
	private int firstEmpty(Inventory inventory){
		for(int i : tradeSlots)
			if(inventory.getItem(i) == null || inventory.getItem(i).getType() == Material.AIR)
				return i;
		return -1;
	}
	private boolean getAcceptationStatus(Player whose) {
		if(whose == object.getBukkitPlayer())	return acceptationStatus[0];
		else									return acceptationStatus[1];
	}
	private void setAccepted(Player who) {
		if(who == object.getBukkitPlayer())		acceptationStatus[0] = true;
		else									acceptationStatus[1] = true;
	}
	private void update(int slot, ItemStack item, Player whoClicked) {
		whoClicked.getOpenInventory().setItem(slot, item);
		getSecondUser(whoClicked).getBukkitPlayer().getOpenInventory().setItem(slot + 5, item);
		resetAcceptationIndicator();
	}
	private int getMoneyToGet(AnyUser whose) {
		if(whose == object)			return moneyToGet[0];
		else						return moneyToGet[1];
	}
	private void resetAcceptationIndicator(){
		if(acceptationStatus[0] || acceptationStatus[1]){
			acceptationStatus[0] = false;
			acceptationStatus[1] = false;
			ItemStack grayAcceptButton = Core.getServer().getItems().getInterfaceElement(CommonInterfaceElement.MENU_TRADE_ACCEPT_SINGLE);
			object.getBukkitPlayer().getOpenInventory().setItem(43, grayAcceptButton);
			object2.getBukkitPlayer().getOpenInventory().setItem(43, grayAcceptButton);
		}
	}
	private void moneyChange(int clickedSlot, boolean rightclick, Player whoClicked) {
		ExtendedPlayerImplementation extObj2 = (ExtendedPlayerImplementation) getSecondUser(whoClicked);
		ExtendedPlayerImplementation extObj1 = (ExtendedPlayerImplementation) getSecondUser(extObj2);
		Player player1 = extObj1.getBukkitPlayer();
		Player player2 = extObj2.getBukkitPlayer();

		int ownedMoney = extObj1.getMoney();
		
		int moneyToGet = getMoneyToGet(extObj1);
		int amount = 0;
		switch(clickedSlot){
			case 36:
				amount = 1;
				break;
			case 37:
				amount = 10;
				break;
			case 38:
				amount = 100;
				break;
			case 39:
				amount = 1000;
				break;
		}
		
		
		if(moneyToGet < 0){
			if(rightclick){
				CoreUtils.ingame.playSoundPrivate(player2, true, "trade.takes_moneyToGet");
				CoreUtils.ingame.playSoundPrivate(player1, false, "click.casual");
				amount = (moneyToGet * (-1)) > amount? amount : moneyToGet * (-1);
				changeBalance(amount);
			} else {
				if(ownedMoney == (moneyToGet * (-1))){
					CoreUtils.ingame.playSoundPrivate(player1, false, "click.error");
					return;
				}
				CoreUtils.ingame.playSoundPrivate(player1, false, "click.twang");
				CoreUtils.ingame.playSoundPrivate(player2, true, "trade.gives_moneyToGet");
				amount = amount > ownedMoney + moneyToGet? ownedMoney + moneyToGet : amount;
				changeBalance(amount * -1);
			}
		} else {
			if(!rightclick){
				if(ownedMoney == 0 && moneyToGet == 0){
					CoreUtils.ingame.playSoundPrivate(player1, false, "click.error");
					return;
				}
				CoreUtils.ingame.playSoundPrivate(player2, true, "trade.gives_moneyToGet");
				CoreUtils.ingame.playSoundPrivate(player1, false, "click.twang");
				amount = amount > ownedMoney + moneyToGet? ownedMoney + moneyToGet : amount;
				changeBalance(amount * -1);
			} else return;
		}

		updateMoneyIndicator(extObj1, extObj2);
		resetAcceptationIndicator();
	}
	private void changeBalance(int amountRelativeToFirstPlayer){
		moneyToGet[0] += amountRelativeToFirstPlayer;
		moneyToGet[1] -= amountRelativeToFirstPlayer;
	}
	private void updateMoneyIndicator(AnyUser extObj1, AnyUser extObj2) {
		InventoryView inv1 = extObj1.getBukkitPlayer().getOpenInventory();
		InventoryView inv2 = extObj2.getBukkitPlayer().getOpenInventory();
		
		int moneyToGet = getMoneyToGet(extObj1);
		
		if(moneyToGet == 0){
			ItemStack domyslnyZapychacz = Core.getServer().getItems().getInterfaceElement(CommonInterfaceElement.MENU_DEFAULT_ELEMENT_CENTER);
			inv1.setItem(41, domyslnyZapychacz);
		} else {
			ItemStack plus = Core.getServer().getItems().getInterfaceElement(CommonInterfaceElement.MENU_TRADE_MONEY_PLUS);
			ItemStack minus = Core.getServer().getItems().getInterfaceElement(CommonInterfaceElement.MENU_TRADE_MONEY_MINUS);
			int moneyToGetAbs = Math.abs(moneyToGet);
			String toSubtract = "§eodliczone §b";
			String toAdd = "§edoliczone §b";
			String zostac = "zostanie";
			
			if(moneyToGetAbs == 1) {
				toSubtract = "§eodliczony §b";
				toAdd = "§edoliczony §b";
			}
			else if((moneyToGetAbs % 10 > 1 && moneyToGetAbs % 10 < 5) && (!(moneyToGetAbs > 4 && moneyToGetAbs <20)))
				zostac = "zostaną";

			String poteflony = Parsers.getProperForm(moneyToGetAbs, "poteflon", "poteflony", "poteflonów");
			
			List<String> subtractionLore = new ArrayList<String>(Arrays.asList("§ePo zaakceptowaniu oferty", "§ez Twojego konta " + zostac, toSubtract + moneyToGetAbs + "§e" + poteflony + "."));
			List<String> additionLore = new ArrayList<String>(Arrays.asList("§ePo zaakceptowaniu oferty", "§edo Twojego konta " + zostac, toAdd + moneyToGetAbs + "§e" + poteflony + "."));
			ItemMeta metaplus = plus.getItemMeta();
			metaplus.setLore(additionLore);
			plus.setItemMeta(metaplus);
			ItemMeta metaminus = minus.getItemMeta();
			metaminus.setLore(subtractionLore);
			minus.setItemMeta(metaminus);
			
			
			if(moneyToGet < 0) {
				inv1.setItem(41, minus);
				inv2.setItem(41, plus);
			} else {
				inv1.setItem(41, plus);
				inv2.setItem(41, minus);
			}
		}
	}

	@Override public boolean areShiftclicksCustomlyHandled() 	{	return true;	};
	
	@Override public void close(Player whoClosed) {
		if(whoClosed != null) {
			Player secondPlayer = getSecondUser(whoClosed).getBukkitPlayer();
			secondPlayer.sendMessage(CoreUtils.chat.getCasualMessageWithHighlight("", whoClosed.getPlayer().getName(), " zrezygnował z handlu."));
			CoreUtils.ingame.playSoundPrivate(secondPlayer, false, "trade.second_denied");
		}
		if(acceptationStatus[0] && acceptationStatus[1]) {
			List<ItemStack> itemsForFirstPlayer = getItems(views[1]);
			List<ItemStack> itemsForSecondPlayer = getItems(views[0]);
			
			if(itemsForFirstPlayer != null && itemsForFirstPlayer.size() > 0)
				object.getBukkitPlayer().getInventory().addItem(itemsForFirstPlayer.toArray(new ItemStack[itemsForFirstPlayer.size()]));
			if(itemsForSecondPlayer != null && itemsForSecondPlayer.size() > 0)
				object2.getBukkitPlayer().getInventory().addItem(itemsForSecondPlayer.toArray(new ItemStack[itemsForFirstPlayer.size()]));
			
			((ExtendedPlayer) object).addMoney(moneyToGet[0]);
			((ExtendedPlayer) object2).addMoney(moneyToGet[1]);
		} else {
			List<ItemStack> itemsForFirstPlayer = getItems(views[0]);
			List<ItemStack> itemsForSecondPlayer = getItems(views[1]);
			
			if(itemsForFirstPlayer != null && itemsForFirstPlayer.size() > 0)
				object.getBukkitPlayer().getInventory().addItem(itemsForFirstPlayer.toArray(new ItemStack[itemsForFirstPlayer.size()]));
			if(itemsForSecondPlayer != null && itemsForSecondPlayer.size() > 0)
				object2.getBukkitPlayer().getInventory().addItem(itemsForSecondPlayer.toArray(new ItemStack[itemsForFirstPlayer.size()]));
		}
	}
	
	private List<ItemStack> getItems(InventoryView view){
		List<ItemStack> items = new ArrayList<>();
		for(int i : tradeSlots) {
			ItemStack item = view.getItem(i);
			if(!(item == null || item.getType().equals(Material.AIR)))
				items.add(item);
		}
		return items;
	}
}
