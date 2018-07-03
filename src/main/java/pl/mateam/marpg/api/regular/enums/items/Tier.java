package pl.mateam.marpg.api.regular.enums.items;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Material;

public enum Tier {
	DAMAGED(1, CommonItem.UPGRADING_GEM_1, Material.MAGENTA_GLAZED_TERRACOTTA, (byte) 0, ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC.toString() + 	"Przedmiot uszkodzony"),
	POORLY_MADE(2, CommonItem.UPGRADING_GEM_2, Material.MAGENTA_GLAZED_TERRACOTTA, (byte) 1, ChatColor.DARK_GRAY.toString() + ChatColor.ITALIC.toString() + "Przedmiot słabego wykonania"),
	CASUAL(3, CommonItem.UPGRADING_GEM_3, Material.MAGENTA_GLAZED_TERRACOTTA, (byte) 2, ChatColor.WHITE.toString() + ChatColor.ITALIC.toString() + 			"Zwyczajny przedmiot"),
	STRENGHENED(4, CommonItem.UPGRADING_GEM_4, Material.MAGENTA_GLAZED_TERRACOTTA, (byte) 3, ChatColor.WHITE.toString() + ChatColor.ITALIC.toString() + 	"Wzmocniony przedmiot"),
	ELITE(5, CommonItem.UPGRADING_GEM_5, Material.PINK_GLAZED_TERRACOTTA, (byte) 0, ChatColor.YELLOW.toString() + ChatColor.ITALIC.toString() + 			"Elitarny przedmiot"),
	EPIC(6, CommonItem.UPGRADING_GEM_6, Material.PINK_GLAZED_TERRACOTTA, (byte) 1, ChatColor.YELLOW.toString() + ChatColor.ITALIC.toString() + 				"Epicki przedmiot"),
	LEGENDARY(7, CommonItem.UPGRADING_GEM_7, Material.PINK_GLAZED_TERRACOTTA, (byte) 2, ChatColor.AQUA.toString() + ChatColor.ITALIC.toString() + 			"Legendarny przedmiot"),
	DIVINE(8, CommonItem.UPGRADING_GEM_8, Material.PINK_GLAZED_TERRACOTTA, (byte) 3, ChatColor.AQUA.toString() + ChatColor.ITALIC.toString() + 				"Boski przedmiot");
	
	public String getDisplayedName()		{	return displayedName;	}
	public Material getOreMaterial()		{	return oreMaterial;		}
	public byte getOreDurability()			{	return oreDurability;	}
	public CommonItem getUpgradingItem()	{	return upgradingItem;	}
	public byte getID()						{	return ID;				}

	private final String displayedName;
	private final Material oreMaterial;
	private final byte oreDurability;
	private final CommonItem upgradingItem;
	private final byte ID;
	private Tier(int tierID, CommonItem upgradingItem, Material material, byte durability, String displayedName) {
		this.displayedName = displayedName;
		this.oreMaterial = material;
		this.oreDurability = durability;
		this.upgradingItem = upgradingItem;
		this.ID = (byte) tierID;
	}
	
	public static Tier getUsingID(int id){
		for(Tier tier : Tier.values())
			if(tier.getID() == id)
				return tier;
		return null;
	}
}
