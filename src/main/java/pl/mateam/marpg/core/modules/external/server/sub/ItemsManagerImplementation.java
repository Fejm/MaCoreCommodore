package pl.mateam.marpg.core.modules.external.server.sub;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagDouble;
import net.minecraft.server.v1_12_R1.NBTTagInt;
import net.minecraft.server.v1_12_R1.NBTTagList;
import net.minecraft.server.v1_12_R1.NBTTagString;

import org.bson.Document;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.enums.items.CommonInterfaceElement;
import pl.mateam.marpg.api.regular.enums.items.CommonItem;
import pl.mateam.marpg.api.regular.enums.items.CommonSpecialItem;
import pl.mateam.marpg.api.regular.enums.items.Tier;
import pl.mateam.marpg.api.regular.modules.sub.server.ItemsManager;
import pl.mateam.marpg.api.regular.objects.items.CommodoreItem;
import pl.mateam.marpg.api.regular.objects.items.CommodoreSpecialItem;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.internal.enums.CompoundListElement;
import pl.mateam.marpg.core.internal.enums.NBT_Attribute;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo.ItemType;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo.ItemTypeGroup;
import pl.mateam.marpg.core.internal.utils.CommodoreCalculator;
import pl.mateam.marpg.core.objects.items.CommodoreItemImplementation;
import pl.mateam.marpg.core.objects.items.special.CommodoreArmorElementImplementation;
import pl.mateam.marpg.core.objects.items.special.CommodoreHandwearImplementation;
import pl.mateam.marpg.core.objects.items.special.CommodoreJewelleryElementImplementation;
import pl.mateam.marpg.core.objects.items.special.CommodoreShieldImplementation;

import com.mongodb.BasicDBObject;

public class ItemsManagerImplementation implements ItemsManager {
	public static class SpecialItemBuilder {
		private final net.minecraft.server.v1_12_R1.ItemStack[] baseNMSStacks;
		private final NBTTagCompound attributesCompound;	//Fast access

		public SpecialItemBuilder(ItemGroupInfoStorage itemsInfo){
			this.baseNMSStacks = itemsInfo.baseNMSStacks;
			this.attributesCompound = CompoundListElement.getElement(itemsInfo.baseCompound, CompoundListElement.ATTRIBUTES);
		}
		
		public void setAttributeValue(NBT_Attribute attribute, boolean value)	{	attributesCompound.setBoolean(attribute.getKey(), value);	}
		public void setAttributeValue(NBT_Attribute attribute, int value)		{	attributesCompound.setInt(attribute.getKey(), value);		}
		public void setAttributeValue(NBT_Attribute attribute, double value)	{	attributesCompound.setDouble(attribute.getKey(), value);	}
		public void setAttributeValue(NBT_Attribute attribute, String value)	{	attributesCompound.setString(attribute.getKey(), value);	}
		
		public void setAttackSpeed(double speed){
			for(int i = 0; i < baseNMSStacks.length; i++){
				NBTTagCompound compound = baseNMSStacks[i].getTag();
				double finalSpeed = speed - 4;
								
				NBTTagList list = new NBTTagList();
		        NBTTagCompound compoundSpeed = new NBTTagCompound();
		        compoundSpeed.set("AttributeName", new NBTTagString("generic.attackSpeed"));
		        compoundSpeed.set("Name", new NBTTagString("generic.attackSpeed"));
		        compoundSpeed.set("Amount", new NBTTagDouble(finalSpeed));
		        compoundSpeed.set("Operation", new NBTTagInt(0));
		        compoundSpeed.set("UUIDLeast", new NBTTagInt(894654));
		        compoundSpeed.set("UUIDMost", new NBTTagInt(2872));
		        compoundSpeed.set("Slot", new NBTTagString("mainhand"));
				list.add(compoundSpeed);
				compound.set("AttributeModifiers", list);
			}
			setAttributeValue(NBT_Attribute.DAMAGE_BASE, CommodoreCalculator.getOptimalDamage(speed));
		}
		
		public void setMining(int value){
			for(int i = 0; i < baseNMSStacks.length; i++){
				NBTTagCompound compound = baseNMSStacks[i].getTag();
				NBTTagList idsTag = new NBTTagList();
				for(Tier tier : Tier.values()){
					String line = "minecraft:" + tier.getOreMaterial().toString().toLowerCase();
					idsTag.add(new NBTTagString(line));
				}
				compound.set("CanDestroy", idsTag);
			}
			setAttributeValue(NBT_Attribute.PICKAXE_MINING_CHANCE, value / 100D);
		}
		
