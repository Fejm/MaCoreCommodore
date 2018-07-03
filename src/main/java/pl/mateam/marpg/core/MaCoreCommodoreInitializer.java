package pl.mateam.marpg.core;

import org.bukkit.Bukkit;
import org.bukkit.plugin.PluginManager;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.core.data.listeners.AsyncPlayerChatEventListener;
import pl.mateam.marpg.core.data.listeners.AsyncPlayerPreLoginEventListener;
import pl.mateam.marpg.core.data.listeners.BlockBreakEventListener;
import pl.mateam.marpg.core.data.listeners.BlockGrowEventListener;
import pl.mateam.marpg.core.data.listeners.BlockPlaceEventListener;
import pl.mateam.marpg.core.data.listeners.EntityCombustEventListener;
import pl.mateam.marpg.core.data.listeners.EntityInteractEventListener;
import pl.mateam.marpg.core.data.listeners.HangingBreakEventListener;
import pl.mateam.marpg.core.data.listeners.InventoryClickEventListener;
import pl.mateam.marpg.core.data.listeners.InventoryCloseEventListener;
import pl.mateam.marpg.core.data.listeners.InventoryDragEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerBucketEmptyEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerBucketFillEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerCommandPreProcessEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerDeathEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerDropItemEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerInteractEntityEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerInteractEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerItemHeldEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerJoinEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerLevelChangeEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerLoginEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerPickupItemEventClass;
import pl.mateam.marpg.core.data.listeners.PlayerQuitEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerResourcePackStatusEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerShearEntityEventListener;
import pl.mateam.marpg.core.data.listeners.PlayerSwapHandItemsEventListener;
import pl.mateam.marpg.core.data.listeners.PrepareItemCraftEventListener;
import pl.mateam.marpg.core.data.listeners.ServerListPingEventListener;
import pl.mateam.marpg.core.data.listeners.StructureGrowEventListener;
import pl.mateam.marpg.core.data.listeners.commodoreevents.GamemasterVisibilityChangeCommodoreEventListener;
import pl.mateam.marpg.core.data.listeners.commodoreevents.PlayerInventoryChangeCommodoreEventListener;
import pl.mateam.marpg.core.data.listeners.commodoreevents.PlayerPvPRequestExpiredCommodoreEventListener;
import pl.mateam.marpg.core.data.listeners.commodoreevents.PlayerPvPRequestSendCommodoreEventListener;
import pl.mateam.marpg.core.data.listeners.commodoreevents.PlayerTradeRequestExpiredCommodoreEventListener;
import pl.mateam.marpg.core.data.listeners.commodoreevents.PlayerTradeRequestSendCommodoreEventListener;
import pl.mateam.marpg.core.data.listeners.protocollib.ClientUseEntity;
import pl.mateam.marpg.core.data.listeners.protocollib.ServerOutServerInfo;
import pl.mateam.marpg.core.data.listeners.protocollib.ServerSetSlotServerWindowsItems;
import pl.mateam.marpg.core.data.tasks.CharacterDataSaverTask;
import pl.mateam.marpg.core.data.tasks.RegenerationTask;
import pl.mateam.marpg.core.modules.external.server.sub.WorldsManagerImplementation;
import pl.mateam.marpg.core.modules.external.utils.ChatUtilsImplementation;
import pl.mateam.marpg.core.modules.external.utils.DeveloperUtilsImplementation;
import pl.mateam.marpg.core.modules.external.utils.IOUtilsImplementation;
import pl.mateam.marpg.core.modules.external.utils.IngameUtilsImplementation;
import pl.mateam.marpg.core.modules.external.utils.MathUtilsImplementation;
import pl.mateam.marpg.core.modules.external.utils.NMSUtilsImplementation;
import pl.mateam.marpg.core.modules.external.utils.ParsingUtilsImplementation;
import pl.mateam.marpg.core.objects.users.AnyUserImplementation.CommodoreScoreboards;
import pl.mateam.marpg.core.objects.users.implementations.PlayerInLobbyImplementation.PacketNPC;
import pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer.TradeInventory;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;

class MaCoreCommodoreInitializer {
	private final String BAR = MaCoreCommodoreEngine.BAR;
	private final String TITLE = MaCoreCommodoreEngine.TITLE;
	private final String RELEASE_DATE = MaCoreCommodoreEngine.RELEASE_DATE;
	private final MaCoreCommodoreEngine ENGINE;

