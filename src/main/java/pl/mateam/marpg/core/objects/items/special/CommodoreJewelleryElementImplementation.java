package pl.mateam.marpg.core.objects.items.special;

import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.ItemStack;
import pl.mateam.marpg.api.regular.objects.items.special.CommodoreJewelleryElement;
import pl.mateam.marpg.core.internal.enums.NBT_Attribute;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo.ItemType;
import pl.mateam.marpg.core.objects.items.CommodoreSpecialItemImplementation;

public class CommodoreJewelleryElementImplementation extends CommodoreSpecialItemImplementation implements CommodoreJewelleryElement {
	private static class CommodoreShieldEffectiveStatsImplementation implements JewelleryEffectiveStats {
		private final CommodoreJewelleryElementImplementation element;
		private CommodoreShieldEffectiveStatsImplementation(CommodoreJewelleryElementImplementation element) {
			this.element = element;
		}
		
		@Override public int getRegenerationRate() {
			return (int) (element.getStatsMultiplier() * element.getRegenerationMultiplier() * element.regeneratedValue);
		}
	}
	
	private final int bonusMaxValue;
	private final int regeneratedValue;
	public CommodoreJewelleryElementImplementation(ItemStack nmsStack) {
		super(nmsStack);
		ItemType type = new ItemTypeInfo(getKeyEffectiveString(NBT_Attribute.TYPE.getKey())).getExactType();
		switch(type){
			case NECKLACE:
				this.bonusMaxValue = 10;
				this.regeneratedValue = 40;
				break;
			case RING:
				this.bonusMaxValue = 5;
				this.regeneratedValue = 20;
				break;
			default:
				this.bonusMaxValue = 0;
				this.regeneratedValue = 0;
		}

	}

	@Override protected int getBonusMaxValue() 						{	return bonusMaxValue;																}
	@Override public JewelleryEffectiveStats getEffectiveStats() 	{	return new CommodoreShieldEffectiveStatsImplementation(this);						}
	@Override public double getRegenerationMultiplier() 			{	return getKeyEffectiveDouble(NBT_Attribute.REGENERATION_MULTIPLIER.getKey());		}

	@Override protected void appendCoreBonusInfo(List<String> lore) {
		String bonusName = null;
		
		bonusName = isNecklace()? "Bonus do regeneracji PŻ: " : "Bonus do regeneracji PE: ";
		lore.add(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + bonusName + ChatColor.WHITE.toString() + getEffectiveStats().getRegenerationRate() + "%");
	}
	
	@Override public boolean isNecklace()	{	return new ItemTypeInfo(getKeyEffectiveString(NBT_Attribute.TYPE.getKey())).getExactType() == ItemType.NECKLACE;	}
}