		public void setBonusable(int offensiveBonusChance, int defensiveBonusChance, int specialBonusChance){
			double sum = offensiveBonusChance + defensiveBonusChance + specialBonusChance;
			double off = offensiveBonusChance / sum;
			double def = defensiveBonusChance / sum;
			double spc = specialBonusChance / sum;
			setAttributeValue(NBT_Attribute.BONUSABLE, true);
			setAttributeValue(NBT_Attribute.OFFENSIVE_BONUS_CHANCE, off);
			setAttributeValue(NBT_Attribute.DEFENSIVE_BONUS_CHANCE, def);
			setAttributeValue(NBT_Attribute.SPECIAL_BONUS_CHANCE, spc);
		}
	}
	
	private static class ItemGroupInfoStorage {
		private final net.minecraft.server.v1_12_R1.ItemStack[] baseNMSStacks;
		private final NBTTagCompound baseCompound;
		
		private ItemGroupInfoStorage(NBTTagCompound baseCompound, net.minecraft.server.v1_12_R1.ItemStack... baseNMSStacks){
			this.baseCompound = baseCompound;
			this.baseNMSStacks = baseNMSStacks;
		}
	}
	
	private final Material INTERFACE_ELEMENT_BASE 	= Material.DIAMOND_AXE;
	private final Material UNSTACKABLE_ITEM_BASE 	= Material.DIAMOND_HOE;
	private final Material SPECIAL_ITEMS_BASE 		= Material.DIAMOND_SPADE;
	
	private static HashMap<String, ItemStack> interfaceElements = new HashMap<>();
	private static HashMap<Integer, ItemGroupInfoStorage> items = new HashMap<>();
	

	@Override public ItemStack getInterfaceElement(CommonInterfaceElement element) 								{	return getInterfaceElement(element.getExploratoryName());		}
	@Override public ItemStack getInterfaceElement(String exploratoryName)										{	return interfaceElements.get(exploratoryName).clone();			}
	@Override public ItemStack getInvisibleInterfaceElement(ItemStack itemToClone) {
		ItemStack clone = itemToClone.clone();
		clone.setDurability((short) 125);
		return clone;
	}
	
	@Override public CommodoreItem getItem(CommonItem item) 													{	return getItem(item.getID());									}
	@Override public CommodoreItem getItem(Integer ID) 															{	return getItemEffectively(ID, 0, null);							}

	@Override public CommodoreSpecialItem getSpecialItem(CommonSpecialItem specialItem) 						{	return getSpecialItem(specialItem.getID());						}
	@Override public CommodoreSpecialItem getSpecialItem(Integer ID) 											{	return getSpecialItem(ID, 0);									}

	@Override public CommodoreSpecialItem getSpecialItem(CommonSpecialItem specialItem, int level)				{	return getSpecialItem(specialItem.getID(), level);				}
	@Override public CommodoreSpecialItem getSpecialItem(Integer ID, int level) 								{	return getSpecialItem(ID, level, null);							}
	
	@Override public CommodoreSpecialItem getSpecialItem(CommonSpecialItem specialItem, int level, Tier tier) 	{	return getSpecialItem(specialItem.getID(), level, tier);		}
	@Override public CommodoreSpecialItem getSpecialItem(Integer ID, int level, Tier tier) {
		CommodoreItem cItem = getItemEffectively(ID, level/10, tier);
		if(cItem == null || !(cItem instanceof CommodoreSpecialItem))
			return null;
		CommodoreSpecialItem csItem = (CommodoreSpecialItem) cItem;
		if(tier != null)
			csItem.setTier(tier);
		else
			csItem.setRandomTier(0);
		return csItem;
	}
	
	private CommodoreItem getItemEffectively(Integer ID, int index, Tier tier){
		ItemGroupInfoStorage info = items.get(ID);
		if(info == null)
			return null;
		if(index < info.baseNMSStacks.length)
			return getCommodoreLayer(info.baseNMSStacks[index].cloneItemStack());
		else 
			return getCommodoreLayer(info.baseNMSStacks[info.baseNMSStacks.length-1].cloneItemStack());
	}
	