	private void sendInitialMessages(){
		CoreUtils.io.sendConsoleMessageImportant(BAR);
		CoreUtils.io.sendConsoleMessageImportant("");
		CoreUtils.io.sendConsoleMessageImportant("");
		CoreUtils.io.sendConsoleMessageImportant("");



		CoreUtils.io.sendConsoleMessageImportant("            /\\/\\   __ _  / __\\___  _ __ ___           ");
		CoreUtils.io.sendConsoleMessageImportant("           /    \\ / _` |/ /  / _ \\| '__/ _ \\          ");
		CoreUtils.io.sendConsoleMessageImportant("          / /\\/\\ \\ (_| / /__| (_) | | |  __/          ");
		CoreUtils.io.sendConsoleMessageImportant("          \\/    \\/\\__,_\\____/\\___/|_|  \\___|          ");
		CoreUtils.io.sendConsoleMessageImportant("                                                      ");
		CoreUtils.io.sendConsoleMessageImportant("   ___   ___                  ___  ___  ___  __    __ ");
		CoreUtils.io.sendConsoleMessageImportant("  / __\\ /___\\/\\/\\    /\\/\\    /___\\/   \\/___\\/__\\  /__\\");
		CoreUtils.io.sendConsoleMessageImportant(" / /   //  //    \\  /    \\  //  // /\\ //  // \\// /_\\  ");
		CoreUtils.io.sendConsoleMessageImportant("/ /___/ \\_// /\\/\\ \\/ /\\/\\ \\/ \\_// /_// \\_// _  \\//__  ");
		CoreUtils.io.sendConsoleMessageImportant("\\____/\\___/\\/    \\/\\/    \\/\\___/___,'\\___/\\/ \\_/\\__/");



		CoreUtils.io.sendConsoleMessageImportant("");
		CoreUtils.io.sendConsoleMessageImportant("");
		CoreUtils.io.sendConsoleMessageImportant("");
		CoreUtils.io.sendConsoleMessageImportant(BAR);
		CoreUtils.io.sendConsoleMessageImportant("MaCoreCommodore " + "v." + ENGINE.getDescription().getVersion());
		CoreUtils.io.sendConsoleMessageImportantWithHighlight("", "(" + TITLE + ")", "");
		CoreUtils.io.sendConsoleMessageImportant("");
		CoreUtils.io.sendConsoleMessageImportantWithHighlight("Pierwsza kompilacja wersji: ", RELEASE_DATE, "");
		CoreUtils.io.sendConsoleMessageImportant(BAR);
	}
	
