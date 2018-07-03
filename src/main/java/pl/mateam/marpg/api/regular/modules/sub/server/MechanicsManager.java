package pl.mateam.marpg.api.regular.modules.sub.server;


public interface MechanicsManager {	
	void setOptimalPvPTime(int value);
	void setOptimalPvMTime(int value);
	void setSkillDamagePercentage(int value);
	void setSkillRegenerationTime(int value);
	void setDelayBetweenPotions(int value);
	void setFistsDamageAmplitude(int value);
	void setRegenerationValue(double value);

	int getOptimalPvPTime();
	int getOptimalPvMTime();
	int getSkillDamagePercentage();
	int getSkillRegenerationTime();
	int getDelayBetweenPotions();
	int getFistsDamageAmplitude();
	double getRegenerationValue();

	
	
	void setMobsAmountToNextLevel(int value);
	void setNextLevelPowerBase(float value);
	void setMobsAtLowLevelExpPowerBase(float value);
	
	int getMobsAmountToNextLevel();
	float getNextLevelPowerBase();
	float getMobsAtLowLevelExpPowerBase();
}