	@Override public CommodoreItem getCommodoreLayer(ItemStack item) {
		if(item == null || item.getType() == Material.AIR)
			return null;
		return getCommodoreLayer(CraftItemStack.asNMSCopy(item));
	}

	
	//BasicDBObject and Document are incompatible
	@Secret public ItemStack getFromBasicDBObject(BasicDBObject basicDBObject) {
		CommodoreItemImplementation item = null;
		String itemTypeKey = NBT_Attribute.COMMODOREID.getKey();
		ItemsManager manager = Core.getServer().getItems();
		int itemID = basicDBObject.getInt(itemTypeKey);
			
		Integer itemLevel = getItemLevel(basicDBObject);
		if(itemLevel == null)
			item  = (CommodoreItemImplementation) manager.getItem(itemID);
		else
			item = (CommodoreItemImplementation) manager.getSpecialItem(itemID, itemLevel);
		

		addKeys(item, basicDBObject, CompoundListElement.METADATA);
		addKeys(item, basicDBObject, CompoundListElement.ATTRIBUTES);
		addKeys(item, basicDBObject, CompoundListElement.BONUSES);
		
		ItemStack readyItemStack = item.craftItemStack();
		
		String itemAmountKey = NBT_Attribute.AMOUNT.getKey();
		if(basicDBObject.containsField(itemAmountKey))
			readyItemStack.setAmount(basicDBObject.getInt(itemAmountKey));

		return readyItemStack;
	}
	
	private Integer getItemLevel(BasicDBObject BasicDBObject){
		Object nested = BasicDBObject.get(CompoundListElement.ATTRIBUTES.getName());
		if(nested != null){
			BasicDBObject = (BasicDBObject) nested;
			nested = BasicDBObject.get(NBT_Attribute.REQUIRED_LEVEL.getKey());
			if(nested != null){
				BasicDBObject = (BasicDBObject) nested;
				return BasicDBObject.getInt("wartosc");
			}
		}
		return null;
	}
		
	private void addKeys(CommodoreItemImplementation item, BasicDBObject document, CompoundListElement list){
		Object attributes = document.get(list.getName());
		if(attributes != null)
			item.restoreMetaKeys(list, (BasicDBObject) attributes);
	}
	
	//BasicDBObject and Document are incompatible
	@Secret public ItemStack getFromDocument(Document document) {
		CommodoreItemImplementation item = null;
		String itemTypeKey = NBT_Attribute.COMMODOREID.getKey();
		ItemsManager manager = Core.getServer().getItems();
		int itemID = document.getInteger(itemTypeKey);
		Integer itemLevel = getItemLevel(document);
		if(itemLevel == null)
			item  = (CommodoreItemImplementation) manager.getItem(itemID);
		else
			item = (CommodoreItemImplementation) manager.getSpecialItem(itemID, itemLevel);
		

		addKeys(item, document, CompoundListElement.METADATA);
		addKeys(item, document, CompoundListElement.ATTRIBUTES);
		addKeys(item, document, CompoundListElement.BONUSES);
		
		return item.craftItemStack();
	}
	
	private Integer getItemLevel(Document document){
		Object nested = document.get(CompoundListElement.ATTRIBUTES.getName());
		if(nested != null){
			document = (Document) nested;
			nested = document.get(NBT_Attribute.REQUIRED_LEVEL.getKey());
			if(nested != null){
				document = (Document) nested;
				return document.getInteger("wartosc");
			}
		}
		return null;
	}
		
	private void addKeys(CommodoreItemImplementation item, Document document, CompoundListElement list){
		Object attributes = document.get(list.getName());
		if(attributes != null)
			item.restoreMetaKeys(list, (Document) attributes);
	}
	
	
	
	

	@Secret public CommodoreItem getCommodoreLayer(net.minecraft.server.v1_12_R1.ItemStack nmsStack) {
		if(nmsStack == null || !nmsStack.hasTag() || !nmsStack.getTag().hasKey(NBT_Attribute.COMMODOREID.getKey()))
			return null;
		int ID = nmsStack.getTag().getInt(NBT_Attribute.COMMODOREID.getKey());
		ItemGroupInfoStorage info = items.get(ID);
		NBTTagCompound attributes = CompoundListElement.getElement(info.baseCompound, CompoundListElement.ATTRIBUTES);
		
		if(!attributes.hasKey(NBT_Attribute.TYPE.getKey()))
			return new CommodoreItemImplementation(nmsStack);
		
		String type = attributes.getString(NBT_Attribute.TYPE.getKey());
				
		ItemTypeGroup group = new ItemTypeInfo(type).assignToGroup();
		switch(group){
			case HANDWEAR:		return new CommodoreHandwearImplementation(nmsStack);
			case ARMOR:			return new CommodoreArmorElementImplementation(nmsStack);
			case SHIELD:		return new CommodoreShieldImplementation(nmsStack);
			case JEWELLERY:		return new CommodoreJewelleryElementImplementation(nmsStack);
			default:			return null;
		}
	}
	
