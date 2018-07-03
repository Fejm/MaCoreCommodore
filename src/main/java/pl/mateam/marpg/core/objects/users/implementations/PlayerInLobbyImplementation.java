package pl.mateam.marpg.core.objects.users.implementations;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;



import net.minecraft.server.v1_12_R1.EntityArmorStand;
import net.minecraft.server.v1_12_R1.EntityPlayer;
import net.minecraft.server.v1_12_R1.EnumItemSlot;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityEquipment;
import net.minecraft.server.v1_12_R1.PacketPlayOutEntityHeadRotation;
import net.minecraft.server.v1_12_R1.PacketPlayOutNamedEntitySpawn;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerInfo.EnumPlayerInfoAction;
import net.minecraft.server.v1_12_R1.PacketPlayOutSpawnEntityLiving;
import net.minecraft.server.v1_12_R1.PlayerInteractManager;
import net.minecraft.server.v1_12_R1.WorldServer;



import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_12_R1.CraftServer;
import org.bukkit.craftbukkit.v1_12_R1.CraftWorld;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;



import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.enums.items.CommonInterfaceElement;
import pl.mateam.marpg.api.regular.enums.players.CharacterClass;
import pl.mateam.marpg.api.regular.events.PlayerClassPickCommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;
import pl.mateam.marpg.api.regular.objects.users.PlayerInLobby;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.data.collections.EveryAccountEntity;
import pl.mateam.marpg.core.data.collections.PlayerAccountEntity;
import pl.mateam.marpg.core.data.collections.embedded.players.CharacterClassInfo;
import pl.mateam.marpg.core.data.collections.embedded.players.CharacterClassInfo.LocationInfo;
import pl.mateam.marpg.core.internal.hardcoded.ItemSerializationSlot;
import pl.mateam.marpg.core.internal.utils.InternalIngameUtils;
import pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.characterclass.inventory.InventoryDatabaseReaderImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.SkinsManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.SkinsManagerImplementation.SkinObject;
import pl.mateam.marpg.core.objects.users.UserBasedPlayerImplementation;
import pl.mateam.marpg.core.objects.users.implementations.chathandlers.lobby.AbstractLobbyChatHandler;
import pl.mateam.marpg.core.objects.users.implementations.chathandlers.lobby.LoggedInChatHandler;
import pl.mateam.marpg.core.objects.users.implementations.chathandlers.lobby.NotLoggedInChatHandler;
import pl.mateam.marpg.core.objects.users.implementations.inventories.lobby.LobbyInventory;



import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;


public class PlayerInLobbyImplementation extends UserBasedPlayerImplementation implements PlayerInLobby {
	@Override protected void writeCurrentFieldsState()	 {}

