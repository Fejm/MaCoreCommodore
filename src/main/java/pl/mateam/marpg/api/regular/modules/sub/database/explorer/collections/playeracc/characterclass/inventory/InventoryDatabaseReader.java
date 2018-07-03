package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.characterclass.inventory;

import java.util.function.BiConsumer;

import org.bukkit.inventory.ItemStack;

public interface InventoryDatabaseReader {
	void takeActionOnInventory(BiConsumer<Integer, ItemStack> actionToTake, boolean withoutEquippedItems);	

	ItemStack restoreHelmet();
	ItemStack restoreChestplate();
	ItemStack restoreLeggings();
	ItemStack restoreBoots();
	ItemStack restoreShield();
	ItemStack restoreItemFromSlot(int slot);
}