package pl.mateam.marpg.core.internal.enums;



public enum NBT_Attribute {
	AMOUNT("ilosc"),
	
	COMMODOREID("commodoreid"),
	TIER("tier"),
	UPGRADEMENT("ulepszenie"),
	REQUIRED_LEVEL("poziom"),
	
	
	
	
	
	TYPE("typ"),
	BLOCKED_CLASSES("zablokowaneklasy"),
	VALUE("wartosc"),

	DAMAGE_BASE("bazowedmg"),
	DAMAGE_MULTIPLIER("mnoznikdmg"),
	SKILL_DAMAGE_MULTIPLIER("mnoznikdmgskilli"),
	DAMAGE_AMPLITUDE("wahaniaobrazen"),
	DEFENCE_MULTIPLIER("mnoznikobrony"),
	BLOCKING_MULTIPLIER("mnoznikbloku"),
	REGENERATION_MULTIPLIER("mnoznikregeneracji"),
	PICKAXE_MINING_CHANCE("gornictwo"),
	
	BONUSABLE("bonusy"),
	REGULAR("modyfikowalny"),
	OFFENSIVE_BONUS_CHANCE("szansanaofensywny"),
	DEFENSIVE_BONUS_CHANCE("szansanadefensywny"),
	SPECIAL_BONUS_CHANCE("szansanaspecjalny");


	private final String foreignKey;	public String getKey()	{	return foreignKey;	}
	private NBT_Attribute(String foreignKey) {
		this.foreignKey = foreignKey;
	}
	
	public static NBT_Attribute getByKey(String key) {
		for(NBT_Attribute attribute : NBT_Attribute.values())
			if(attribute.getKey().equals(key))
				return attribute;
		return null;
	}
}
