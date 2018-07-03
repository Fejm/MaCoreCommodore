package pl.mateam.marpg.api.regular.modules.sub.server;

import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.regular.enums.items.CommonInterfaceElement;
import pl.mateam.marpg.api.regular.enums.items.CommonItem;
import pl.mateam.marpg.api.regular.enums.items.CommonSpecialItem;
import pl.mateam.marpg.api.regular.enums.items.Tier;
import pl.mateam.marpg.api.regular.objects.items.CommodoreItem;
import pl.mateam.marpg.api.regular.objects.items.CommodoreSpecialItem;

public interface ItemsManager {
	ItemStack getInterfaceElement(String exploratoryName);
	ItemStack getInterfaceElement(CommonInterfaceElement element);
	ItemStack getInvisibleInterfaceElement(ItemStack itemToClone);
	
	CommodoreItem getItem(Integer ID);
	CommodoreItem getItem(CommonItem item);
	
	CommodoreSpecialItem getSpecialItem(Integer ID);
	CommodoreSpecialItem getSpecialItem(CommonSpecialItem specialItem);
	CommodoreSpecialItem getSpecialItem(Integer ID, int level);
	CommodoreSpecialItem getSpecialItem(CommonSpecialItem specialItem, int level);
	CommodoreSpecialItem getSpecialItem(Integer ID, int level, Tier tier);
	CommodoreSpecialItem getSpecialItem(CommonSpecialItem specialItem, int level, Tier tier);
	
	CommodoreItem getCommodoreLayer(ItemStack item);
}