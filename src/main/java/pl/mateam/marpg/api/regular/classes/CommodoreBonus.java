package pl.mateam.marpg.api.regular.classes;


public abstract class CommodoreBonus {
	public static abstract class CommodoreOffensiveBonus extends CommodoreBonus {}
	public static abstract class CommodoreDefensiveBonus extends CommodoreBonus {}
	public static abstract class CommodoreSpecialBonus extends CommodoreBonus {}

	public abstract String getNBTkey();
	public abstract String getLoreDescription();
}