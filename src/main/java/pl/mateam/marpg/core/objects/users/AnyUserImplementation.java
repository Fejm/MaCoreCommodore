package pl.mateam.marpg.core.objects.users;

import static java.util.Collections.unmodifiableCollection;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.InventoryView;
import org.bukkit.potion.PotionEffect;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreSharedInventory;
import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.enums.chat.ChatState;
import pl.mateam.marpg.api.regular.enums.inventories.CommonInventory;
import pl.mateam.marpg.api.regular.events.PlayerInventoryChangeCommodoreEvent;
import pl.mateam.marpg.api.regular.modules.sub.server.SongsManager;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;
import pl.mateam.marpg.core.CommodoreCore;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.data.collections.EveryAccountEntity;
import pl.mateam.marpg.core.internal.helpers.ChatStateInfo;
import pl.mateam.marpg.core.modules.external.server.sub.EnvironmentManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.SkinsManagerImplementation;
import pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer.StandardPlayerInventory;

public abstract class AnyUserImplementation implements AnyUser {
	private static Map<Player, AnyUserImplementation> players = new HashMap<>();

	@Secret public static AnyUserImplementation get(Player player)		{	return players.get(player);										}
	@Secret public static Collection<AnyUser> getAll()					{	return unmodifiableCollection(players.values());	}
	

	public final EveryAccountEntity primaryInfo;
	protected final Player player;								@Override public Player getBukkitPlayer()		{	return player;			}
	
