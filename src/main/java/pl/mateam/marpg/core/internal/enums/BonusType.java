package pl.mateam.marpg.core.internal.enums;

public enum BonusType {
	OFFENSIVE(1),
	DEFENSIVE(0.6),
	SPECIAL(0.2);
	
	private final double maxMultiplier;		public double getMaxValueMultiplier()	{	return maxMultiplier;	}
	private BonusType(double maxMultiplier){
		this.maxMultiplier = maxMultiplier;
	}
}
