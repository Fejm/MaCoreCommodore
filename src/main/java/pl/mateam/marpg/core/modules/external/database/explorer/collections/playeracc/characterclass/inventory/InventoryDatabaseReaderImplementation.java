package pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.characterclass.inventory;

import java.util.Map;
import java.util.function.BiConsumer;

import org.bson.Document;
import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.characterclass.inventory.InventoryDatabaseReader;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.internal.hardcoded.ItemSerializationSlot;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseReader;
import pl.mateam.marpg.core.modules.external.server.sub.ItemsManagerImplementation;

import com.mongodb.BasicDBObject;

public class InventoryDatabaseReaderImplementation extends AbstractDatabaseReader implements InventoryDatabaseReader {
	public InventoryDatabaseReaderImplementation(Object document) 	{	super(document);	}

	@Override public ItemStack restoreHelmet() 		{	return restoreHelmet(document);			}
	@Override public ItemStack restoreChestplate() 	{	return restoreChestplate(document);		}
	@Override public ItemStack restoreLeggings() 	{	return restoreLeggings(document);		}
	@Override public ItemStack restoreBoots() 		{	return restoreBoots(document);			}
	@Override public ItemStack restoreShield() 		{	return restoreShield(document);			}

	@Override public ItemStack restoreItemFromSlot(int slot) {
		return restoreItemFromSlot(document, slot);
	}

	@Override public void takeActionOnInventory(BiConsumer<Integer, ItemStack> actionToTake, boolean withoutEquippedItems) {
		ItemsManagerImplementation manager = ((ItemsManagerImplementation) Core.getServer().getItems());
		document.forEach((key, object) -> {
			Integer slot = Integer.valueOf(key);
			if((!withoutEquippedItems || slot < 36) && object != null)
				actionToTake.accept(slot, manager.getFromDocument((Document) object));
		});
	}
	
	
	
	
	
	//Used in lobby NPC's inventory loading module
	@Secret public static ItemStack restoreItemFromSlot(Map<String, Object> map, int slot) {
		Object retrieved = map.get(String.valueOf(slot));
		if(retrieved == null)
			return null;
		if(retrieved instanceof Document)
			return ((ItemsManagerImplementation) Core.getServer().getItems()).getFromDocument((Document) retrieved);
		else if(retrieved instanceof BasicDBObject)
			return ((ItemsManagerImplementation) Core.getServer().getItems()).getFromBasicDBObject((BasicDBObject) retrieved);
		else return null;
	}
	
	@Secret public static ItemStack restoreHelmet(Map<String, Object> map) 		{	return restoreItemFromSlot(map, ItemSerializationSlot.HELMET);		}
	@Secret public static ItemStack restoreChestplate(Map<String, Object> map) 	{	return restoreItemFromSlot(map, ItemSerializationSlot.CHESTPLATE);	}
	@Secret public static ItemStack restoreLeggings(Map<String, Object> map) 	{	return restoreItemFromSlot(map, ItemSerializationSlot.LEGGINGS);	}
	@Secret public static ItemStack restoreBoots(Map<String, Object> map) 		{	return restoreItemFromSlot(map, ItemSerializationSlot.BOOTS);		}
	@Secret public static ItemStack restoreShield(Map<String, Object> map) 		{	return restoreItemFromSlot(map, ItemSerializationSlot.SHIELD);		}
}
