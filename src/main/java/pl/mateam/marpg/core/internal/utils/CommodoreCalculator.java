package pl.mateam.marpg.core.internal.utils;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.modules.sub.server.MechanicsManager;

public class CommodoreCalculator {
	private static final float optimalAttackSpeed = (float) 1.6;
	private static final float a = (float) 0.0104194444;
	private static final float b = (float) 0.07916;
	//Works good for speed < 20.
	
	public static double getOptimalDamage(double attackSpeed){
		MechanicsManager mechanics = Core.getServer().getMechanics();
		double playerHPincludingPotions = 20 + (mechanics.getOptimalPvPTime()/mechanics.getDelayBetweenPotions() + 1) * 4;		//4 is the value of HP regenerated by potion
		double damageTakenByHits = playerHPincludingPotions * (100 - mechanics.getSkillDamagePercentage()) / 100D;
		double damagePerSecond = damageTakenByHits / mechanics.getOptimalPvPTime();
		double notCompensatedDamage = damagePerSecond / attackSpeed;
				
		double compensationBase = Math.abs(attackSpeed - optimalAttackSpeed);
		return (compensationBase * (compensationBase * a + b) + 1) * notCompensatedDamage;
	}
	
	public static double getFistsDamage() 	{	return getOptimalDamage(4.0) / 2;	}
}