	private void setupCommodoreRoot(){
		try {
			//We can not use CoreUtils.developer yet.
			DeveloperUtilsImplementation devUtils = new DeveloperUtilsImplementation();
			
			devUtils.overrideReflectiveFinalField(CoreUtils.class, null, "initialize math utils", new MathUtilsImplementation());
			devUtils.overrideReflectiveFinalField(CoreUtils.class, null, "initialize chat utils", new ChatUtilsImplementation());
			devUtils.overrideReflectiveFinalField(CoreUtils.class, null, "initialize ingame utils", new IngameUtilsImplementation());
			devUtils.overrideReflectiveFinalField(CoreUtils.class, null, "initialize io utils", new IOUtilsImplementation());
			devUtils.overrideReflectiveFinalField(CoreUtils.class, null, "initialize parsing utils", new ParsingUtilsImplementation());
			devUtils.overrideReflectiveFinalField(CoreUtils.class, null, "initialize developer utils", devUtils);
			devUtils.overrideReflectiveFinalField(CoreUtils.class, null, "initialize nms utils", new NMSUtilsImplementation());
			
			//Saving instance of CommodoreCore
			devUtils.overrideReflectiveFinalField(Core.class, null, "initialize commodore", new CommodoreCore());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public MaCoreCommodoreInitializer(MaCoreCommodoreEngine engine){
		long startTime = System.nanoTime();

		this.ENGINE = engine;
		setupCommodoreRoot();		//Setup registries
		sendInitialMessages();
		
		((WorldsManagerImplementation) Core.getServer().getWorlds()).prepareWorlds();
		
		CommodoreCore.getHiddenMethods().startEnabling(ENGINE);	//Saving above settings manually. Normally this methods triggers automatically on CommodoreAddOn's final onEnable() method.

		ENGINE.regenerateResources();
		ENGINE.reloadConfiguration();
		registerEventListeners();
		registerTasks();
		initializeMiscellaneousClasses();

		CommodoreCore.getHiddenMethods().enablingFinished(ENGINE);

		double timeInSeconds = CoreUtils.math.round(((double) ((System.nanoTime() - startTime) / 1000000))/1000, 3);
		CoreUtils.io.sendConsoleMessageImportant("");
		CoreUtils.io.sendConsoleMessageImportantWithHighlight("- " + ENGINE.getName() + ": załadowano! Zajęło to ", timeInSeconds + "s.", "");
		CoreUtils.io.sendConsoleMessageImportant("");
		CoreUtils.io.sendConsoleMessageImportant(BAR);
		CoreUtils.io.sendConsoleMessageImportant("");
	}
	
	private void registerEventListeners(){
		PluginManager manager = Bukkit.getPluginManager();
		
		manager.registerEvents(new AsyncPlayerChatEventListener(), ENGINE);
		manager.registerEvents(new AsyncPlayerPreLoginEventListener(), ENGINE);
		manager.registerEvents(new BlockBreakEventListener(), ENGINE);
		manager.registerEvents(new BlockGrowEventListener(), ENGINE);
		manager.registerEvents(new BlockPlaceEventListener(), ENGINE);
		manager.registerEvents(new EntityCombustEventListener(), ENGINE);
		manager.registerEvents(new EntityInteractEventListener(), ENGINE);
		manager.registerEvents(new HangingBreakEventListener(), ENGINE);
		manager.registerEvents(new InventoryClickEventListener(), ENGINE);
		manager.registerEvents(new InventoryCloseEventListener(), ENGINE);
		manager.registerEvents(new InventoryDragEventListener(), ENGINE);
		manager.registerEvents(new PlayerBucketEmptyEventListener(), ENGINE);
		manager.registerEvents(new PlayerBucketFillEventListener(), ENGINE);
		manager.registerEvents(new PlayerCommandPreProcessEventListener(), ENGINE);
		manager.registerEvents(new PlayerDeathEventListener(), ENGINE);
		manager.registerEvents(new PlayerDropItemEventListener(), ENGINE);
		manager.registerEvents(new PlayerInteractEntityEventListener(), ENGINE);
		manager.registerEvents(new PlayerInteractEventListener(), ENGINE);
		manager.registerEvents(new PlayerItemHeldEventListener(), ENGINE);
		manager.registerEvents(new PlayerJoinEventListener(), ENGINE);
		manager.registerEvents(new PlayerLevelChangeEventListener(), ENGINE);
		manager.registerEvents(new PlayerLoginEventListener(), ENGINE);
		manager.registerEvents(new PlayerPickupItemEventClass(), ENGINE);
		manager.registerEvents(new PlayerQuitEventListener(), ENGINE);
		manager.registerEvents(new PlayerResourcePackStatusEventListener(), ENGINE);
		manager.registerEvents(new PlayerShearEntityEventListener(), ENGINE);
		manager.registerEvents(new PlayerSwapHandItemsEventListener(), ENGINE);
		manager.registerEvents(new PrepareItemCraftEventListener(), ENGINE);
		manager.registerEvents(new ServerListPingEventListener(), ENGINE);
		manager.registerEvents(new StructureGrowEventListener(), ENGINE);
		
		manager.registerEvents(new GamemasterVisibilityChangeCommodoreEventListener(), ENGINE);
		manager.registerEvents(new PlayerInventoryChangeCommodoreEventListener(), ENGINE);
		manager.registerEvents(new PlayerPvPRequestExpiredCommodoreEventListener(), ENGINE);
		manager.registerEvents(new PlayerPvPRequestSendCommodoreEventListener(), ENGINE);
		manager.registerEvents(new PlayerTradeRequestExpiredCommodoreEventListener(), ENGINE);
		manager.registerEvents(new PlayerTradeRequestSendCommodoreEventListener(), ENGINE);
		
		ProtocolManager pManager = ProtocolLibrary.getProtocolManager();
		pManager.removePacketListeners(ENGINE);

		new ClientUseEntity();
		new ServerOutServerInfo();
		new ServerSetSlotServerWindowsItems();
	}
	
	private void registerTasks() {
		new CharacterDataSaverTask().register();
		new RegenerationTask().register();
	}
	
	private void initializeMiscellaneousClasses(){
		PacketNPC.init();
		CommodoreScoreboards.init();
		TradeInventory.init();
	}
}
