package pl.mateam.marpg.core.data.collections.embedded.players;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.modules.sub.server.ItemsManager;
import pl.mateam.marpg.api.regular.objects.items.CommodoreItem;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.NullAtRuntime;
import pl.mateam.marpg.core.internal.hardcoded.ItemSerializationSlot;
import pl.mateam.marpg.core.internal.hardcoded.database.players.CharacterClassHardcodedNames;
import pl.mateam.marpg.core.internal.hardcoded.database.players.CharacterClassHardcodedNames.LocationInfoHardcodedNames;
import pl.mateam.marpg.core.internal.interfaces.TransitoryElements;
import pl.mateam.marpg.core.objects.items.CommodoreItemImplementation;

public class CharacterClassInfo implements TransitoryElements {	
	@Transient private final long timeJoined = new Date().getTime();
		
	@NullAtRuntime	@Property(CharacterClassHardcodedNames.ITEMS)
	public Map<String, Object> items = new HashMap<>();		//String, Document. Should NOT be Integer, Document - after pull from database keys are stored as Strings anyway
	
	@NullAtRuntime @Property(CharacterClassHardcodedNames.LEVEL)			public int level = 1;
	@NullAtRuntime @Property(CharacterClassHardcodedNames.EXPERIENCE)		public float experience = 0;
	@NullAtRuntime @Property(CharacterClassHardcodedNames.HEALTH)			public double health = 20;
	@NullAtRuntime @Property(CharacterClassHardcodedNames.ENERGY)			public int energy = 20;
	
	@NullAtRuntime @Property(CharacterClassHardcodedNames.TIME_PLAYED)		public long timePlayed = 0;


	@Property(CharacterClassHardcodedNames.MONEY)			public int money = 0;
	
	@Embedded(CharacterClassHardcodedNames.LOCATION)		public LocationInfo location = new LocationInfo();

	public void writeCurrentFieldsState(Player player){
		this.level = player.getLevel();
		this.experience = player.getExp();
		this.health = player.getHealth();
		this.energy = player.getFoodLevel();
		
		items = new HashMap<>();
		PlayerInventory inv = player.getInventory();
		ItemsManager manager = Core.getServer().getItems();
		for(int i = 0; i < inv.getSize(); i++)
			serializeItem(inv.getItem(i), i, manager);
		
		serializeItem(inv.getHelmet(), ItemSerializationSlot.HELMET, manager);
		serializeItem(inv.getChestplate(), ItemSerializationSlot.CHESTPLATE, manager);
		serializeItem(inv.getLeggings(), ItemSerializationSlot.LEGGINGS, manager);
		serializeItem(inv.getBoots(), ItemSerializationSlot.BOOTS, manager);
		serializeItem(inv.getItemInOffHand(), ItemSerializationSlot.SHIELD, manager);
		
		serializeLocation(player.getLocation());
	
		timePlayed += TimeUnit.SECONDS.convert(new Date().getTime() - timeJoined, TimeUnit.MILLISECONDS);
	}
	
	private void serializeItem(ItemStack item, int slot, ItemsManager manager){
		if(item != null && item.getType() != Material.AIR) {
			CommodoreItem cLayer = manager.getCommodoreLayer(item);
			if(cLayer == null)
				return;
			CommodoreItemImplementation cExLayer = (CommodoreItemImplementation) cLayer;
			items.put(String.valueOf(slot), cExLayer.convertToPlainDocument());
		}
	}
	
	private void serializeLocation(Location location) {
		this.location.worldname = location.getWorld().getName();
		this.location.x 		= location.getX();
		this.location.y 		= location.getY();
		this.location.z 		= location.getZ();
		this.location.yaw 		= location.getYaw();
		this.location.pitch		= location.getPitch();
	}
	
	
	
	@Embedded public static class LocationInfo {
		@NullAtRuntime @Property(LocationInfoHardcodedNames.WORLD)			public String worldname;
		@NullAtRuntime @Property(LocationInfoHardcodedNames.X)				public double x = 0;
		@NullAtRuntime @Property(LocationInfoHardcodedNames.Y)				public double y = 0;
		@NullAtRuntime @Property(LocationInfoHardcodedNames.Z)				public double z = 0;
		@NullAtRuntime @Property(LocationInfoHardcodedNames.YAW)			public float yaw = 0;
		@NullAtRuntime @Property(LocationInfoHardcodedNames.PITCH)			public float pitch = 0;
	}
}