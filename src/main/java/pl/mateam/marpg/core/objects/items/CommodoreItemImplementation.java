package pl.mateam.marpg.core.objects.items;

import java.util.Iterator;
import java.util.Map.Entry;

import net.minecraft.server.v1_12_R1.NBTTagCompound;

import org.bson.Document;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.items.CommodoreItem;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.internal.enums.CompoundListElement;
import pl.mateam.marpg.core.internal.enums.NBT_Attribute;
import pl.mateam.marpg.core.internal.interfaces.ConvertableToDocument;
import pl.mateam.marpg.core.internal.utils.Parsers;
import pl.mateam.marpg.core.modules.external.server.sub.ItemsManagerImplementation;

import com.mongodb.BasicDBObject;


public class CommodoreItemImplementation implements CommodoreItem, ConvertableToDocument {
	private final net.minecraft.server.v1_12_R1.ItemStack nmsStack;
	
	public CommodoreItemImplementation(net.minecraft.server.v1_12_R1.ItemStack nmsStack) {
		this.nmsStack = nmsStack;
	}
	
	@Override public ItemStack craftItemStack() 								{	return CraftItemStack.asBukkitCopy(nmsStack);										}

	@Override public int getValue() 											{	return getKeyEffectiveInteger(NBT_Attribute.VALUE.getKey());						}
	@Override public void setCustomValue(int newValue) 							{	setKey(NBT_Attribute.VALUE.getKey(), newValue);										}
	
	
	@Override public boolean hasMetadataKeyEffectively(String tagName) 			{	return containsGenericKeyEffectively(CompoundListElement.METADATA, tagName);		}
	@Override public boolean getMetadataKeyEffectiveBoolean(String tagName) 	{	return getGenericKeyEffectiveBoolean(CompoundListElement.METADATA, tagName);		}
	@Override public double getMetadataKeyEffectiveDouble(String tagName)		{	return getGenericKeyEffectiveDouble(CompoundListElement.METADATA, tagName);			}
	@Override public int getMetadataKeyEffectiveInteger(String tagName) 		{	return getGenericKeyEffectiveInteger(CompoundListElement.METADATA, tagName);		}
	@Override public String getMetadataKeyEffectiveString(String tagName) 		{	return getGenericKeyEffectiveString(CompoundListElement.METADATA, tagName);			}
	
	@Override public void setMetadataKey(String tagName, boolean value)			{	setGenericKey(CompoundListElement.METADATA, tagName, value);						}
	@Override public void setMetadataKey(String tagName, double value) 			{	setGenericKey(CompoundListElement.METADATA, tagName, value);						}
	@Override public void setMetadataKey(String tagName, int value) 			{	setGenericKey(CompoundListElement.METADATA, tagName, value);						}
	@Override public void setMetadataKey(String tagName, String value) 			{	setGenericKey(CompoundListElement.METADATA, tagName, value);						}
	
	@Override public void removeMetadataKey(String tagName) 					{	removeGenericKey(CompoundListElement.METADATA, tagName);							}
	
	
	//BasicDBObject and Document are incompatible
	@Secret public void restoreMetaKeys(CompoundListElement listType, BasicDBObject describingDocument){
		Iterator<Entry<String, Object>> iterator = describingDocument.entrySet().iterator();
		NBTTagCompound requestedList = getList(listType);
		while(iterator.hasNext()){
			Entry<String, Object> entry = iterator.next();
			String metaKeyName = entry.getKey();
			BasicDBObject metaKeyInfo = (BasicDBObject) entry.getValue();
			
			int type = metaKeyInfo.getInt("typ");
			switch(type){
				//NBTTag's types magic values
				case 1:		requestedList.setByte(metaKeyName, (byte) metaKeyInfo.getInt("wartosc"));		break;
				case 2:		requestedList.setShort(metaKeyName, (short) metaKeyInfo.getInt("wartosc"));		break;
				case 3:		requestedList.setInt(metaKeyName, metaKeyInfo.getInt("wartosc"));				break;
				case 4:		requestedList.setLong(metaKeyName, metaKeyInfo.getLong("wartosc"));				break;
				case 5:		requestedList.setFloat(metaKeyName, (float) metaKeyInfo.getDouble("wartosc"));	break;
				case 6:		requestedList.setDouble(metaKeyName, metaKeyInfo.getDouble("wartosc"));			break;
				case 8:		requestedList.setString(metaKeyName, metaKeyInfo.getString("wartosc"));			break;
			}
		}
	}
	
