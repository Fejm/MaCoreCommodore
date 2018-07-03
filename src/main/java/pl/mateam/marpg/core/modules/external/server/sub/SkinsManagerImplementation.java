package pl.mateam.marpg.core.modules.external.server.sub;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.enums.players.CharacterClass;
import pl.mateam.marpg.api.regular.enums.players.Rank;
import pl.mateam.marpg.api.regular.modules.sub.server.SkinsManager;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.modules.external.utils.DeveloperUtilsImplementation;

public class SkinsManagerImplementation implements SkinsManager {
	public SkinObject get(String skinName)	{	return renderedSkins.get(skinName);	}
	
	private static HashMap<String, SkinObject> renderedSkins = new HashMap<>();
	
	/* ---------------
	 * Secret methods
	 * --------------- */
	
	@Secret public String buildSkinName(CharacterClass characterClass, boolean woman, Rank rank, boolean grey){
		String skinName = "";
		if(characterClass == null)
			skinName += "New";
		else {
			switch(characterClass){
				case WARRIOR:
					skinName = "Woj";
					break;
				case BARBARIAN:
					skinName = "Bar";
					break;
				case MAGE:
					skinName = "Mag";
					break;
				case SLAYER:
					skinName = "Zab";
					break;
			}
		}
		if(woman)	skinName += "Kob";
		else		skinName += "Men";
		switch(rank){
			case PREMIUM:
				skinName += "Vip";
				break;
			case NOBLEMAN:
				skinName += "Noble";
				break;
			default: break;
		}
		if(grey) skinName += "Gray";
		return skinName;
	}
	
