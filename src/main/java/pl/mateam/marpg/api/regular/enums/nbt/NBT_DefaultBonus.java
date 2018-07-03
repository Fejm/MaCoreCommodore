package pl.mateam.marpg.api.regular.enums.nbt;


public enum NBT_DefaultBonus {
	MOBS("potwory"),
	WARRIORS("wojownicy"),
	BARBARIANS("barbarzyncy"),
	MAGES("magowie"),
	SLAYERS("zabojcy"),
	
	RESISTANCE_MOBS("odppotwory"),
	RESISTANCE_WARRIORS("odpwojownicy"),
	RESISTANCE_BARBARIANS("odpbarbarzyncy"),
	RESISTANCE_MAGES("odpmagowie"),
	RESISTANCE_SLAYERS("odpzabojcy"),
	
	SLOWNESS("spowolnienie"),
	SKILLSBONUS("bonusum"),
	CRITICAL("krytyczne"),
	BONUSDEF("bonusobr");
	
	private final String nbtKey;	public String getKey()	{	return nbtKey;	}
	private NBT_DefaultBonus(String nbtKey){
		this.nbtKey = nbtKey;
	}
}
