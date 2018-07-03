package pl.mateam.marpg.api.regular.objects.items;

import org.bukkit.inventory.ItemStack;

public interface CommodoreItem {
	ItemStack craftItemStack();
	boolean hasMetadataKeyEffectively(String tagName);
	boolean getMetadataKeyEffectiveBoolean(String tagName);
	double getMetadataKeyEffectiveDouble(String tagName);
	int getMetadataKeyEffectiveInteger(String tagName);
	String getMetadataKeyEffectiveString(String tagName);
	void setMetadataKey(String tagName, boolean value);
	void setMetadataKey(String tagName, double value);
	void setMetadataKey(String tagName, int value);
	void setMetadataKey(String tagName, String value);
	void removeMetadataKey(String tagName);
	
	int getValue();
	void setCustomValue(int newValue);
}