	@Secret public void restoreMetaKeys(CompoundListElement listType, Document describingDocument){
		Iterator<Entry<String, Object>> iterator = describingDocument.entrySet().iterator();
		NBTTagCompound requestedList = getList(listType);
		while(iterator.hasNext()){
			Entry<String, Object> entry = iterator.next();
			String metaKeyName = entry.getKey();
			Document metaKeyInfo = (Document) entry.getValue();
			
			int type = metaKeyInfo.getInteger("typ");
			switch(type){
				//NBTTag's types magic values
				case 1:		requestedList.setByte(metaKeyName, (byte) (int) metaKeyInfo.getInteger("wartosc"));			break;
				case 2:		requestedList.setShort(metaKeyName, (short) (int) metaKeyInfo.getInteger("wartosc"));		break;
				case 3:		requestedList.setInt(metaKeyName, metaKeyInfo.getInteger("wartosc"));						break;
				case 4:		requestedList.setLong(metaKeyName, metaKeyInfo.getLong("wartosc"));							break;
				case 5:		requestedList.setFloat(metaKeyName, (float) (double) metaKeyInfo.getDouble("wartosc"));		break;
				case 6:		requestedList.setDouble(metaKeyName, metaKeyInfo.getDouble("wartosc"));						break;
				case 8:		requestedList.setString(metaKeyName, metaKeyInfo.getString("wartosc"));						break;
			}
		}
	}

	@Override	@Secret
	public Document convertToPlainDocument() {
		Document result = new Document();
		NBTTagCompound tag = getNBTTag();
		
		String typeKey = NBT_Attribute.COMMODOREID.getKey();
		
		result.put(typeKey, tag.getInt(typeKey));
		
		if(nmsStack.getCount() > 1) {
			String amountKey = NBT_Attribute.AMOUNT.getKey();
			result.put(amountKey, nmsStack.getCount());
		}
		
		putSubDocument(result, tag, CompoundListElement.ATTRIBUTES);
		putSubDocument(result, tag, CompoundListElement.METADATA);
		return result;
	}
	
	protected void putSubDocument(Document mainDocument, NBTTagCompound mainCompound, CompoundListElement listType){
		Document result = new Document();
		NBTTagCompound list = CompoundListElement.getElement(mainCompound, listType);
		for(String key : list.c()){
			Document keyDocument = new Document();
			keyDocument.append("typ", list.get(key).getTypeId());
			keyDocument.append("wartosc", Parsers.getTagValue(list, key));
			result.put(key, keyDocument);
		}
		mainDocument.put(listType.getName(), result);
	}
	
	protected boolean containsEffectivelyKey(String tagName)					{	return containsGenericKeyEffectively(CompoundListElement.ATTRIBUTES, tagName);		}
	protected boolean getKeyEffectiveBoolean(String tagName)					{	return getGenericKeyEffectiveBoolean(CompoundListElement.ATTRIBUTES, tagName);		}
	protected double getKeyEffectiveDouble(String tagName) 						{	return getGenericKeyEffectiveDouble(CompoundListElement.ATTRIBUTES, tagName);		}
	protected int getKeyEffectiveInteger(String tagName) 						{	return getGenericKeyEffectiveInteger(CompoundListElement.ATTRIBUTES, tagName);		}
	protected String getKeyEffectiveString(String tagName) 						{	return getGenericKeyEffectiveString(CompoundListElement.ATTRIBUTES, tagName);		}
	
	protected void setKey(String tagName, boolean value)						{	setGenericKey(CompoundListElement.ATTRIBUTES, tagName, value);						}
	protected void setKey(String tagName, double value) 						{	setGenericKey(CompoundListElement.ATTRIBUTES, tagName, value);						}
	protected void setKey(String tagName, int value) 							{	setGenericKey(CompoundListElement.ATTRIBUTES, tagName, value);						}
	protected void setKey(String tagName, String value) 						{	setGenericKey(CompoundListElement.ATTRIBUTES, tagName, value);						}
	
