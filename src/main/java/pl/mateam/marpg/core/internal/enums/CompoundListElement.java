package pl.mateam.marpg.core.internal.enums;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;

public enum CompoundListElement {
	ATTRIBUTES("Atrybuty"), METADATA("Metadata"), BONUSES("Bonusy");
	
	private final String listName;	public String getName()	{	return listName;	}
	private CompoundListElement(String listName){
		this.listName = listName;
	}
	
	public static NBTTagCompound getElement(NBTTagCompound mainCompound, CompoundListElement element){
		NBTTagList list = ((NBTTagList) mainCompound.get("MaRPG"));
		for(int i = 0; i < list.size(); i++){
			NBTTagCompound newCompound = list.get(i);
			if(newCompound.getString("TypListy").equals(element.getName())){
				NBTTagList valuesList = (NBTTagList) newCompound.get("Wartosci");
				return valuesList.get(0);
			}
		}
		
		return insertList(list, element.getName());
	}
			
	public static NBTTagCompound overrideElement(NBTTagCompound mainCompound, CompoundListElement element){
		NBTTagList list = ((NBTTagList) mainCompound.get("MaRPG"));
		for(int i = 0; i < list.size(); i++){
			NBTTagCompound newCompound = list.get(i);
			if(newCompound.getString("TypListy").equals(element.getName())){
				list.remove(i);
				break;
			}
		}

		return insertList(list, element.getName());
	}
	
	private static NBTTagCompound insertList(NBTTagList coreList, String listName){
		NBTTagCompound compound = new NBTTagCompound();
		
		NBTTagList valuesList = new NBTTagList();
		NBTTagCompound valuesCompound = new NBTTagCompound();
		valuesList.add(valuesCompound);

		compound.setString("TypListy", listName);
		compound.set("Wartosci", valuesList);

		coreList.add(compound);
		return valuesCompound;
	}
}
