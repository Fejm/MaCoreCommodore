package pl.mateam.marpg.api.regular.enums.players;

public enum CharacterClass {
	WARRIOR("Wojownik"), BARBARIAN("Barbarzyńca"), MAGE("Mag"), SLAYER("Zabójca");
	
	private final String ingameName;
	private CharacterClass(String ingameName)	{	this.ingameName = ingameName;	}
	public String getIngameName()	{	return ingameName;	}
}