	@Secret public boolean changeSkin(Player player, String skin){
		try{
			renderUsingPremiumNickname(skin, false);
			if(!renderedSkins.containsKey(skin))	return false;
			Object skinObject = renderedSkins.get(skin).getSkin();
			
			DeveloperUtilsImplementation utils = (DeveloperUtilsImplementation) CoreUtils.developer;
			Object cp = utils.getBukkitClass("entity.CraftPlayer").cast(player);
			Object ep = utils.invokeMethod(cp.getClass(), cp, "getHandle");
			Object profile = utils.invokeMethod(ep.getClass(), ep, "getProfile");
			Object propmap = utils.invokeMethod(profile.getClass(), profile, "getProperties");
			utils.invokeMethod(propmap, "clear");
			utils.invokeMethod(propmap.getClass(), propmap, "put", new Class[] { Object.class, Object.class }, new Object[] { "textures", skinObject });

			Bukkit.getScheduler().runTaskAsynchronously(MaCoreCommodoreEngine.getReference(), new Runnable() {
				@Override
				public void run() {
					if (!player.isOnline()){
						return;
					}
					try {
						Object cp = utils.getBukkitClass("entity.CraftPlayer").cast(player);
						Object ep = utils.invokeMethod(cp, "getHandle");
						Location l = player.getLocation();

						List<Object> set = new ArrayList<>();
						set.add(ep);
						Iterable<?> iterable = set;

						Object removeInfo = utils
								.invokeConstructor(
										utils
												.getNMSClass("PacketPlayOutPlayerInfo"),
										new Class<?>[] {
												utils.getEnum(utils.getNMSClass("PacketPlayOutPlayerInfo"),
														"EnumPlayerInfoAction", "REMOVE_PLAYER").getClass(),
												Iterable.class },
								utils.getEnum(utils.getNMSClass("PacketPlayOutPlayerInfo"),
										"EnumPlayerInfoAction", "REMOVE_PLAYER"), iterable);

						Object removeEntity = utils.invokeConstructor(
								utils.getNMSClass("PacketPlayOutEntityDestroy"), new Class<?>[] { int[].class },
								new int[] { player.getEntityId() });

						Object addNamed = utils.invokeConstructor(
								utils.getNMSClass("PacketPlayOutNamedEntitySpawn"),
								new Class<?>[] { utils.getNMSClass("EntityHuman") }, ep);

						Object addInfo = utils
								.invokeConstructor(
										utils
												.getNMSClass("PacketPlayOutPlayerInfo"),
										new Class<?>[] {
												utils.getEnum(utils.getNMSClass("PacketPlayOutPlayerInfo"),
														"EnumPlayerInfoAction", "ADD_PLAYER").getClass(),
												Iterable.class },
								utils.getEnum(utils.getNMSClass("PacketPlayOutPlayerInfo"),
										"EnumPlayerInfoAction", "ADD_PLAYER"), iterable);


						
						Object world = utils.invokeMethod(ep, "getWorld");
						Object difficulty = utils.invokeMethod(world, "getDifficulty");
						Object worlddata = utils.getObject(world, "worldData");
						Object worldtype = utils.invokeMethod(worlddata, "getType");

						Object worldserver = utils.getNMSClass("WorldServer").cast(world);
						int dimension = (int) utils.getObject(worldserver, "dimension");

						Object playerIntManager = utils.getObject(ep, "playerInteractManager");
						Enum<?> enumGamemode = (Enum<?>) utils.invokeMethod(playerIntManager, "getGameMode");

						int gmid = (int) utils.invokeMethod(enumGamemode, "getId");

						Object respawn = utils
								.invokeConstructor(utils.getNMSClass("PacketPlayOutRespawn"),
										new Class<?>[] { int.class,
												utils.getEnum(utils.getNMSClass("EnumDifficulty"), "PEACEFUL")
														.getClass(),
												worldtype.getClass(), enumGamemode.getClass() },
										dimension, difficulty, worldtype, utils.invokeMethod(enumGamemode.getClass(), null,
												"getById", new Class<?>[] { int.class }, new Object[] { gmid }));


						 Object	pos = utils.invokeConstructor(utils.getNMSClass("PacketPlayOutPosition"),
									new Class<?>[] { double.class, double.class, double.class, float.class, float.class, Set.class,
											int.class },
									l.getX(), l.getY(), l.getZ(), l.getYaw(), l.getPitch(), new HashSet<Enum<?>>(), 0);

						Object hand = null;
						Object mainhand = null;
						Object offhand = null;
						Object helmet = null;
						Object boots = null;
						Object chestplate = null;
						Object leggings = null;

						
							mainhand = utils.invokeConstructor(utils.getNMSClass("PacketPlayOutEntityEquipment"),
									new Class<?>[] { int.class,
											utils.getEnum(utils.getNMSClass("EnumItemSlot"), "MAINHAND")
													.getClass(),
											utils.getNMSClass("ItemStack") },
									player.getEntityId(),
									utils.getEnum(utils.getNMSClass("EnumItemSlot"), "MAINHAND"),
									utils.invokeMethod(utils.getBukkitClass("inventory.CraftItemStack"), null,
											"asNMSCopy", new Class<?>[] { ItemStack.class },
											new Object[] { player.getInventory().getItemInMainHand() }));

							offhand = utils.invokeConstructor(utils.getNMSClass("PacketPlayOutEntityEquipment"),
									new Class<?>[] { int.class,
											utils.getEnum(utils.getNMSClass("EnumItemSlot"), "OFFHAND")
													.getClass(),
											utils.getNMSClass("ItemStack") },
									player.getEntityId(),
									utils.getEnum(utils.getNMSClass("EnumItemSlot"), "OFFHAND"),
									utils.invokeMethod(utils.getBukkitClass("inventory.CraftItemStack"), null,
											"asNMSCopy", new Class<?>[] { ItemStack.class },
											new Object[] { player.getInventory().getItemInOffHand() }));

							helmet = utils
									.invokeConstructor(utils.getNMSClass("PacketPlayOutEntityEquipment"),
											new Class<?>[] { int.class,
													utils.getEnum(utils.getNMSClass("EnumItemSlot"), "HEAD")
															.getClass(),
													utils.getNMSClass("ItemStack") },
											player.getEntityId(),
											utils.getEnum(utils.getNMSClass("EnumItemSlot"), "HEAD"),
											utils.invokeMethod(utils.getBukkitClass("inventory.CraftItemStack"),
													null, "asNMSCopy", new Class<?>[] { ItemStack.class },
													new Object[] { player.getInventory().getHelmet() }));

							chestplate = utils
									.invokeConstructor(utils.getNMSClass("PacketPlayOutEntityEquipment"),
											new Class<?>[] { int.class,
													utils.getEnum(utils.getNMSClass("EnumItemSlot"), "CHEST")
															.getClass(),
													utils.getNMSClass("ItemStack") },
											player.getEntityId(),
											utils.getEnum(utils.getNMSClass("EnumItemSlot"), "CHEST"),
											utils.invokeMethod(utils.getBukkitClass("inventory.CraftItemStack"),
													null, "asNMSCopy", new Class<?>[] { ItemStack.class },
													new Object[] { player.getInventory().getChestplate() }));

							leggings = utils
									.invokeConstructor(utils.getNMSClass("PacketPlayOutEntityEquipment"),
											new Class<?>[] { int.class,
													utils.getEnum(utils.getNMSClass("EnumItemSlot"), "LEGS")
															.getClass(),
													utils.getNMSClass("ItemStack") },
											player.getEntityId(),
											utils.getEnum(utils.getNMSClass("EnumItemSlot"), "LEGS"),
											utils.invokeMethod(utils.getBukkitClass("inventory.CraftItemStack"),
													null, "asNMSCopy", new Class<?>[] { ItemStack.class },
													new Object[] { player.getInventory().getLeggings() }));

							boots = utils
									.invokeConstructor(utils.getNMSClass("PacketPlayOutEntityEquipment"),
											new Class<?>[] { int.class,
													utils.getEnum(utils.getNMSClass("EnumItemSlot"), "FEET")
															.getClass(),
													utils.getNMSClass("ItemStack") },
											player.getEntityId(),
											utils.getEnum(utils.getNMSClass("EnumItemSlot"), "FEET"),
											utils.invokeMethod(utils.getBukkitClass("inventory.CraftItemStack"),
													null, "asNMSCopy", new Class<?>[] { ItemStack.class },
													new Object[] { player.getInventory().getBoots() }));

						Object slot = utils.invokeConstructor(utils.getNMSClass("PacketPlayOutHeldItemSlot"),
								new Class<?>[] { int.class }, player.getInventory().getHeldItemSlot());


						List<Player> players = player.getWorld().getPlayers();
						for (Player inWorld : players) {
							Object craftOnline = utils.getBukkitClass("entity.CraftPlayer").cast(inWorld);
							final Object craftHandle = utils.invokeMethod(craftOnline, "getHandle");
							Object playerCon = utils.getObject(craftHandle, "playerConnection");
							if (inWorld.equals(player)) {
								utils.sendPacket(playerCon, removeInfo);
								utils.sendPacket(playerCon, addInfo);
								utils.sendPacket(playerCon, respawn);
								Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), new Runnable() {
									@Override
									public void run() {
										try {
											utils.invokeMethod(craftHandle, "updateAbilities");
										} catch (Exception e) {
										}
									}

								});
								utils.sendPacket(playerCon, pos);
								utils.sendPacket(playerCon, slot);
								utils.invokeMethod(craftOnline, "updateScaledHealth");
								utils.invokeMethod(craftOnline, "updateInventory");
								utils.invokeMethod(craftHandle, "triggerHealthUpdate");
								continue;
							}
							utils.sendPacket(playerCon, removeEntity);
							utils.sendPacket(playerCon, removeInfo);
							if (inWorld.canSee(player)){
							utils.sendPacket(playerCon, addInfo);
							utils.sendPacket(playerCon, addNamed);
							if (hand == null) {
								utils.sendPacket(playerCon, mainhand);
								utils.sendPacket(playerCon, offhand);
							} else {
								utils.sendPacket(playerCon, hand);
							}
							utils.sendPacket(playerCon, helmet);
							utils.sendPacket(playerCon, chestplate);
							utils.sendPacket(playerCon, leggings);
							utils.sendPacket(playerCon, boots);
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			});
			return true;
		} catch(Exception e){
			e.printStackTrace();
		}
		return false;
	}
	
	/* -------------------
	 * Implementation part
	 * ------------------- */

	@Override public boolean isRendered(String name){
		return renderedSkins.containsKey(name);
	}
	
	@Override public void render(String name, String value, String signature, boolean override){
		if(!override && isRendered(name))
			CoreUtils.io.sendConsoleMessageWarningWithHighlight("Co� pr�bowa�o nadpisa� wyrenderowanego skina ", name, "!");
		try {
			renderedSkins.put(name, new SkinObject(value, signature));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override public void renderUsingPremiumNickname(String premiumNickname, boolean override){
		if(!override && isRendered(premiumNickname))
			return;
		try {
			String UUIDofPlayerPremium = CoreUtils.io.getUUIDofPlayerPremium(premiumNickname);
			if(UUIDofPlayerPremium == null)		return;
			String[] textFromMojangAPI = CoreUtils.io.getRawTextFromURL(
					new URL("https://sessionserver.mojang.com/session/minecraft/profile/" + UUIDofPlayerPremium + "?unsigned=false")).get(0).split("\"");
			String signature = textFromMojangAPI[13];
			String value = textFromMojangAPI[21];
			render(premiumNickname, value, signature, override);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public static class SkinObject {
		private final String value;			public String getValue()		{	return value;		}
		private final String signature;		public String getSignature()	{	return signature;	}
		private final Object wrapped;		public Object getSkin()			{	return wrapped;		}
		
		private SkinObject(String value, String signature) throws Exception {
			this.value = value;
			this.signature = signature;
			this.wrapped = ((DeveloperUtilsImplementation) CoreUtils.developer).invokeConstructor
					(Class.forName("com.mojang.authlib.properties.Property"), new Class<?>[] { String.class, String.class, String.class }, "textures", value, signature);
		}
	}
}