	protected void removeKey(String tagName)									{	removeGenericKey(CompoundListElement.ATTRIBUTES, tagName);							}
	
	protected NBTTagCompound getNBTTag()										{	return nmsStack.getTag();															}

	private boolean containsGenericKeyEffectively(CompoundListElement list, String tagName){
		NBTTagCompound listCompound = getList(list);
		if(listCompound.hasKey(tagName))
			return true;
		else {
			int itemID = getItemID(getNBTTag());
			NBTTagCompound foreignCompound = (NBTTagCompound) ((ItemsManagerImplementation) Core.getServer().getItems()).performOperation(itemID, (array, compound) -> { return compound; });
			NBTTagCompound foreignListCompound = CompoundListElement.getElement(foreignCompound, list);
			return foreignListCompound.hasKey(tagName);
		}
	}
	
	private boolean getGenericKeyEffectiveBoolean(CompoundListElement list, String tagName) {
		NBTTagCompound listCompound = getList(list);
		if(listCompound.hasKey(tagName))
			return listCompound.getBoolean(tagName);
		else {
			int itemID = getItemID(getNBTTag());
			NBTTagCompound foreignCompound = (NBTTagCompound) ((ItemsManagerImplementation) Core.getServer().getItems()).performOperation(itemID, (array, compound) -> { return compound; });
			NBTTagCompound foreignListCompound = CompoundListElement.getElement(foreignCompound, list);
			return foreignListCompound.getBoolean(tagName);
		}
	}

	private double getGenericKeyEffectiveDouble(CompoundListElement list, String tagName) {
		NBTTagCompound listCompound = getList(list);
		if(listCompound.hasKey(tagName))
			return listCompound.getDouble(tagName);
		else {
			int itemID = getItemID(getNBTTag());
			NBTTagCompound foreignCompound = (NBTTagCompound) ((ItemsManagerImplementation) Core.getServer().getItems()).performOperation(itemID, (array, compound) -> { return compound; });
			NBTTagCompound foreignListCompound = CompoundListElement.getElement(foreignCompound, list);
			return foreignListCompound.getDouble(tagName);
		}
	}

	private int getGenericKeyEffectiveInteger(CompoundListElement list, String tagName) {
		NBTTagCompound listCompound = getList(list);
		if(listCompound.hasKey(tagName))
			return listCompound.getInt(tagName);
		else {
			int itemID = getItemID(getNBTTag());
			NBTTagCompound foreignCompound = (NBTTagCompound) ((ItemsManagerImplementation) Core.getServer().getItems()).performOperation(itemID, (array, compound) -> { return compound; });
			NBTTagCompound foreignListCompound = CompoundListElement.getElement(foreignCompound, list);
			return foreignListCompound.getInt(tagName);
		}
	}

	private String getGenericKeyEffectiveString(CompoundListElement list, String tagName) {
		NBTTagCompound listCompound = getList(list);
		if(listCompound.hasKey(tagName))
			return listCompound.getString(tagName);
		else {
			int itemID = getItemID(getNBTTag());
			NBTTagCompound foreignCompound = (NBTTagCompound) ((ItemsManagerImplementation) Core.getServer().getItems()).performOperation(itemID, (array, compound) -> { return compound; });
			NBTTagCompound foreignListCompound = CompoundListElement.getElement(foreignCompound, list);
			return foreignListCompound.getString(tagName);
		}
	}
	
	private int getItemID(NBTTagCompound compound){
		return compound.getInt(NBT_Attribute.COMMODOREID.getKey());
	}

	private void setGenericKey(CompoundListElement list, String tagName, boolean value) {	getList(list).setBoolean(tagName, value);	}
	private void setGenericKey(CompoundListElement list, String tagName, double value) 	{	getList(list).setDouble(tagName, value);	}
	private void setGenericKey(CompoundListElement list, String tagName, int value) 	{	getList(list).setInt(tagName, value);		}
	private void setGenericKey(CompoundListElement list, String tagName, String value) 	{	getList(list).setString(tagName, value);	}
	
	private void removeGenericKey(CompoundListElement list, String tagName)				{	getList(list).remove(tagName);				}
	
	private NBTTagCompound getList(CompoundListElement list)			{	return CompoundListElement.getElement(getNBTTag(), list);	}
}
