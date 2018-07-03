package pl.mateam.marpg.api.regular.enums.items;

public enum CommonItem {
	ENCHANTING_SCROLL_1(1),
	ENCHANTING_SCROLL_2(2),
	ENCHANTING_SCROLL_3(3),
	
	
	UPGRADING_GEM_1(4),
	UPGRADING_GEM_2(5),
	UPGRADING_GEM_3(6),
	UPGRADING_GEM_4(7),
	UPGRADING_GEM_5(8),
	UPGRADING_GEM_6(9),
	UPGRADING_GEM_7(10),
	UPGRADING_GEM_8(11);


	public int getID()	{	return ID;	}
	private final int ID;
	private CommonItem(int ID)	{
		this.ID = ID;
	}
}