	@Secret public Object performOperation(Integer itemID, BiFunction<net.minecraft.server.v1_12_R1.ItemStack[], NBTTagCompound, Object> action)	{
		ItemGroupInfoStorage info = items.get(itemID);
		return action.apply(info.baseNMSStacks, info.baseCompound);
	}
	
	@Secret public void performOnAll(BiConsumer<net.minecraft.server.v1_12_R1.ItemStack[], NBTTagCompound> action)	{
		items.values().forEach(groupInfo -> action.accept(groupInfo.baseNMSStacks, groupInfo.baseCompound));
	}
	
	@Secret public void registerInterfaceElement(String exploratoryName, short iconID, String itemName, List<String> itemLore) {
		ItemStack item = prepareItemStack(INTERFACE_ELEMENT_BASE, iconID, itemName, itemLore);
		interfaceElements.put(exploratoryName, item);
	}
	
	@Secret public void registerUnstackableItem(Integer itemID, short durability, int value, String itemName, List<String> itemLore, HashMap<String, Object> itemMetadata) {
		net.minecraft.server.v1_12_R1.ItemStack nmsStack = createNMSStack(itemID, UNSTACKABLE_ITEM_BASE, durability, itemName, itemLore);
		NBTTagCompound baseCompound = createDefaultCompound(value, itemMetadata);
		items.put(itemID, new ItemGroupInfoStorage(baseCompound, nmsStack));
	}
	
	@SuppressWarnings("deprecation")
	@Secret public void registerStackableItem(Integer itemID, int materialID, short iconID, int value, String itemName, List<String> itemLore, HashMap<String, Object> itemMetadata) {
		net.minecraft.server.v1_12_R1.ItemStack nmsStack = createNMSStack(itemID, Material.getMaterial(materialID), iconID, itemName, itemLore);
		NBTTagCompound baseCompound = createDefaultCompound(value, itemMetadata);
		items.put(itemID, new ItemGroupInfoStorage(baseCompound, nmsStack));
	}
	
	@Secret public SpecialItemBuilder registerSingleSpecialItem(Integer itemID, String itemType, short iconID, int value, String itemName, List<String> itemLore, HashMap<String, Object> itemMetadata, boolean regular, int blockedClasses, int level){
		net.minecraft.server.v1_12_R1.ItemStack nmsStack = getNMSStackDependingOnType(itemID, itemType, iconID, itemName, itemLore);
		NBTTagCompound attributeCompound = CompoundListElement.getElement(nmsStack.getTag(), CompoundListElement.ATTRIBUTES);
		if(level > 0)
			attributeCompound.setInt(NBT_Attribute.REQUIRED_LEVEL.getKey(), level);
		if(regular)
			attributeCompound.setInt(NBT_Attribute.UPGRADEMENT.getKey(), 0);
		return registerSpecial(itemID, itemType, value, itemMetadata, regular, blockedClasses, nmsStack);
	}
	
	@Secret public SpecialItemBuilder registerSpecialItemsGroup(Integer itemsID, String itemsType, short iconsStart, int valueBase, List<String> itemsNames, List<String> itemsLore, HashMap<String, Object> itemsMetadata, boolean regular, int blockedClasses){
		net.minecraft.server.v1_12_R1.ItemStack[] stacks = new net.minecraft.server.v1_12_R1.ItemStack[10];
		int startLevel = new ItemTypeInfo(itemsType).getExactType().getStartLevel();
		for(int i = 0; i < 10; i++){
			net.minecraft.server.v1_12_R1.ItemStack stack = getNMSStackDependingOnType(itemsID, itemsType, (short) (iconsStart + i), itemsNames.get(i), itemsLore);
			NBTTagCompound attributeCompound = CompoundListElement.getElement(stack.getTag(), CompoundListElement.ATTRIBUTES);
			attributeCompound.setInt(NBT_Attribute.REQUIRED_LEVEL.getKey(), startLevel + i * 10);
			if(regular)
				attributeCompound.setInt(NBT_Attribute.UPGRADEMENT.getKey(), 0);
			stacks[i] = stack;
		}
		return registerSpecial(itemsID, itemsType, valueBase, itemsMetadata, regular, blockedClasses, stacks);
	}
	