	private AbstractLobbyChatHandler chatHandler = new NotLoggedInChatHandler(this);
	public PlayerInLobbyImplementation(Player player, EveryAccountEntity primaryInfo, PlayerAccountEntity additionalInfo) {
		super(player, primaryInfo, additionalInfo);
		Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> {
			player.setGameMode(GameMode.ADVENTURE);
			player.teleport(Core.getServer().getWorlds().getWarp("LobbyStart"));
			player.addPotionEffect(new PotionEffect(PotionEffectType.SLOW, Integer.MAX_VALUE, 100, false, false), true);
			player.setHealth(20);
			player.setFoodLevel(20);
			player.setLevel(0);
			player.setExp(0);
			player.getInventory().clear();
			player.getInventory().setItem(ItemSerializationSlot.NECKLACE, Core.getServer().getItems().getInterfaceElement(CommonInterfaceElement.EMPTY_NECKLACE_SLOT));
			player.getInventory().setItem(ItemSerializationSlot.RING1, Core.getServer().getItems().getInterfaceElement(CommonInterfaceElement.EMPTY_RING_SLOT));
			player.getInventory().setItem(ItemSerializationSlot.RING2, Core.getServer().getItems().getInterfaceElement(CommonInterfaceElement.EMPTY_RING_SLOT));
			player.getInventory().setItem(8, Core.getServer().getItems().getInterfaceElement(CommonInterfaceElement.EMPTY_WEAPON_SLOT));
			if(player.getInventory().getHeldItemSlot() == 8)
				player.getInventory().setHeldItemSlot(7);
			hidePlayersBidirectional();	
			playMusicInTectonicMode();
		});
	}
	
	@Secret public void setChatHandler(AbstractLobbyChatHandler chatHandler) {
		this.chatHandler = chatHandler;
	}
	
	private void hidePlayersBidirectional() {
		Collection<PlayerInLobby> foreigners = Core.getServer().getUsers().getAllPlayersInLobby();
		foreigners.remove(this);
		foreigners.forEach(foreigner -> {
			foreigner.getBukkitPlayer().hidePlayer(player);
			player.hidePlayer(foreigner.getBukkitPlayer());		
		});
		Core.getServer().getUsers().getAllGamemasterObjects().forEach(foreigner -> player.hidePlayer(foreigner.getBukkitPlayer()));
	}
	
	@Override protected void setDefaultSkin() {
		SkinsManagerImplementation manager = ((SkinsManagerImplementation) Core.getServer().getSkins());
		String skinName = manager.buildSkinName(null, isWoman(), getRank(), false);
		manager.changeSkin(player, skinName);
	}

	@Override public void handleMessageSending(AsyncPlayerChatEvent event) 	{	chatHandler.handleMessageSending(event);	}
	
	@Override public void handleInventoryClicking(InventoryClickEvent event) {
		event.setCancelled(true);
	}
	
	@Override public boolean isRegistered() {	return additionalInfo.password != null;				}
	@Override public boolean isLoggedIn() 	{	return chatHandler instanceof LoggedInChatHandler;	}
	
	public static class PacketNPC extends EntityPlayer {
		private static Map<Integer, PacketNPC> npcs = new HashMap<>();

		public static void init() {
			createNPCbase(CharacterClass.WARRIOR, "Wojownik", "PostacWojownik", npcs);
			createNPCbase(CharacterClass.BARBARIAN, "Barbarzyńca", "PostacBarbarzynca", npcs);
			createNPCbase(CharacterClass.MAGE, "Mag", "PostacMag", npcs);
			createNPCbase(CharacterClass.SLAYER, "Zabójca", "PostacZabojca", npcs);
		}
		
		private static void createNPCbase(CharacterClass characterClass, String name, String locationName, Map<Integer, PacketNPC> npcs){
			PacketNPC npc = new PacketNPC(characterClass, name, Core.getServer().getWorlds().getWarp(locationName));
			npcs.put(npc.getId(), npc);
		}
		
		public final CharacterClass characterClass;
		public final Location location;
		
		private PacketNPC(CharacterClass characterClass, String name, Location location){
			super(((CraftServer) Bukkit.getServer()).getServer(), ((CraftWorld) Bukkit.getWorlds().get(0)).getHandle(),
					new GameProfile(UUID.randomUUID(), name), new PlayerInteractManager(((CraftWorld) Bukkit.getWorlds().get(0)).getHandle()));
			this.location = location;
			this.setLocation(location.getX(), location.getY(), location.getZ(), location.getYaw(), location.getPitch());
			this.characterClass = characterClass;
		}
				
		private void spawn(PlayerInLobbyImplementation lobbyObject, CharacterClass characterClass, boolean isGray) {
			getProfile().getProperties().removeAll("textures");
			String skin = ((SkinsManagerImplementation) Core.getServer().getSkins()).buildSkinName(characterClass, lobbyObject.isWoman(), lobbyObject.getRank(), isGray);
			SkinObject skinObject = ((SkinsManagerImplementation) Core.getServer().getSkins()).get(skin);
			
			getProfile().getProperties().put("textures", new Property("textures", skinObject.getValue(), skinObject.getSignature()));
			
			PacketPlayOutPlayerInfo info = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.ADD_PLAYER, this);
			PacketPlayOutNamedEntitySpawn spawn = new PacketPlayOutNamedEntitySpawn(this);
			CoreUtils.nms.sendPacket(info, lobbyObject.player);					
			Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> {
				CoreUtils.nms.sendPacket(spawn, lobbyObject.player);					
		        Bukkit.getScheduler().scheduleSyncDelayedTask(MaCoreCommodoreEngine.getReference(), () -> {
		            PacketPlayOutPlayerInfo info2 = new PacketPlayOutPlayerInfo(EnumPlayerInfoAction.REMOVE_PLAYER, PacketNPC.this);
					CoreUtils.nms.sendPacket(info2, lobbyObject.player);					
		        }, 20);
			});
		}
	}
	
	@Secret public void showLobby() {
		Bukkit.getScheduler().runTaskAsynchronously(MaCoreCommodoreEngine.getReference(), () -> {
			for(PacketNPC npc : PacketNPC.npcs.values()){
				CharacterClass characterClass = npc.characterClass;
				Location location = npc.location;

				try {
					Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> {
						CharacterClassInfo classInfo;
						
						switch(characterClass){
							case WARRIOR:	classInfo = additionalInfo.warriorInfo;		break;
							case BARBARIAN:	classInfo = additionalInfo.barbarianInfo;	break;
							case MAGE:		classInfo = additionalInfo.mageInfo;		break;
							case SLAYER:	classInfo = additionalInfo.slayerInfo;		break;
							default:		classInfo = null;							break;
						}
						
						boolean playedBefore = classInfo != null;
						
						try{
							if(playedBefore) {
								LocationInfo storedLocation = classInfo.location;
								int bugSensitive;
								Location loc = new Location(Bukkit.getServer().getWorld(storedLocation.worldname),
															storedLocation.x,
															storedLocation.y,
															storedLocation.z,
															storedLocation.yaw,
															storedLocation.pitch);
								
								sendPrivateHologram(location, 
										ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Czas gry",
										ChatColor.YELLOW + CoreUtils.parsing.timeInSecondsToText(classInfo.timePlayed),
										ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Lokalizacja",
										ChatColor.YELLOW + Core.getServer().getWorlds().getTectonicPlate(loc).getSettingsManager().getName(),
										" ",
										ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Poziom: " + ChatColor.YELLOW + classInfo.level,
										ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Exp: " + ChatColor.YELLOW + CoreUtils.math.round(classInfo.experience * 100, 2) + "%",
										ChatColor.GOLD.toString() + ChatColor.BOLD.toString() + "Poteflony: " + ChatColor.YELLOW + classInfo.money);
								npc.spawn(this, characterClass, false);
								
								Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> {
									PacketPlayOutEntityHeadRotation packet = new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw / 360.0F * 256.0F));
									CoreUtils.nms.sendPacket(packet, player);					
													
									PacketPlayOutEntityEquipment eqPacket;
									eqPacket = new PacketPlayOutEntityEquipment(npc.getId(), EnumItemSlot.MAINHAND, CraftItemStack.asNMSCopy(InventoryDatabaseReaderImplementation.restoreItemFromSlot(classInfo.items, 8)));
									CoreUtils.nms.sendPacket(eqPacket, player);					
									eqPacket = new PacketPlayOutEntityEquipment(npc.getId(), EnumItemSlot.HEAD, CraftItemStack.asNMSCopy(InternalIngameUtils.setArmorValidColor(InventoryDatabaseReaderImplementation.restoreItemFromSlot(classInfo.items, ItemSerializationSlot.HELMET), getRank().getHSVArmorColorValue())));
									CoreUtils.nms.sendPacket(eqPacket, player);					
									eqPacket = new PacketPlayOutEntityEquipment(npc.getId(), EnumItemSlot.CHEST, CraftItemStack.asNMSCopy(InternalIngameUtils.setArmorValidColor(InventoryDatabaseReaderImplementation.restoreItemFromSlot(classInfo.items, ItemSerializationSlot.CHESTPLATE), getRank().getHSVArmorColorValue())));
									CoreUtils.nms.sendPacket(eqPacket, player);					
									eqPacket = new PacketPlayOutEntityEquipment(npc.getId(), EnumItemSlot.LEGS, CraftItemStack.asNMSCopy(InternalIngameUtils.setArmorValidColor(InventoryDatabaseReaderImplementation.restoreItemFromSlot(classInfo.items, ItemSerializationSlot.LEGGINGS), getRank().getHSVArmorColorValue())));
									CoreUtils.nms.sendPacket(eqPacket, player);					
									eqPacket = new PacketPlayOutEntityEquipment(npc.getId(), EnumItemSlot.FEET, CraftItemStack.asNMSCopy(InternalIngameUtils.setArmorValidColor(InventoryDatabaseReaderImplementation.restoreItemFromSlot(classInfo.items, ItemSerializationSlot.BOOTS), getRank().getHSVArmorColorValue())));
									CoreUtils.nms.sendPacket(eqPacket, player);					
									eqPacket = new PacketPlayOutEntityEquipment(npc.getId(), EnumItemSlot.OFFHAND, CraftItemStack.asNMSCopy(InventoryDatabaseReaderImplementation.restoreItemFromSlot(classInfo.items, ItemSerializationSlot.SHIELD)));
									CoreUtils.nms.sendPacket(eqPacket, player);					
								});
							} else {
								sendPrivateHologram(location, ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "<Nie istnieje>");
								npc.spawn(this, characterClass, true);
							}
						} catch(Exception e){
							e.printStackTrace();
						}
					});
				} catch(Exception e){
					e.printStackTrace();
				}
			}				
		});
	}
	
	private void sendPrivateHologram(Location location, String... text){
		location.add(0, 0.25 + text.length*0.25, 0);
		WorldServer nmsRpg = ((CraftWorld) location.getWorld()).getHandle();
		for (String line : text) {
			location.subtract(0, 0.25, 0);
			EntityArmorStand as = new EntityArmorStand(nmsRpg, location.getX(), location.getY(), location.getZ());
			as.setCustomNameVisible(true);
			as.setInvisible(true);
			as.setNoGravity(true);
			as.setCustomName(line);
			PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(as);
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
	}
	
	private boolean hasClickedAtNPC = false;
	@Secret public void handleClassPicking(int clickedNPCbukkitEntityID){
		Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> {
			if(!hasClickedAtNPC){
				hasClickedAtNPC = true;
				PacketNPC cc = PacketNPC.npcs.getOrDefault(clickedNPCbukkitEntityID, null);
				if(cc == null)
					return;
				
				ExtendedPlayer extPlayer = new ExtendedPlayerImplementation(this, cc.characterClass);
				Bukkit.getServer().getPluginManager().callEvent(new PlayerClassPickCommodoreEvent(extPlayer));
			}
		});
	}

	@Override protected CommodoreInventory getDefaultInventory() 	{	return new LobbyInventory(this);	}

	@Override protected void chatStateHasChanged(ChatChanel chanel) {}

	@Override public boolean hasOpenInventory() 	{	return !(getOpenInventory() instanceof LobbyInventory);	}
}
