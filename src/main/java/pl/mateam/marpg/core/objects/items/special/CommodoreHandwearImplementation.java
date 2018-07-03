package pl.mateam.marpg.core.objects.items.special;

import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.ItemStack;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.objects.items.special.CommodoreHandwear;
import pl.mateam.marpg.core.internal.enums.NBT_Attribute;
import pl.mateam.marpg.core.objects.items.CommodoreSpecialItemImplementation;

public class CommodoreHandwearImplementation extends CommodoreSpecialItemImplementation implements CommodoreHandwear {	
	private static class CommodoreHandwearEffectiveStatsImplementation implements HandwearEffectiveStats {
		private final CommodoreHandwearImplementation element;
		private CommodoreHandwearEffectiveStatsImplementation(CommodoreHandwearImplementation element) {
			this.element = element;
		}

		@Override public double getFinalSkillDamageMultiplier() {
			return element.getStatsMultiplier() * element.getKeyEffectiveDouble(NBT_Attribute.SKILL_DAMAGE_MULTIPLIER.getKey());
		}

		@Override public double getMinimalDamage()	 {	return getAverageDamage() * (1 - element.getDamageAmplitudePercent() / 100D);	}
		@Override public double getMaximalDamage()	 {	return getAverageDamage() * (1 + element.getDamageAmplitudePercent() / 100D);	}
		
		@Override public double getAverageDamage() {
			return element.getStatsMultiplier() * element.getKeyEffectiveDouble(NBT_Attribute.DAMAGE_MULTIPLIER.getKey())
					* element.getKeyEffectiveDouble(NBT_Attribute.DAMAGE_BASE.getKey());
		}

		@Override public double getRandomDamage() {
			double damageRandomizedMultiplier = 1 + ((Math.random() * 2 - 1) * element.getDamageAmplitudePercent()/100D);
			return damageRandomizedMultiplier * getAverageDamage();
		}
	}

	public CommodoreHandwearImplementation(ItemStack nmsStack) {
		super(nmsStack);
	}

	@Override public double getSkillDamageMultiplier() 	{	return getStatsMultiplier() * getKeyEffectiveDouble(NBT_Attribute.SKILL_DAMAGE_MULTIPLIER.getKey());	}
	@Override public int getDamageAmplitudePercent() 	{	return getKeyEffectiveInteger(NBT_Attribute.DAMAGE_AMPLITUDE.getKey());									}

	@Override public double getChanceToMineSuccesfully() {
		if(containsEffectivelyKey(NBT_Attribute.PICKAXE_MINING_CHANCE.getKey()))
			return getKeyEffectiveDouble(NBT_Attribute.PICKAXE_MINING_CHANCE.getKey());
		return -1;
	}

	@Override protected int getBonusMaxValue() 					{	return 20;																			}
	@Override public double getDamageMultiplier() 				{	return getKeyEffectiveDouble(NBT_Attribute.DAMAGE_MULTIPLIER.getKey());				}
	@Override public HandwearEffectiveStats getEffectiveStats() {	return new CommodoreHandwearEffectiveStatsImplementation(this);						}
	
	@Override protected void appendCoreBonusInfo(List<String> lore) {
		if(!getKeyEffectiveBoolean(NBT_Attribute.REGULAR.getKey()))
			return;
		HandwearEffectiveStats stats = getEffectiveStats();
		lore.add(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "Zadawane obrażenia: " + ChatColor.WHITE.toString() + CoreUtils.math.round(stats.getMinimalDamage(), 2) + "-" +
				CoreUtils.math.round(stats.getMaximalDamage(), 2) + ChatColor.DARK_RED.toString() + " \u2764");
		lore.add(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "Moc umiejętności bojowych: " + ChatColor.WHITE.toString() + CoreUtils.math.round(stats.getFinalSkillDamageMultiplier(), 1));
	}
}
