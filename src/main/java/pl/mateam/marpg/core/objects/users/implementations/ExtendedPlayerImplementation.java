package pl.mateam.marpg.core.objects.users.implementations;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitScheduler;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.classes.observers.ExtendedPlayerActionLocalObserver;
import pl.mateam.marpg.api.regular.classes.observers.ExtendedPlayerActionObserver;
import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.enums.items.CommonInterfaceElement;
import pl.mateam.marpg.api.regular.enums.items.CommonSpecialItem;
import pl.mateam.marpg.api.regular.enums.items.Tier;
import pl.mateam.marpg.api.regular.enums.nbt.NBT_DefaultBonus;
import pl.mateam.marpg.api.regular.enums.players.CharacterClass;
import pl.mateam.marpg.api.regular.enums.players.Rank;
import pl.mateam.marpg.api.regular.events.PlayerInventoryChangeCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPFinishedCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPRequestExpiredCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPRequestSendCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerTradeRequestExpiredCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerTradeRequestSendCommodoreEvent;
import pl.mateam.marpg.api.regular.objects.items.CommodoreItem;
import pl.mateam.marpg.api.regular.objects.items.CommodoreSpecialItem;
import pl.mateam.marpg.api.regular.objects.items.special.CommodoreHandwear.HandwearEffectiveStats;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.data.collections.embedded.players.CharacterClassInfo;
import pl.mateam.marpg.core.internal.hardcoded.ItemSerializationSlot;
import pl.mateam.marpg.core.internal.hardcoded.OtherHardcoded;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo.ItemSlotType;
import pl.mateam.marpg.core.internal.utils.CommodoreCalculator;
import pl.mateam.marpg.core.internal.utils.Parsers;
import pl.mateam.marpg.core.modules.external.server.sub.EnvironmentManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.ItemsManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.SkinsManagerImplementation;
import pl.mateam.marpg.core.objects.effects.LevelUpCommodoreEffect;
import pl.mateam.marpg.core.objects.effects.PlayerSpawnCommodoreEffect;
import pl.mateam.marpg.core.objects.items.CommodoreSpecialItemImplementation;
import pl.mateam.marpg.core.objects.items.special.CommodoreArmorElementImplementation;
import pl.mateam.marpg.core.objects.items.special.CommodoreHandwearImplementation;
import pl.mateam.marpg.core.objects.items.special.CommodoreJewelleryElementImplementation;
import pl.mateam.marpg.core.objects.items.special.CommodoreShieldImplementation;
import pl.mateam.marpg.core.objects.users.UserBasedPlayerImplementation;
import pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer.ChatManagementInventory;
import pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer.InteractionWithAnotherPlayerInventory;
import pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer.StandardPlayerInventory;
import pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer.TradeInventory;

import com.mongodb.BasicDBObject;

public class ExtendedPlayerImplementation extends UserBasedPlayerImplementation implements ExtendedPlayer {
	private static final int MINIMAL_LEVEL_TO_CHAT = 2;
	
	private final CharacterClass characterClass;
	private final CharacterClassInfo currentCharacterInfo;
	private final boolean wasChatUnlocked;

	protected ExtendedPlayerImplementation(PlayerInLobbyImplementation currentObject, CharacterClass characterClass) {
		super(currentObject.getBukkitPlayer(), currentObject.primaryInfo, currentObject.additionalInfo);
		assignToChatChannels();
		this.characterClass = characterClass;
		
		CharacterClassInfo characterInfo = null;
		
		switch(characterClass){
			case WARRIOR:
				characterInfo = additionalInfo.warriorInfo;
				break;
			case BARBARIAN:
				characterInfo = additionalInfo.barbarianInfo;
				break;
			case MAGE:
				characterInfo = additionalInfo.mageInfo;
				break;
			case SLAYER:
				characterInfo = additionalInfo.slayerInfo;
				break;
		}
		
		showPlayersBidirectional();

		if(characterInfo == null){
			player.teleport(Core.getServer().getWorlds().getWarp("StartGry"));
			
			currentCharacterInfo = new CharacterClassInfo();
			switch(characterClass){
				case WARRIOR:
					additionalInfo.warriorInfo = currentCharacterInfo;
					break;
				case BARBARIAN:
					additionalInfo.barbarianInfo = currentCharacterInfo;
					break;
				case MAGE:
					additionalInfo.mageInfo = currentCharacterInfo;
					break;
				case SLAYER:
					additionalInfo.slayerInfo = currentCharacterInfo;
					break;
			}
		} else {
			currentCharacterInfo = characterInfo;
			restoreLocation();
			restoreInventory();
		}
		
		restoreInfo();
		refreshHealthBar();
		
		wasChatUnlocked = 	(additionalInfo.warriorInfo != null && additionalInfo.warriorInfo.level >= MINIMAL_LEVEL_TO_CHAT)
				||			(additionalInfo.barbarianInfo != null && additionalInfo.barbarianInfo.level >= MINIMAL_LEVEL_TO_CHAT)
				||			(additionalInfo.mageInfo != null && additionalInfo.mageInfo.level >= MINIMAL_LEVEL_TO_CHAT)
				||			(additionalInfo.slayerInfo != null && additionalInfo.slayerInfo.level >= MINIMAL_LEVEL_TO_CHAT);
		
		updateTextBase();
		new PlayerSpawnCommodoreEffect(player.getLocation()).playInternal();
		bonusTemporaryValues = new HashMap<>();
	}
	
