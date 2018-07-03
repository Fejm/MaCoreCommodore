package pl.mateam.marpg.core.internal.helpers;

import java.util.Arrays;

import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.enums.items.CommonInterfaceElement;


public class ItemTypeInfo {
	private final ItemSlotType slotType;	public ItemSlotType getCorrespondingSlotType()	{	return slotType;	}
	private final ItemType itemType;		public ItemType getExactType()					{	return itemType;	}
	private final String customName;
	
	public ItemTypeInfo(String itemTypeString) {
		switch(itemTypeString){
			case "HELM":		this.itemType = ItemType.HELMET;		this.slotType = ItemSlotType.HELMET;		break;
			case "NAPIERSNIK":	this.itemType = ItemType.CHESTPLATE;	this.slotType = ItemSlotType.CHESTPLATE;	break;
			case "NOGAWICE":	this.itemType = ItemType.LEGGINGS;		this.slotType = ItemSlotType.LEGGINGS;		break;
			case "BUTY":		this.itemType = ItemType.BOOTS;			this.slotType = ItemSlotType.BOOTS;			break;
			case "TARCZA":		this.itemType = ItemType.SHIELD;		this.slotType = ItemSlotType.SHIELD;		break;
			case "NASZYJNIK":	this.itemType = ItemType.NECKLACE;		this.slotType = ItemSlotType.NECKLACE;		break;
			case "PIERSCIEN":	this.itemType = ItemType.RING;			this.slotType = ItemSlotType.RING;			break;
			case "BRAK":		this.itemType = ItemType.NONE;			this.slotType = ItemSlotType.HANDWEAR;		break;
			default:
				this.itemType = ItemType.CUSTOM;
				this.slotType = ItemSlotType.HANDWEAR;
				this.customName = itemTypeString;
				return;
		}
		this.customName = null;
	}
	
	public String getDisplayedName(){
		switch(itemType){
			case NONE:			return null;
			case CUSTOM:		return customName;
			default:			return itemType.displayedName;
		}
	}
	public ItemTypeGroup assignToGroup()	{	return itemType.associatedGroup;	}


	
	
	public static enum ItemTypeGroup {
		HANDWEAR,
		ARMOR,
		SHIELD,
		JEWELLERY;
	}
	
	public static enum ItemType {
		NONE(null, ItemTypeGroup.HANDWEAR, 1),
		CUSTOM(null, ItemTypeGroup.HANDWEAR, 1),
		HELMET("Hełm", ItemTypeGroup.ARMOR, 4),
		CHESTPLATE("Napierśnik", ItemTypeGroup.ARMOR, 2),
		LEGGINGS("Nogawice", ItemTypeGroup.ARMOR, 3),
		BOOTS("Buty", ItemTypeGroup.ARMOR, 5),
		SHIELD("Tarcza", ItemTypeGroup.SHIELD, 6),
		NECKLACE("Naszyjnik", ItemTypeGroup.JEWELLERY, 7),
		RING("Pierścień", ItemTypeGroup.JEWELLERY, 8);
		
		private final String displayedName;
		private final ItemTypeGroup associatedGroup;
		private final int startLevel;
		
		private ItemType(String displayedName, ItemTypeGroup associatedGroup, int startLevel){
			this.displayedName = displayedName;
			this.associatedGroup = associatedGroup;
			this.startLevel = startLevel;
		}
		
		public int getStartLevel()	{	return startLevel;	}
	}
	
	public static enum ItemSlotType {
		HANDWEAR(CommonInterfaceElement.EMPTY_WEAPON_SLOT, 8), NECKLACE(CommonInterfaceElement.EMPTY_NECKLACE_SLOT, 9), RING(CommonInterfaceElement.EMPTY_RING_SLOT, 10, 11),
		BOOTS(null, 36), LEGGINGS(null, 37), CHESTPLATE(null, 38), HELMET(null, 39), SHIELD(null, 40);
		
		public static ItemSlotType getUsingNumber(int rawSlotFromInventoryClickEvent){
			for(ItemSlotType slotType : ItemSlotType.values())
				for(int allowedSlot : slotType.allowedSlots)
					if(allowedSlot == rawSlotFromInventoryClickEvent)
						return slotType;
			return null;
		}
		
		private final int[] allowedSlots;
		private final CommonInterfaceElement emptySlotElement;
		private ItemSlotType(CommonInterfaceElement emptySlotElement, int... allowedSlots) {
			this.emptySlotElement = emptySlotElement;
			this.allowedSlots = allowedSlots;
		}
		
		public int[] getAllowedSlots()	{	return Arrays.copyOf(allowedSlots, allowedSlots.length);	}
		public ItemStack getEmptySlotElement() {
			return emptySlotElement == null? null : Core.getServer().getItems().getInterfaceElement(emptySlotElement);
		}
	}
}
