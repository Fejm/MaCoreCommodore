package pl.mateam.marpg.core.objects.items.special;

import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.ItemStack;

import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.objects.items.special.CommodoreShield;
import pl.mateam.marpg.core.internal.enums.NBT_Attribute;
import pl.mateam.marpg.core.objects.items.CommodoreSpecialItemImplementation;

public class CommodoreShieldImplementation extends CommodoreSpecialItemImplementation implements CommodoreShield {
	private static class CommodoreShieldEffectiveStatsImplementation implements ShieldEffectiveStats {
		private final CommodoreShieldImplementation element;
		private CommodoreShieldEffectiveStatsImplementation(CommodoreShieldImplementation element) {
			this.element = element;
		}

		@Override public double getBlockRate() {
			return CoreUtils.math.round((1-(1/(1+(0.25 * element.getStatsMultiplier() * element.getBlockMultiplier())))) * 100, 2);
		}
	}
	
	public CommodoreShieldImplementation(ItemStack nmsStack) {
		super(nmsStack);
	}

	@Override protected int getBonusMaxValue() 					{	return 20;																				}
	@Override public ShieldEffectiveStats getEffectiveStats() 	{	return new CommodoreShieldEffectiveStatsImplementation(this);							}
	@Override public double getBlockMultiplier() 				{	return getKeyEffectiveDouble(NBT_Attribute.BLOCKING_MULTIPLIER.getKey());			}

	@Override protected void appendCoreBonusInfo(List<String> lore) {
		lore.add(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "Ilość blokowanych obrażeń: " + ChatColor.WHITE.toString() + getEffectiveStats().getBlockRate() + "%");
	}
}