	protected AnyUserImplementation(Player player, EveryAccountEntity info){
		this.player = player;
		this.primaryInfo = info;
		players.put(player, this);
		Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> {	//Should be invoked after whole object is created
			setDefaultMaSkin();
			for(PotionEffect potion : player.getActivePotionEffects())
				player.removePotionEffect(potion.getType());
		});
	}
	
	protected AnyUserImplementation(AnyUserImplementation currentObject) {
		this(currentObject.player, currentObject.primaryInfo);
	}
	
	protected abstract void writeCurrentFieldsState();
	protected abstract Runnable writeAdditionalInfoToDatabase();
	
	@Secret public final void saveCurrentStateToDatabase() {
		writeCurrentFieldsState();
		Runnable runnable = () -> Core.getDatabase().getMorphiaDatastore().save(primaryInfo);
		boolean sync = CommodoreCore.getHiddenMethods().getHiddenData().isServerDown;
		if(sync) {
			writeAdditionalInfoToDatabase().run();
			runnable.run();
		}
		else {
			Bukkit.getScheduler().runTaskAsynchronously(MaCoreCommodoreEngine.getReference(), writeAdditionalInfoToDatabase());
			Bukkit.getScheduler().runTaskAsynchronously(MaCoreCommodoreEngine.getReference(), runnable);
		}
	}
	
	@Secret public void hasQuit(PlayerQuitEvent event) {
		if(event != null) {
			stopMusic();
			players.remove(this);
			Bukkit.getScheduler().runTaskAsynchronously(MaCoreCommodoreEngine.getReference(), () -> {
				saveCurrentStateToDatabase();
			});
		} else {
			closeInventory();
			saveCurrentStateToDatabase();
		}
	}

	private boolean hasCustomSkin = false;
	@Override public boolean hasCustomSkin()		{	return hasCustomSkin;	}
	@Override public final void setDefaultMaSkin() {
		setDefaultSkin();
		hasCustomSkin = false;
	}
	protected abstract void setDefaultSkin();
	@Override public final void setSkin(String nickOfPlayerPremium){
		if(nickOfPlayerPremium == null)
			setDefaultMaSkin();
		else if(((SkinsManagerImplementation) Core.getServer().getSkins()).changeSkin(player, nickOfPlayerPremium))
			hasCustomSkin = true;
	}
	
	@Secret public abstract void handleMessageSending(AsyncPlayerChatEvent event);

	@Override public void muteMusicPlayer() {
		stopMusic();
		primaryInfo.music.muted = true;
	}
	
	
	private int musicTaskID = -1;
	private String songName;
	@Override public void playMusicInTectonicMode() {
		if(primaryInfo.music.muted)		return;
		stopMusic();
		Player player = getBukkitPlayer();

		String newSongName = Core.getServer().getWorlds().getTectonicPlate(player.getLocation()).getMusicManager().getRandomSongName();
		if(newSongName == null)		return;

		this.songName = newSongName;
		player.playSound(player.getLocation(), "marpg.muzyka." + newSongName, 100F, 1);
		SongsManager music = Core.getServer().getSongs();
		musicTaskID = new BukkitRunnable() {
			@Override
			public void run() {
				new BukkitRunnable(){
					@Override
					public void run(){
						if(player.isOnline() && musicTaskID != -1){
							player.stopSound("marpg.muzyka." + newSongName);
							playMusicInTectonicMode();
						} else {
							if(musicTaskID != -1)
								Bukkit.getScheduler().cancelTask(musicTaskID);
						}				
					}
				}.runTask(MaCoreCommodoreEngine.getReference());
			}
		}.runTaskLaterAsynchronously(MaCoreCommodoreEngine.getReference(), music.getDurability(newSongName) + music.getIntervalBetweenSongs()).getTaskId();
	}
	@Override public void playSong(String songName, boolean looped) {
		if(primaryInfo.music.muted)		return;
		stopMusic();
		if(songName == null)	return;
		this.songName = songName;
		Player player = getBukkitPlayer();
		if(looped){
			musicTaskID = new BukkitRunnable(){
				public void run() {
					new BukkitRunnable() {
						public void run() {
							if(player.isOnline() && musicTaskID != -1){
								player.stopSound("marpg.muzyka." + songName);
								player.playSound(player.getLocation(), "marpg.muzyka." + songName, 100F, 1);
							} else
								this.cancel();							
						}
					}.runTask(MaCoreCommodoreEngine.getReference());
				}
			}.runTaskTimerAsynchronously(MaCoreCommodoreEngine.getReference(), 0, Core.getServer().getSongs().getDurability(songName)).getTaskId();
		} else {
			musicTaskID = new BukkitRunnable(){
				@Override public void run() {	AnyUserImplementation.this.songName = null;	}
			}.runTaskLaterAsynchronously(MaCoreCommodoreEngine.getReference(), Core.getServer().getSongs().getDurability(songName)).getTaskId();
		}
	}

	@Override public void stopMusic() {
		if(musicTaskID != -1){
			Player player = getBukkitPlayer();
			if(player != null && player.isOnline()) player.stopSound("marpg.muzyka." + songName);
			Bukkit.getScheduler().cancelTask(musicTaskID);
			this.musicTaskID = -1;
			this.songName = null;
		}
	}
	@Override public String getNameOfCurrentlyPlayingSong() 			{	return songName;		}
	
	protected abstract CommodoreInventory getDefaultInventory();


	private CommodoreInventory currentInventory = getDefaultInventory();
	@Secret public CommodoreInventory getOpenInventory()	{	return currentInventory;	}

	@Secret public void handleInventoryClicking(InventoryClickEvent event) 	{	currentInventory.handleClicking(event);		}
	@Override public abstract boolean hasOpenInventory();
	
	@Override public void closeInventory() 	{	closeInventory(null);	}

	private static Set<CommodoreInventory> sharedInventoriesToClose = new LinkedHashSet<>();
	private boolean isNewInventoryBeingOpened = false;
	private boolean hasInventoryJustBeenClosedNotNaturally = false;
	@Secret public void closeInventory(Player whoClosed) {
		if(hasInventoryJustBeenClosedNotNaturally) {
			hasInventoryJustBeenClosedNotNaturally = false;
			return;
		}
		InventoryView openInv = player.getOpenInventory();
		if(openInv == null)
			return;

		if(currentInventory.shouldCursorBeReturnedOnClose()) {
			if(Core.getServer().getItems().getCommodoreLayer(openInv.getCursor()) != null)
				player.getInventory().addItem(openInv.getCursor());
		}

		if(isNewInventoryBeingOpened)
			whoClosed = null;
				
		if(currentInventory instanceof CommodoreSharedInventory) {
			if(!sharedInventoriesToClose.remove(currentInventory)) {
				sharedInventoriesToClose.add(currentInventory);
				((CommodoreSharedInventory) currentInventory).getSecondUser(this).getBukkitPlayer().getOpenInventory().close();
				currentInventory.close(whoClosed);
			}
		} else currentInventory.close(whoClosed);

		player.getOpenInventory().setCursor(null);
		if(!isNewInventoryBeingOpened) {
			currentInventory = getDefaultInventory();
			Bukkit.getPluginManager().callEvent(new PlayerInventoryChangeCommodoreEvent(this));
		} else isNewInventoryBeingOpened = false;
		if(whoClosed == null) {
			hasInventoryJustBeenClosedNotNaturally = true;
			openInv.close();
		}
	}
	
	@Override public void openInventoryIfPossible(CommodoreInventory inventory) {
		if(!hasOpenInventory()) 	forceOpenInventory(inventory);
	}
	
	@Override public void openInventoryIfPossible(String remoteInventoryAccessKey) {
		if(!hasOpenInventory())		forceOpenInventory(remoteInventoryAccessKey);
	}
	
	@Override public void openInventoryIfPossible(CommonInventory inventoryType) {
		if(!hasOpenInventory())		forceOpenInventory(inventoryType);
	}
	
	@Override public void forceOpenInventory(CommodoreInventory inventory) {
		isNewInventoryBeingOpened = true;
		if(currentInventory instanceof StandardPlayerInventory)
			closeInventory();
		AnyUserImplementation second = null;
		if(inventory instanceof CommodoreSharedInventory) {
			second = (AnyUserImplementation) ((CommodoreSharedInventory) inventory).getSecondUser(this);
			second.isNewInventoryBeingOpened = true;
			if(second.currentInventory instanceof StandardPlayerInventory)
				closeInventory();
		}
		
		inventory.open();
		currentInventory = inventory;
		Bukkit.getPluginManager().callEvent(new PlayerInventoryChangeCommodoreEvent(this));
		if(inventory instanceof CommodoreSharedInventory) {
			second.currentInventory = inventory;
			Bukkit.getPluginManager().callEvent(new PlayerInventoryChangeCommodoreEvent(second));
		}
	}
	
	@Override public void forceOpenInventory(String remoteInventoryAccessKey) {
		Class<? extends CommodoreInventory> inventoryClass = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getRemoteInventory(remoteInventoryAccessKey);
		try {
			forceOpenInventory(inventoryClass.getDeclaredConstructor(AnyUser.class).newInstance(this));
		} catch (InstantiationException | IllegalAccessException
				| IllegalArgumentException | InvocationTargetException
				| NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	
	@Override public void forceOpenInventory(CommonInventory inventoryType) {
		CommodoreInventory inventoryToOpen = null;
		switch(inventoryType) {
			default:	int todo;	//	throw new RuntimeException("Do inventory nie została podpięta żadna klasa!");
		}

		if(inventoryToOpen != null)
			forceOpenInventory(inventoryToOpen);
	}
		
	protected ChatChanel typingChanel = null;				//Fast access
	@Override public ChatChanel getTypingChanel()				{	return typingChanel;								}
	@Override public ChatState getChatState(ChatChanel chanel)	{	return primaryInfo.chatStates[chanel.ordinal()];	}
	
	@Override public void setChatState(ChatChanel chanel, ChatState newState) {
		ChatState oldState = getChatState(chanel);
		if(oldState == newState)
			return;
		if(newState == ChatState.NOT_VISIBLE)
			if(this instanceof UserBasedPlayerImplementation || chanel != ChatChanel.MODERATOR) {
				if(((UserBasedPlayerImplementation) this).isModerator())
					throw new RuntimeException("Próbowano wyłączyć widoczność kanału moderatorów dla moderatora!");
			} else throw new RuntimeException("Użycie ChatState.NOT_VISIBLE jest możliwe tylko dla kanału moderatorów dla byłego moderatora.");
				
		
		if(this instanceof UserBasedPlayerImplementation) {
			boolean isBanned = ((UserBasedPlayerImplementation) this).checkChatBanExpirationTime(chanel);
			boolean newStateIsBanned = new ChatStateInfo(newState).isBanned();
			if(chanel == ChatChanel.GAMEMASTER)
				throw new RuntimeException("Ten gracz nie może uzyskać dostępu do czatu Mistrzów!");
			if(chanel == ChatChanel.MODERATOR) {
				boolean isModerator =  ((UserBasedPlayerImplementation) this).isModerator();
				if(!isModerator)
					newState = ChatState.NOT_VISIBLE;
				else if(!(newState == ChatState.ACTIVE || newState == ChatState.TYPING))
					newState = ChatState.ACTIVE;
			}
			if(isBanned) {
				if(!newStateIsBanned)
					throw new RuntimeException("Czat tego gracza nie był zablokowany! Jeżeli chcesz go zablokować, zrób to z poziomu menedżera banów.");
			} else
				if(newStateIsBanned)
					throw new RuntimeException("Czat tego gracza był zablokowany! Jeżeli chcesz go odblokować, zrób to z poziomu menedżera banów.");
		} else
			if(newState == ChatState.BLOCKED_ACTIVE || newState == ChatState.BLOCKED_MUTED)
				throw new RuntimeException("Czat Mistrza Gry nie może zostać zablokowany!");

		boolean wasActive = new ChatStateInfo(oldState).isActive();
		boolean isActive = 	new ChatStateInfo(newState).isActive();
		
		if(wasActive != isActive)
			if(wasActive)	chatChanelsListeners[chanel.ordinal()].remove(getBukkitPlayer());
			else			chatChanelsListeners[chanel.ordinal()].add(getBukkitPlayer());
		
		if(newState == ChatState.TYPING) {
			if(typingChanel != null) {
				primaryInfo.chatStates[typingChanel.ordinal()] = ChatState.ACTIVE;
				chatStateHasChanged(typingChanel);
			}
			typingChanel = chanel;
		} else if(typingChanel == chanel)
			typingChanel = null;

		primaryInfo.chatStates[chanel.ordinal()] = newState;
		chatStateHasChanged(chanel);
	}
	
	protected abstract void chatStateHasChanged(ChatChanel chanel);
	
	protected void pushMessage(TextComponent craftedMessage) 	{	chatChanelsListeners[typingChanel.ordinal()].forEach(player -> player.spigot().sendMessage(craftedMessage));	}
	
	private int messageLogging;

	@SuppressWarnings("unchecked")
    private final static Set<Player>[] chatChanelsListeners = new Set[ChatChanel.values().length];
	protected void assignToChatChannels() {
		for(int i = 0; i < primaryInfo.chatStates.length; i++) {
			ChatState chatState = primaryInfo.chatStates[i];
			if(new ChatStateInfo(chatState).isActive())
				chatChanelsListeners[i].add(getBukkitPlayer());
			if(chatState == ChatState.TYPING)
				typingChanel = ChatChanel.values()[i];
		}
	}

	static {
		for(int i = 0; i < chatChanelsListeners.length; i++)
			chatChanelsListeners[i] = new LinkedHashSet<>();
	}
	
	
	public static class CommodoreScoreboards {
		private static Scoreboard board = Bukkit.getServer().getScoreboardManager().getMainScoreboard();
		
		public static void init() {
			Set<Team> teamList = board.getTeams();
			for (Team team : teamList)
				team.unregister();
			
			registerTeam("masters", ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString(), null);
			for(int i = 1; i<100; i++){
				registerTeam("hbr0lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), null);
				registerTeam("hbr1lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), " " + ChatColor.DARK_RED.toString() + "\u258c" + ChatColor.DARK_GRAY.toString() + "\u258c\u258c\u258c\u258c\u258c\u258c\u258c\u258c\u258c");
				registerTeam("hbr2lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), " " + ChatColor.DARK_RED.toString() + "\u258c\u258c" + ChatColor.DARK_GRAY.toString() + "\u258c\u258c\u258c\u258c\u258c\u258c\u258c\u258c");
				registerTeam("hbr3lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), " " + ChatColor.RED.toString() + "\u258c\u258c\u258c" + ChatColor.DARK_GRAY.toString() + "\u258c\u258c\u258c\u258c\u258c\u258c\u258c");
				registerTeam("hbr4lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), " " + ChatColor.RED.toString() + "\u258c\u258c\u258c\u258c" + ChatColor.DARK_GRAY.toString() + "\u258c\u258c\u258c\u258c\u258c\u258c");
				registerTeam("hbr5lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), " " + ChatColor.YELLOW.toString() + "\u258c\u258c\u258c\u258c\u258c" + ChatColor.DARK_GRAY.toString() + "\u258c\u258c\u258c\u258c\u258c");
				registerTeam("hbr6lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), " " + ChatColor.YELLOW.toString() + "\u258c\u258c\u258c\u258c\u258c\u258c" + ChatColor.DARK_GRAY.toString() + "\u258c\u258c\u258c\u258c");
				registerTeam("hbr7lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), " " + ChatColor.GREEN.toString() + "\u258c\u258c\u258c\u258c\u258c\u258c\u258c" + ChatColor.DARK_GRAY.toString() + "\u258c\u258c\u258c");
				registerTeam("hbr8lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), " " + ChatColor.GREEN.toString() + "\u258c\u258c\u258c\u258c\u258c\u258c\u258c\u258c" + ChatColor.DARK_GRAY.toString() + "\u258c\u258c");
				registerTeam("hbr9lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), " " + ChatColor.DARK_GREEN.toString() + "\u258c\u258c\u258c\u258c\u258c\u258c\u258c\u258c\u258c" + ChatColor.DARK_GRAY.toString() + "\u258c");
				registerTeam("hbr10lv" + i, ChatColor.YELLOW.toString() + "Lv. " + ChatColor.GOLD.toString() + i + " " + ChatColor.WHITE.toString(), " " + ChatColor.DARK_GREEN.toString() + "\u258c\u258c\u258c\u258c\u258c\u258c\u258c\u258c\u258c\u258c");
			 }
		}
			
		private static void registerTeam(String name, String prefix, String suffix) {
			if(board.getTeam(name) == null){
				Team team = board.registerNewTeam(name);
				if(prefix != null)	team.setPrefix(prefix);
				if(suffix != null)	team.setSuffix(suffix);
			}
		}

		@SuppressWarnings("deprecation")
		public static void addPlayerToTeam(Player whom, String teamName) {
			board.getTeam(teamName).addPlayer(whom);
		}
	}
}
