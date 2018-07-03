package pl.mateam.marpg.core.objects.items.special;

import java.util.List;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.ItemStack;
import pl.mateam.marpg.api.regular.enums.players.CharacterClass;
import pl.mateam.marpg.api.regular.objects.items.special.CommodoreArmorElement;
import pl.mateam.marpg.core.internal.enums.NBT_Attribute;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo;
import pl.mateam.marpg.core.internal.utils.InternalIngameUtils;
import pl.mateam.marpg.core.objects.items.CommodoreSpecialItemImplementation;

public class CommodoreArmorElementImplementation extends CommodoreSpecialItemImplementation implements CommodoreArmorElement {
	private static class ArmorElementEffectiveStatsImplementation implements ArmorElementEffectiveStats {
		private final CommodoreArmorElementImplementation element;
		private ArmorElementEffectiveStatsImplementation(CommodoreArmorElementImplementation element) {
			this.element = element;
		}
		
		@Override public int getDefence() {
			return (int) (element.getStatsMultiplier() * element.getDefenceMultiplier() * 20 * getDefenceMultiplierBasedOnItemType());
		}
		
		private int getDefenceMultiplierBasedOnItemType() {
			switch(new ItemTypeInfo(element.getKeyEffectiveString(NBT_Attribute.TYPE.getKey())).getExactType()){
				case HELMET:		return 2;
				case CHESTPLATE:	return 4;
				case LEGGINGS:		return 3;
				case BOOTS:			return 1;
				default:			return 0;
			}
		}
	}
	
	public CommodoreArmorElementImplementation(ItemStack nmsStack) {
		super(nmsStack);
	}

	private ArmorElementEffectiveStats stats = new ArmorElementEffectiveStatsImplementation(this);
	
	@Override protected int getBonusMaxValue()	 						{	return 10;																			}
	@Override public double getDefenceMultiplier() 						{	return getKeyEffectiveDouble(NBT_Attribute.DEFENCE_MULTIPLIER.getKey());			}
	@Override public ArmorElementEffectiveStats getEffectiveStats()		{	return new ArmorElementEffectiveStatsImplementation(this);							}

	@Override protected void appendCoreBonusInfo(List<String> lore) {
		lore.add(ChatColor.GRAY.toString() + ChatColor.BOLD.toString() + "Wartość obrony: " + ChatColor.WHITE.toString() + stats.getDefence());
	}
	
	@Override public org.bukkit.inventory.ItemStack getEffectiveAppearance(CharacterClass playerClass, int playerLevel, float colorValue){
		org.bukkit.inventory.ItemStack result = super.getEffectiveAppearance(playerClass, playerLevel, colorValue);
		return InternalIngameUtils.setArmorValidColor(result, colorValue);
	}
}