	private net.minecraft.server.v1_12_R1.ItemStack getNMSStackDependingOnType(Integer ID, String typeString, short durability, String itemName, List<String> itemLore) {
		ItemType type = new ItemTypeInfo(typeString).getExactType();
		switch(type){
			case HELMET:		return createNMSStack(ID, Material.LEATHER_HELMET, (short) 0, itemName, itemLore);
			case CHESTPLATE:	return createNMSStack(ID, Material.LEATHER_CHESTPLATE, (short) 0, itemName, itemLore);
			case LEGGINGS:		return createNMSStack(ID, Material.LEATHER_LEGGINGS, (short) 0, itemName, itemLore);
			case BOOTS:			return createNMSStack(ID, Material.LEATHER_BOOTS, (short) 0, itemName, itemLore);
			case SHIELD:		return createNMSStack(ID, Material.SHIELD, (short) 0, itemName, itemLore);
			default:			return createNMSStack(ID, SPECIAL_ITEMS_BASE, durability, itemName, itemLore);
		}
	}
	
	private SpecialItemBuilder registerSpecial(Integer itemsID, String itemsType, int valueBase, HashMap<String, Object> itemsMetadata, boolean regular, int blockedClasses, net.minecraft.server.v1_12_R1.ItemStack... stacks){
		NBTTagCompound baseCompound = createDefaultCompound(valueBase, itemsMetadata);
		
		NBTTagCompound attributesCompound = CompoundListElement.getElement(baseCompound, CompoundListElement.ATTRIBUTES);
		attributesCompound.setString(NBT_Attribute.TYPE.getKey(), itemsType);
		attributesCompound.setBoolean(NBT_Attribute.REGULAR.getKey(), regular);
		attributesCompound.setInt(NBT_Attribute.BLOCKED_CLASSES.getKey(), blockedClasses);

		ItemGroupInfoStorage groupInfo = new ItemGroupInfoStorage(baseCompound, stacks);
		items.put(itemsID, groupInfo);
		return new SpecialItemBuilder(groupInfo);
	}
	
	private net.minecraft.server.v1_12_R1.ItemStack createNMSStack(Integer ID, Material base, short durability, String itemName, List<String> itemLore){
		ItemStack item = prepareItemStack(base, durability, itemName, itemLore);
		net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = nmsStack.hasTag()? nmsStack.getTag() : new NBTTagCompound();
		compound.set("MaRPG", new NBTTagList());		
		compound.setInt(NBT_Attribute.COMMODOREID.getKey(), ID);
		nmsStack.setTag(compound);
		
		return nmsStack;
	}
	
	private NBTTagCompound createDefaultCompound(int itemValue, Map<String, Object> itemMetadata){
		NBTTagCompound defaultCompound = new NBTTagCompound();
		defaultCompound.set("MaRPG", new NBTTagList());
		NBTTagCompound defaultAttributesCompound = CompoundListElement.getElement(defaultCompound, CompoundListElement.ATTRIBUTES);
		
		defaultAttributesCompound.setInt(NBT_Attribute.VALUE.getKey(), itemValue);
		
		NBTTagCompound metadataCompound = CompoundListElement.getElement(defaultCompound, CompoundListElement.METADATA);
		
		itemMetadata.forEach((key, mapValue) -> {
			if(mapValue instanceof Boolean)
				metadataCompound.setBoolean(key, (boolean) mapValue);
			else if(mapValue instanceof Integer)
				metadataCompound.setInt(key, (int) mapValue);
			else if(mapValue instanceof Double)
				metadataCompound.setDouble(key, (double) mapValue);
			else
				metadataCompound.setString(key, mapValue.toString());
		});
		
		return defaultCompound;
	}
	
	private ItemStack prepareItemStack(Material material, short durability, String itemName, List<String> itemLore){
		ItemStack item = new ItemStack(material, 1, durability);
		ItemMeta meta = item.getItemMeta();
		meta.setDisplayName(itemName);
		meta.setLore(itemLore);
		item.setItemMeta(meta);
		net.minecraft.server.v1_12_R1.ItemStack nmsStack = CraftItemStack.asNMSCopy(item);
		NBTTagCompound compound = nmsStack.hasTag()? nmsStack.getTag() : new NBTTagCompound();
		removeRedundantTags(compound);
		nmsStack.setTag(compound);
		item = CraftItemStack.asBukkitCopy(nmsStack);
		return item;
	}
	
	private NBTTagCompound removeRedundantTags(NBTTagCompound compound) {
		compound.set("HideFlags", new NBTTagInt(63));
		compound.setBoolean("Unbreakable", true);
		if(compound.hasKey("AttributeModifiers"))
			compound.set("AttributeModifiers", new NBTTagList());
		return compound;
	}
}