	private void showPlayersBidirectional(){
		Collection<ExtendedPlayer> foreigners = Core.getServer().getUsers().getAllExtendedPlayers();
		foreigners.remove(this);
		foreigners.forEach(foreigner -> {
			foreigner.getBukkitPlayer().showPlayer(player);
			player.showPlayer(foreigner.getBukkitPlayer());
		});
		Collection<Gamemaster> gamemasters = Core.getServer().getUsers().getAllGamemasterObjects();
		gamemasters.forEach(gamemaster -> {
			if(gamemaster.isVisible())
				player.showPlayer(gamemaster.getBukkitPlayer());
		});
	}
	private void restoreInfo() {
		player.setLevel(currentCharacterInfo.level);
		player.setExp(currentCharacterInfo.experience);
		player.setHealth(currentCharacterInfo.health);
		int changesInEnergySystem;
		player.setFoodLevel(currentCharacterInfo.energy);
	}
	private void restoreLocation() {
		int bugSensitive;
		World world = Bukkit.getWorld(currentCharacterInfo.location.worldname);
		double x = currentCharacterInfo.location.x;
		double y = currentCharacterInfo.location.y;
		double z = currentCharacterInfo.location.z;
		float yaw = currentCharacterInfo.location.yaw;
		float pitch = currentCharacterInfo.location.pitch;
		player.teleport(new Location(world, x, y, z, yaw, pitch));
	}
	private void restoreInventory() {
		Map<String, Object> items = currentCharacterInfo.items;
		PlayerInventory inv = player.getInventory();
		
		ItemsManagerImplementation manager = ((ItemsManagerImplementation) Core.getServer().getItems());
		for(Entry<String, Object> entry : items.entrySet()){
			int itemSlot = Integer.parseInt((String) entry.getKey());	//Due to Morphia's deserialization problem, slotID is stored as String
			ItemStack item = manager.getFromBasicDBObject((BasicDBObject) entry.getValue());
			
			switch(itemSlot){
				case ItemSerializationSlot.HELMET:		inv.setHelmet(item);			recalculateStats(null, manager.getCommodoreLayer(item));	break;
				case ItemSerializationSlot.CHESTPLATE:	inv.setChestplate(item);		recalculateStats(null, manager.getCommodoreLayer(item));	break;
				case ItemSerializationSlot.LEGGINGS:	inv.setLeggings(item);			recalculateStats(null, manager.getCommodoreLayer(item));	break;
				case ItemSerializationSlot.BOOTS:		inv.setBoots(item);				recalculateStats(null, manager.getCommodoreLayer(item));	break;
				case ItemSerializationSlot.SHIELD:		inv.setItemInOffHand(item);		recalculateStats(null, manager.getCommodoreLayer(item));	break;
				default:								inv.setItem(itemSlot, item);	break;
			}
		}
		currentCharacterInfo.items = null;
		if(inv.getItem(ItemSerializationSlot.NECKLACE) == null)
			inv.setItem(ItemSerializationSlot.NECKLACE, manager.getInterfaceElement(CommonInterfaceElement.EMPTY_NECKLACE_SLOT));
		else {
			CommodoreItem layer = manager.getCommodoreLayer(inv.getItem(ItemSerializationSlot.NECKLACE));
			if(layer != null) recalculateStats(null, layer);
		}
	
		if(inv.getItem(ItemSerializationSlot.RING1) == null)
			inv.setItem(ItemSerializationSlot.RING1, manager.getInterfaceElement(CommonInterfaceElement.EMPTY_RING_SLOT));
		else {
			CommodoreItem layer = manager.getCommodoreLayer(inv.getItem(ItemSerializationSlot.RING1));
			if(layer != null) recalculateStats(null, layer);
		}

		if(inv.getItem(ItemSerializationSlot.RING2) == null)
			inv.setItem(ItemSerializationSlot.RING2, manager.getInterfaceElement(CommonInterfaceElement.EMPTY_RING_SLOT));
		else {
			CommodoreItem layer = manager.getCommodoreLayer(inv.getItem(ItemSerializationSlot.RING2));
			if(layer != null) recalculateStats(null, layer);
		}

		if(inv.getItem(8) == null)
			inv.setItem(8, manager.getInterfaceElement(CommonInterfaceElement.EMPTY_WEAPON_SLOT));
		else {
			CommodoreItem layer = manager.getCommodoreLayer(inv.getItem(8));
			if(layer != null) recalculateStats(null, layer);
		}
	}

