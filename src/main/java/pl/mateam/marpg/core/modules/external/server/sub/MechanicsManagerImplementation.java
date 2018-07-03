package pl.mateam.marpg.core.modules.external.server.sub;

import net.minecraft.server.v1_12_R1.NBTTagCompound;
import net.minecraft.server.v1_12_R1.NBTTagList;
import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.modules.sub.server.MechanicsManager;
import pl.mateam.marpg.core.internal.enums.NBT_Attribute;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo.ItemTypeGroup;
import pl.mateam.marpg.core.internal.utils.CommodoreCalculator;


public class MechanicsManagerImplementation implements MechanicsManager {	
	private int optimalPvPtime = 20;
	private int optimalPvMTime = 15;
	private int skillDamagePercentage = 50;
	private int skillRegenerationTime = 20;
	private int delayBetweenPotions = 15;
	private int fistsDamageAmplitude = 10;
	private double regenerationValue = 0.3;
	
	private void recalculateWeaponsDamage(){
		ItemsManagerImplementation manager = ((ItemsManagerImplementation) Core.getServer().getItems());
		
		manager.performOnAll((array, compound) -> {
			if(!compound.hasKey(NBT_Attribute.TYPE.getKey()))
				return;
			ItemTypeGroup group = new ItemTypeInfo(compound.getString(NBT_Attribute.TYPE.getKey())).assignToGroup();
			if(group != ItemTypeGroup.HANDWEAR)
				return;
			
			double attackSpeed = getAttackSpeed(array[0]);
			double baseDamage = CommodoreCalculator.getOptimalDamage(attackSpeed);
			compound.setDouble(NBT_Attribute.DAMAGE_BASE.getKey(), baseDamage);
		});
	}
	
	private double getAttackSpeed(net.minecraft.server.v1_12_R1.ItemStack nmsStack){
		NBTTagCompound compound = nmsStack.getTag();
		NBTTagList list = (NBTTagList) compound.get("AttributeModifiers");
		for(int i = 0; i < list.size(); i++){
			NBTTagCompound newCompound = list.get(i);
			if(newCompound.getString("AttributeName").equals("generic.attackSpeed"))
				return newCompound.getDouble("Amount") + 4;
		}
		return 4;
	}
	
	@Override public void setOptimalPvPTime(int value) {
		this.optimalPvPtime = value;
		recalculateWeaponsDamage();
	}
	@Override public void setOptimalPvMTime(int value) 					{	this.optimalPvMTime = value;			}
	@Override public void setSkillDamagePercentage(int value) 			{	this.skillDamagePercentage = value;		}
	@Override public void setSkillRegenerationTime(int value) 			{	this.skillRegenerationTime = value;		}
	@Override public void setDelayBetweenPotions(int value) {
		this.delayBetweenPotions = value;
		recalculateWeaponsDamage();
	}
	@Override public void setFistsDamageAmplitude(int value)			{	this.fistsDamageAmplitude = value;		}
	@Override public void setRegenerationValue(double value)			{	this.regenerationValue = value;			}
	@Override public int getOptimalPvPTime() 							{	return optimalPvPtime;					}
	@Override public int getOptimalPvMTime() 							{	return optimalPvMTime;					}
	@Override public int getSkillDamagePercentage()						{	return skillDamagePercentage;			}
	@Override public int getSkillRegenerationTime() 					{	return skillRegenerationTime;			}
	@Override public int getDelayBetweenPotions() 						{	return delayBetweenPotions;				}
	@Override public int getFistsDamageAmplitude()						{	return fistsDamageAmplitude;			}
	@Override public double getRegenerationValue()						{	return regenerationValue;				}

	
	private int mobsAmountToNextLevel = 40;
	private float nextLevelPowerBase = 1.05F;
	private float mobsAtLowLevelExpPowerBase = 0.9F;

	@Override public void setMobsAmountToNextLevel(int value) 			{	mobsAmountToNextLevel = value;			}
	@Override public void setNextLevelPowerBase(float value) 			{	nextLevelPowerBase = value;				}
	@Override public void setMobsAtLowLevelExpPowerBase(float value) 	{	mobsAtLowLevelExpPowerBase = value;		}
	@Override public int getMobsAmountToNextLevel() 					{	return mobsAmountToNextLevel;			}
	@Override public float getNextLevelPowerBase() 						{	return nextLevelPowerBase;				}
	@Override public float getMobsAtLowLevelExpPowerBase() 				{	return mobsAtLowLevelExpPowerBase;		}
}