	private long lastTexted;
	@Override public void handleMessageSending(AsyncPlayerChatEvent event) {
		int syf;
		CommodoreSpecialItem si = Core.getServer().getItems().getSpecialItem(CommonSpecialItem.DEFAULT_DAGGER, 0, Tier.DAMAGED);
		CommodoreSpecialItem si2 = Core.getServer().getItems().getSpecialItem(CommonSpecialItem.DEFAULT_STAFF, 0, Tier.DAMAGED);
		CommodoreSpecialItem si3 = Core.getServer().getItems().getSpecialItem(CommonSpecialItem.DEFAULT_AXE, 0, Tier.DAMAGED);
		CommodoreSpecialItem si4 = Core.getServer().getItems().getSpecialItem(CommonSpecialItem.DEFAULT_HAMMER, 0, Tier.DAMAGED);
		CommodoreSpecialItem si5 = Core.getServer().getItems().getSpecialItem(CommonSpecialItem.DEFAULT_SWORD, 0, Tier.DAMAGED);
		CommodoreSpecialItem si6 = Core.getServer().getItems().getSpecialItem(CommonSpecialItem.DEFAULT_GREATSWORD, 0, Tier.DAMAGED);
		event.getPlayer().getInventory().addItem(si.craftItemStack());
		event.getPlayer().getInventory().addItem(si2.craftItemStack());
		event.getPlayer().getInventory().addItem(si3.craftItemStack());
		event.getPlayer().getInventory().addItem(si4.craftItemStack());
		event.getPlayer().getInventory().addItem(si5.craftItemStack());
		event.getPlayer().getInventory().addItem(si6.craftItemStack());

		if(player.getLevel() < MINIMAL_LEVEL_TO_CHAT && !wasChatUnlocked){
			player.sendMessage(CoreUtils.chat.getErrorMessage("Aby korzystać z czatu tekstowego, musisz osiągnąć przynajmniej " + MINIMAL_LEVEL_TO_CHAT + " poziom postaci!"));
			CoreUtils.ingame.playSoundPrivate(player, true, "chat.no_level");
			return;
		}
		if(typingChanel == null) {
			player.sendMessage(CoreUtils.chat.getErrorMessage("Nie znajdujesz się na żadnym czacie tekstowym!"));
			CoreUtils.ingame.playSoundPrivate(player, true, "chat.no_room");
			return;
		}
		Instant currentTime = Instant.now();
		if(!isModerator()) {
			if(lastTexted > Instant.now().minusSeconds(Core.getServer().getEnvironment().getCurrentMessageSendingDelayInSeconds()).toEpochMilli()) {
				player.sendMessage(CoreUtils.chat.getErrorMessage("Poczekaj, zanim wyślesz kolejną wiadomość."));
				CoreUtils.ingame.playSoundPrivate(player, true, "chat.wait");
				return;
			}
			lastTexted = currentTime.toEpochMilli();
		}
		messageBase.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(messageBasePrefix + Core.getServer().getWorlds().getTectonicPlate(player.getLocation()).getSettingsManager().getName() + messageBasePostfix).create()));
		
		
		TextComponent base = new TextComponent(ChatColor.DARK_AQUA + new SimpleDateFormat("[HH:mm] ").format(currentTime.toEpochMilli()));
		base.addExtra(new TextComponent(Core.getServer().getEnvironment().getChatMessagePrefix(typingChanel)));
		base.addExtra(" ");
		base.addExtra(messageBase);
        base.addExtra(new TextComponent(event.getMessage()));
		
        pushMessage(base);
	}

	
	@Override protected void writeCurrentFieldsState() 	{	currentCharacterInfo.writeCurrentFieldsState(player);	}
	
	


	@Secret public double getBonusRawValue(NBT_DefaultBonus bonusKey) 			{	return getBonusRawValue(bonusKey.getKey());							}
	@Secret public double getBonusTemporaryValue(NBT_DefaultBonus bonusKey) 	{	return getBonusTemporaryValue(bonusKey.getKey());					}
	@Secret public double getBonusValue(NBT_DefaultBonus bonusKey) 				{	return getBonusValue(bonusKey.getKey());							}
	@Secret public double getBonusRawValue(String bonusKey) 					{	return bonusRawValues.get(bonusKey);								}
	@Secret public double getBonusTemporaryValue(String bonusKey) 				{	return bonusTemporaryValues.get(bonusKey);							}
	@Secret public double getBonusValue(String bonusKey) 						{	return getBonusValue(bonusKey) + getBonusTemporaryValue(bonusKey);	}
	@Override public double getDamageBase()										{	return damageBase;													}
	@Override public double getRandomDamage() {
		double damageRandomizedMultiplier = 1 + ((Math.random() * 2 - 1) * damageAmplitude/100D);
		return damageRandomizedMultiplier * damageBase;
	}
	@Override public double getSkillDamageBase()								{	return skillDamageBase;					}
	@Override public int getDamageAmplitude()									{	return damageAmplitude;					}
	
	@Override public int getDefence()											{	return defence;							}
	@Override public double getBlockingRate()									{	return blocking;						}
	@Override public int getHpRegeneration()									{	return hpReg;							}
	@Override public int getEpRegeneration()									{	return epReg;							}
	@Override public boolean isInBattleMode() 									{	int todo;	return true;				}
	@Override public void switchToBattleMode()									{	int todo;								}
	
	@Override public void refreshHealthBar()	{	changeHPLevel(0); 	}
	@Override public void changeHPLevel(double value){
		double currentHP = player.getHealth();
		if(currentHP + value > 20) {
			player.setHealth(20);
        	CommodoreScoreboards.addPlayerToTeam(player, "hbr0lv" + player.getLevel());
        	return;
		}
		player.setHealth(currentHP + value);
		if(!player.isDead()) {
	        int intHealth = (int) (player.getHealth() / 2);
	        if(intHealth < player.getHealth() / 2)	intHealth++;
	        CommodoreScoreboards.addPlayerToTeam(player, "hbr" + intHealth + "lv" + player.getLevel());
		}
	}
	
	@Override public void changeEPLevel(double value) {
		int todo;
	}
	
	

	private double damageBase = CommodoreCalculator.getFistsDamage();
	private double skillDamageBase = 0.5;
	private int damageAmplitude = Core.getServer().getMechanics().getFistsDamageAmplitude();
	
	private int defence;
	private double blocking;
	private int hpReg;
	private int epReg;
	
	private final Map<String, Integer> bonusRawValues = new HashMap<>();
	private final Map<String, Integer> bonusTemporaryValues;
	private int possibilityToGiveTemporaryEffect;
	/* 0 - weapon
	 * 1 - helmet
	 * 2 - chestplate
	 * 3 - leggings
	 * 4 - boots
	 * 5 - shield
	 * 6 - necklace
	 * 7 - ring */
	private final long[] lastChangedInventoryItem = new long[8];

	@Secret public void recalculateStats(CommodoreItem clickedItemLayer, CommodoreItem newItem) {
		if(clickedItemLayer != null) {
			for(Entry<String, Integer> bonus : ((CommodoreSpecialItemImplementation) clickedItemLayer).getBonusesValues().entrySet()) {
				Integer newValue = bonusRawValues.remove(bonus.getKey()) - bonus.getValue();
				if(newValue > 0)	bonusRawValues.put(bonus.getKey(), newValue);
			}
		}
		
		if(newItem != null) {
			for(Entry<String, Integer> bonus : ((CommodoreSpecialItemImplementation) newItem).getBonusesValues().entrySet()) {
				Integer value = bonusRawValues.remove(bonus.getKey());
				value = value == null? bonus.getValue() : value + bonus.getValue();
				bonusRawValues.put(bonus.getKey(), value);
			}
		}
		
		if(clickedItemLayer instanceof CommodoreHandwearImplementation || newItem instanceof CommodoreHandwearImplementation) {
			if(newItem == null) {
				damageBase = CommodoreCalculator.getFistsDamage();
				skillDamageBase = 0.5;
				damageAmplitude = Core.getServer().getMechanics().getFistsDamageAmplitude();
			} else {
				HandwearEffectiveStats stats = ((CommodoreHandwearImplementation) newItem).getEffectiveStats();
				damageBase = stats.getAverageDamage();
				skillDamageBase = stats.getFinalSkillDamageMultiplier();
				damageAmplitude = ((CommodoreHandwearImplementation) newItem).getDamageAmplitudePercent();
			}
			return;
		}
		
		if(clickedItemLayer instanceof CommodoreArmorElementImplementation)
			defence -= ((CommodoreArmorElementImplementation) clickedItemLayer).getEffectiveStats().getDefence();
		if(newItem instanceof CommodoreArmorElementImplementation)
			defence += ((CommodoreArmorElementImplementation) newItem).getEffectiveStats().getDefence();
		
		if(clickedItemLayer instanceof CommodoreJewelleryElementImplementation) {
			CommodoreJewelleryElementImplementation jewellery = (CommodoreJewelleryElementImplementation) clickedItemLayer;
			if(jewellery.isNecklace())
				hpReg -= jewellery.getEffectiveStats().getRegenerationRate();
			else
				epReg -= jewellery.getEffectiveStats().getRegenerationRate();
		}
		if(newItem instanceof CommodoreJewelleryElementImplementation) {
			CommodoreJewelleryElementImplementation jewellery = (CommodoreJewelleryElementImplementation) newItem;
			if(jewellery.isNecklace())
				hpReg += jewellery.getEffectiveStats().getRegenerationRate();
			else
				epReg += jewellery.getEffectiveStats().getRegenerationRate();
		}
		
		if(clickedItemLayer instanceof CommodoreShieldImplementation || newItem instanceof CommodoreShieldImplementation)
			blocking = newItem != null ? ((CommodoreShieldImplementation) newItem).getEffectiveStats().getBlockRate() : 0;
		
		int damageDebug;
	}
	
	@Secret public boolean canChangeItemYet(ItemSlotType slot)	{	return lastChangedInventoryItem[slot.ordinal()] < Instant.now().minusSeconds(5).toEpochMilli();		}
	private int timeBelowIsHardcoded = 0;
	@Secret public void notifyThatItemGotEquipped(ItemSlotType slot) {
		Instant instant = Instant.now();
		lastChangedInventoryItem[slot.ordinal()] = instant.toEpochMilli();
	}
	
	private boolean hasJustJoined = true;
	@SuppressWarnings("deprecation")
	@Secret public void handle(PlayerLevelChangeEvent event) {
		if(hasJustJoined) {
			hasJustJoined = false;
			return;
		}
		player.setExp(0);
		changeHPLevel(player.getMaxHealth());
		changeEPLevel(20);

		Inventory inv = player.getInventory();
		if(event.getNewLevel() <= OtherHardcoded.LEVEL_MAX){
			inv.setContents(inv.getContents());	//Inventory refreshing
			new LevelUpCommodoreEffect(player.getLocation().add(0, 1, 0)).playInternal(event.getNewLevel(), getRank().getHSVArmorColorValue());;
			int todo;
//			Core.getUtils().minecraft().sendParticles(event.getPlayer().getLocation().add(0, 0.8, 0), ParticleEffect.SPELL_WITCH, 250, 0.3f, 0.5f);
			CoreUtils.ingame.playSoundPrivate(player, false, "other.levelup");
		}
		else
			player.setLevel(OtherHardcoded.LEVEL_MAX);
		
		if(getOpenInventory() instanceof InteractionWithAnotherPlayerInventory)
			((InteractionWithAnotherPlayerInventory) getOpenInventory()).handlePlayerLevelChangeEvent(event);
		
		observedBy.forEach((handler, observerset) -> observerset.forEach(observer -> handler.handle(event, this, observer)));
	}

	
	@Override public CharacterClass getCharacterClass() 	{	return characterClass;					}
	@Override public int getMoney()							{	return currentCharacterInfo.money;		}
	@Override public void addMoney(int amount)				{	currentCharacterInfo.money += amount;	}

	@Override protected void setDefaultSkin() {
		SkinsManagerImplementation manager = ((SkinsManagerImplementation) Core.getServer().getSkins());
		String skinName = manager.buildSkinName(getCharacterClass(), isWoman(), getRank(), false);
		manager.changeSkin(player, skinName);
	}
	
	@Override public boolean hasOpenInventory() 	{	return !(getOpenInventory() instanceof StandardPlayerInventory);	}
	@Override public boolean canInterract(String playername)	{
		if(!hasOpenInventory())
			return true;
		CommodoreInventory openinv = getOpenInventory();
		return openinv instanceof InteractionWithAnotherPlayerInventory && (( InteractionWithAnotherPlayerInventory) openinv).getObservedPlayer()
																			.getBukkitPlayer().getName().equals(playername); 
	}
			


	private int todoWChuj;
	
	@Override protected CommodoreInventory getDefaultInventory() 	{	return new StandardPlayerInventory(this);	}
	@Override protected void chatStateHasChanged(ChatChanel chanel) {
		CommodoreInventory openInventory = getOpenInventory();
		if(openInventory instanceof ChatManagementInventory)
			((ChatManagementInventory) openInventory).refreshChatState(chanel);
	}
	
	private TextComponent messageBase;
	private String messageBasePrefix, messageBasePostfix;
	private void updateTextBase() {
		TextComponent messageBaseBuilder = new TextComponent();
		if(isModerator()) {
			messageBaseBuilder.setText(ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + player.getName() + ": ");
			messageBasePrefix = getRank().getExplicitTitle(player.getName()) + "\n" + ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Moderator";
		} else {
			messageBaseBuilder.setText(getRank().getColoredNickname(player.getName()) + ": ");
			messageBasePrefix = getRank().getExplicitTitle(player.getName());
		}
		
		messageBasePrefix += "\n\n" + ChatColor.YELLOW.toString() + "Klasa postaci: " + ChatColor.WHITE.toString() + getCharacterClass().getIngameName()
	       		+ "\n" + ChatColor.YELLOW.toString() + "Lokalizacja: " + ChatColor.WHITE.toString();
		
		messageBase = messageBaseBuilder;
		
		messageBasePostfix = "\n" + ChatColor.YELLOW.toString() + "Poziom: " + ChatColor.WHITE.toString() + player.getLevel()
		         		+ "\n" + ChatColor.YELLOW.toString() + "Płeć: " + ChatColor.WHITE.toString() + Parsers.genderToString(isWoman());
	}
	
	@Override public void setRank(Rank rank) {
		super.setRank(rank);
		updateTextBase();
	}
	
	@Override public void setModerator(boolean state) {
		super.setModerator(state);
	}
	
	private Map<String, Integer> pvpPropositions = new HashMap<>();
	private Map<String, Integer> tradePropositions = new HashMap<>();
	private Set<ExtendedPlayerImplementation> proposedPvPs = new LinkedHashSet<>();
	private Set<ExtendedPlayerImplementation> proposedTrades = new LinkedHashSet<>();
	private ExtendedPlayerImplementation pvpOpponent = null;
	private boolean hasPvPStarted = false;
	@Override public ExtendedPlayer getPvPOpponent() 	{	return pvpOpponent;		}
	@Override public boolean hasPvPStartedYet()			{	return hasPvPStarted;	}
	
	@Override public boolean hasProposedPvP(String playername)		{	return pvpPropositions.containsKey(playername);		}
	@Override public boolean hasProposedTrade(String playername)	{	return tradePropositions.containsKey(playername);	}
	
	@Secret public void proposePvP(ExtendedPlayerImplementation who) {
		Player second = who.getBukkitPlayer();
		String playername = second.getName();
		Integer taskID = pvpPropositions.remove(playername);
		if(taskID != null)
			Bukkit.getScheduler().cancelTask(taskID);
		if(who.getPvPOpponent() == null && proposedPvPs.contains(who)) {
			pvpOpponent = who;
			who.pvpOpponent = this;
			Bukkit.getPluginManager().callEvent(new PlayerPvPRequestSendCommodoreEvent(who, this, true));
			proposedPvPs.remove(who);
			Bukkit.getScheduler().cancelTask(who.pvpPropositions.remove(player.getName()));
			
			addObserver(who, PvPOpponentObserver.class);
			who.addObserver(this, PvPOpponentObserver.class);
			CoreUtils.ingame.playSoundPrivate(player, true, "pvp.accepted");
			CoreUtils.ingame.playSoundPrivate(second, true, "pvp.accepted");
			CoreUtils.ingame.sendTitle(player, 0, 200, 0, "§aPojedynek z §2§l" + playername, "§fDo rozpoczęcia pojedynku pozostało §f§l10 sekund§f.");
			CoreUtils.ingame.sendTitle(who.getBukkitPlayer(), 0, 200, 0, "§aPojedynek z §2§l" + player.getName(), "§fDo rozpoczęcia pojedynku pozostało §f§l10 sekund§f.");
			recursion(who, 9);
		}
		else {
			pvpPropositions.put(playername, Bukkit.getScheduler().runTaskLater(MaCoreCommodoreEngine.getReference(),
				() -> {
					pvpPropositions.remove(playername);
					who.proposedPvPs.remove(ExtendedPlayerImplementation.this);
					Bukkit.getPluginManager().callEvent(new PlayerPvPRequestExpiredCommodoreEvent(who, ExtendedPlayerImplementation.this));
				}, 200).getTaskId());
			Bukkit.getPluginManager().callEvent(new PlayerPvPRequestSendCommodoreEvent(who, this, false));
			who.proposedPvPs.add(this);
		}
	}
	
	@Secret public void proposeTrade(ExtendedPlayerImplementation who) {
		Player second = who.getBukkitPlayer();
		String playername = second.getName();
		Integer taskID = tradePropositions.remove(playername);
		if(taskID != null)
			Bukkit.getScheduler().cancelTask(taskID);
		if(proposedTrades.contains(who)) {
			Bukkit.getPluginManager().callEvent(new PlayerTradeRequestSendCommodoreEvent(who, this, true));
			proposedTrades.remove(who);
			Bukkit.getScheduler().cancelTask(who.tradePropositions.remove(player.getName()));
			forceOpenInventory(new TradeInventory(who, this));
		}
		else {
			tradePropositions.put(playername, Bukkit.getScheduler().runTaskLater(MaCoreCommodoreEngine.getReference(),
				() -> {
					tradePropositions.remove(playername);
					who.proposedTrades.remove(ExtendedPlayerImplementation.this);
					Bukkit.getPluginManager().callEvent(new PlayerTradeRequestExpiredCommodoreEvent(who, ExtendedPlayerImplementation.this));
				}, 200).getTaskId());
			Bukkit.getPluginManager().callEvent(new PlayerTradeRequestSendCommodoreEvent(who, this, false));
			who.proposedTrades.add(this);
		}
	}
	
	
	
	@SuppressWarnings("deprecation")
	private void recursion(ExtendedPlayerImplementation extSecond, int secondsLeft) {
		Bukkit.getScheduler().runTaskLater(MaCoreCommodoreEngine.getReference(), new BukkitRunnable() {
			@Override
			public void run() {
				Player second = extSecond.getBukkitPlayer();
				if(pvpOpponent == extSecond) {
					String text = Parsers.getProperForm(secondsLeft, "pozostała 1 sekunda", " pozostały " + secondsLeft + " sekundy", "pozostało " + secondsLeft + " sekund");
					CoreUtils.ingame.sendTitle(player, 0, 200, 0, "§aPojedynek z §2§l" + second.getName(), "§fDo rozpoczęcia pojedynku " + text);
					CoreUtils.ingame.sendTitle(second, 0, 200, 0, "§aPojedynek z §2§l" + player.getName(), "§fDo rozpoczęcia pojedynku " + text);
					if(secondsLeft > 0)
						recursion(extSecond, secondsLeft-1);
					else
						if(pvpOpponent == extSecond) {
							hasPvPStarted = true;
							extSecond.hasPvPStarted = true;
							player.sendMessage(CoreUtils.chat.getCasualMessage("Pojedynek rozpoczął się!"));
							second.sendMessage(CoreUtils.chat.getCasualMessage("Pojedynek rozpoczął się!"));
							CoreUtils.ingame.sendTitle(player, 0, 0, 40, "§4§lJAZDA!", "§cPojedynek rozpoczął się!");
							CoreUtils.ingame.sendTitle(second, 0, 0, 40, "§4§lJAZDA!", "§cPojedynek rozpoczął się!");
							CoreUtils.ingame.playSoundPrivate(player, true, "pvp.started");
							CoreUtils.ingame.playSoundPrivate(second, true, "pvp.started");
						}
				}
			}
		}, 20);
	}
	
	private Map<ExtendedPlayerActionObserver, Set<ExtendedPlayer>> observedBy = new HashMap<>();
	private Map<Class<? extends Event>, Set<ExtendedPlayerActionLocalObserver>> eventLocalObservers = new HashMap<>();
	private Set<ExtendedPlayerImplementation> observing = new LinkedHashSet<>();
	
	@Secret public void addObserver(ExtendedPlayerImplementation observer, Class<? extends ExtendedPlayerActionObserver> observingClass) {
		ExtendedPlayerActionObserver observerInstance = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getExtendedPlayerActionObserver(observingClass);
		observedBy.putIfAbsent(observerInstance, new LinkedHashSet<>());
		observedBy.get(observerInstance).add(observer);
		observer.observing.add(this);
	}
	
	@Secret public void removeObserver(ExtendedPlayerImplementation observer, Class<? extends ExtendedPlayerActionObserver> observingClass) {
		ExtendedPlayerActionObserver observerInstance = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getExtendedPlayerActionObserver(observingClass);
		Set<ExtendedPlayer> observers = observedBy.get(observerInstance);
		if(observers != null)
			observers.remove(observer);
		observer.observing.remove(this);
	}
	
	@Override public void addObserver(ExtendedPlayer observer, String observingModuleRemoteName) {
		ExtendedPlayerActionObserver observerInstance = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getExtendedPlayerActionObserver(observingModuleRemoteName);
		observedBy.putIfAbsent(observerInstance, new LinkedHashSet<>());
		observedBy.get(observerInstance).add(observer);
		((ExtendedPlayerImplementation) observer).observing.add(this);
	}

	@Override public void removeObserver(ExtendedPlayer observer, String observingModuleRemoteName) {
		ExtendedPlayerActionObserver observerInstance = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getExtendedPlayerActionObserver(observingModuleRemoteName);
		Set<ExtendedPlayer> observers = observedBy.get(observerInstance);
		if(observers != null)
			observers.remove(observer);
		((ExtendedPlayerImplementation) observer).observing.remove(this);
	}
	
	@Secret public void addLocalObserver(Class<? extends Event> observedEvent, Class<? extends ExtendedPlayerActionLocalObserver> observingClass) {
		ExtendedPlayerActionLocalObserver observerInstance = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getExtendedPlayerLocalActionObserver(observingClass);
		eventLocalObservers.putIfAbsent(observedEvent, new LinkedHashSet<>());
		eventLocalObservers.get(observedEvent).add(observerInstance);
	}
	
	@Secret public void removeLocalObserver(Class<? extends Event> observedEvent, Class<? extends ExtendedPlayerActionLocalObserver> observingClass) {
		ExtendedPlayerActionLocalObserver observerInstance = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getExtendedPlayerLocalActionObserver(observingClass);
		Set<ExtendedPlayerActionLocalObserver> observers = eventLocalObservers.get(observedEvent);
		if(observers != null)
			observers.remove(observerInstance);
	}

	@Override public void addLocalObserver(Class<? extends Event> observedEvent, String observingModuleRemoteName) {
		ExtendedPlayerActionLocalObserver observerInstance = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getExtendedPlayerLocalActionObserver(observingModuleRemoteName);
		eventLocalObservers.putIfAbsent(observedEvent, new LinkedHashSet<>());
		eventLocalObservers.get(observedEvent).add(observerInstance);
	}
	
	@Override public void removeLocalObserver(Class<? extends Event> observedEvent, String observingModuleRemoteName) {
		ExtendedPlayerActionLocalObserver observerInstance = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getExtendedPlayerLocalActionObserver(observingModuleRemoteName);
		Set<ExtendedPlayerActionLocalObserver> observers = eventLocalObservers.get(observedEvent);
		if(observers != null)
			observers.remove(observerInstance);
	}
	
	@Override @Secret public void hasQuit(PlayerQuitEvent event) {
		super.hasQuit(event);
		observedBy.forEach((handler, observerset) -> observerset.forEach(observer -> {
			handler.handle(event, this, observer);
			((ExtendedPlayerImplementation) observer).observing.remove(ExtendedPlayerImplementation.this);
		}));
		if(event != null) {
			observedBy = null;
			observing.forEach(extPlayer -> extPlayer.observedBy.entrySet().forEach(entry -> entry.getValue().remove(this)));
			cancelRequests();
		}
	}
	
	@Secret public void handle(PlayerDeathEvent event) {
		Set<ExtendedPlayerActionLocalObserver> observers = eventLocalObservers.get(PlayerDeathEvent.class);
		if(observers != null)
			observers.forEach(observer -> observer.handle(event));
		observedBy.forEach((handler, observerset) -> observerset.forEach(observer -> handler.handle(event, this, observer)));
	}
	
	@Secret public void handle(PlayerPvPRequestExpiredCommodoreEvent event) {
		Set<ExtendedPlayerActionLocalObserver> observers = eventLocalObservers.get(PlayerPvPRequestExpiredCommodoreEvent.class);
		if(observers != null)
			observers.forEach(observer -> observer.handle(event));
		observedBy.forEach((handler, observerset) -> observerset.forEach(observer -> handler.handle(event, this, observer)));
	}

	@Secret public void handle(PlayerPvPRequestSendCommodoreEvent event) {
		Set<ExtendedPlayerActionLocalObserver> observers = eventLocalObservers.get(PlayerPvPRequestSendCommodoreEvent.class);
		if(observers != null)
			observers.forEach(observer -> observer.handle(event));
		observedBy.forEach((handler, observerset) -> observerset.forEach(observer -> handler.handle(event, this, observer)));
	}
	
	@Secret public void handle(PlayerTradeRequestExpiredCommodoreEvent event) {
		Set<ExtendedPlayerActionLocalObserver> observers = eventLocalObservers.get(PlayerTradeRequestExpiredCommodoreEvent.class);
		if(observers != null)
			observers.forEach(observer -> observer.handle(event));
		observedBy.forEach((handler, observerset) -> observerset.forEach(observer -> handler.handle(event, this, observer)));
	}

	@Secret public void handle(PlayerTradeRequestSendCommodoreEvent event) {
		Set<ExtendedPlayerActionLocalObserver> observers = eventLocalObservers.get(PlayerTradeRequestSendCommodoreEvent.class);
		if(observers != null)
			observers.forEach(observer -> observer.handle(event));
		observedBy.forEach((handler, observerset) -> observerset.forEach(observer -> handler.handle(event, this, observer)));
	}

	@Secret public void handle(PlayerInventoryChangeCommodoreEvent event) {
		Set<ExtendedPlayerActionLocalObserver> observers = eventLocalObservers.get(PlayerInventoryChangeCommodoreEvent.class);
		if(observers != null)
			observers.forEach(observer -> observer.handle(event));
		observedBy.forEach((handler, observerset) -> observerset.forEach(observer -> handler.handle(event, this, observer)));
	}
	
	private void cancelRequests() {
		BukkitScheduler scheduler = Bukkit.getScheduler();
		pvpPropositions.values().forEach(taskID -> scheduler.cancelTask(taskID));
		proposedPvPs.forEach(extPlayer -> {
			Integer taskID = pvpPropositions.get(this);
			if(taskID != null)
				scheduler.cancelTask(taskID);
		});
		pvpPropositions = null;
		proposedPvPs = null;
		
		tradePropositions.values().forEach(taskID -> scheduler.cancelTask(taskID));
		proposedTrades.forEach(extPlayer -> {
			Integer taskID = tradePropositions.get(this);
			if(taskID != null)
				scheduler.cancelTask(taskID);
		});
		tradePropositions = null;
		proposedTrades = null;
	}
	
	private void finishPvP() {
		if(pvpOpponent != null) {
			pvpOpponent.pvpOpponent = null;
			pvpOpponent.hasPvPStarted = false;
			pvpOpponent = null;
			hasPvPStarted = false;
		}
	}
	
	public static class PvPOpponentObserver extends ExtendedPlayerActionObserver {
		@Override public void handle(PlayerQuitEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {
			if(event != null) {
				observer.getBukkitPlayer().sendMessage(CoreUtils.chat.getCasualMessageWithHighlight("Twój przeciwnik, ", caller.getBukkitPlayer().getName(), " uciekł z walki! Hańba!"));
				CoreUtils.ingame.sendTitle(observer.getBukkitPlayer(), 0, 20, 40, "§cPojedynek z §4§l" + caller.getBukkitPlayer().getName(), "§cPrzeciwnik uciekł z walki!");
				CoreUtils.ingame.playSoundPrivate(observer.getBukkitPlayer(), true, "pvp.coward");
				Bukkit.getPluginManager().callEvent(new PlayerPvPFinishedCommodoreEvent(observer, caller, true));
				((ExtendedPlayerImplementation) caller).finishPvP();
			}
		}
		@Override public void handle(PlayerDeathEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {
			observer.getBukkitPlayer().sendMessage(CoreUtils.chat.getCasualMessageWithHighlight("Pojedynek z ", caller.getBukkitPlayer().getName(), " zakończony! Chwalebny triumf!"));
			CoreUtils.ingame.sendTitle(observer.getBukkitPlayer(), 0, 20, 40, "§aPojedynek z §2§l" + caller.getBukkitPlayer().getName(), "§aZwycięstwo!");
			CoreUtils.ingame.playSoundPrivate(observer.getBukkitPlayer(), true, "pvp.victory");
			Bukkit.getPluginManager().callEvent(new PlayerPvPFinishedCommodoreEvent(observer, caller, false));
			((ExtendedPlayerImplementation) caller).finishPvP();
		}
	}
}
