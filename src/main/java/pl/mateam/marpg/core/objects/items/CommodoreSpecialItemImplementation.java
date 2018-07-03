package pl.mateam.marpg.core.objects.items;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_12_R1.NBTTagCompound;

import org.bson.Document;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.enums.items.Tier;
import pl.mateam.marpg.api.regular.enums.nbt.NBT_DefaultBonus;
import pl.mateam.marpg.api.regular.enums.players.CharacterClass;
import pl.mateam.marpg.api.regular.objects.items.CommodoreSpecialItem;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.internal.enums.BonusType;
import pl.mateam.marpg.core.internal.enums.CompoundListElement;
import pl.mateam.marpg.core.internal.enums.NBT_Attribute;
import pl.mateam.marpg.core.internal.hardcoded.TierCompartment;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo.ItemSlotType;
import pl.mateam.marpg.core.modules.external.server.sub.EnvironmentManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.EnvironmentManagerImplementation.BonusInfo;

public abstract class CommodoreSpecialItemImplementation extends CommodoreItemImplementation implements CommodoreSpecialItem {
	public CommodoreSpecialItemImplementation(net.minecraft.server.v1_12_R1.ItemStack nmsStack) {
		super(nmsStack);
	}

	@Override public boolean hasProperClass(CharacterClass playerClass) {
		int blockedClasses = getKeyEffectiveInteger(NBT_Attribute.BLOCKED_CLASSES.getKey());
		switch(playerClass){
			case WARRIOR: 	return ((blockedClasses >> 3) & 1) == 0;
			case BARBARIAN: return ((blockedClasses >> 2) & 1) == 0;
			case MAGE:		return ((blockedClasses >> 1) & 1) == 0;
			case SLAYER:	return ((blockedClasses >> 0) & 1) == 0;
			default:		return false;
		}
	}

	@Override public void allowUsingToClass(CharacterClass characterClass) {
		int blockedClasses = getKeyEffectiveInteger(NBT_Attribute.BLOCKED_CLASSES.getKey());
		switch(characterClass){
			case WARRIOR: 	setKey(NBT_Attribute.BLOCKED_CLASSES.getKey(), blockedClasses - (blockedClasses & (1 << 3)));	return;
			case BARBARIAN: setKey(NBT_Attribute.BLOCKED_CLASSES.getKey(), blockedClasses - (blockedClasses & (1 << 2)));	return;
			case MAGE:		setKey(NBT_Attribute.BLOCKED_CLASSES.getKey(), blockedClasses - (blockedClasses & (1 << 1)));	return;
			case SLAYER:	setKey(NBT_Attribute.BLOCKED_CLASSES.getKey(), blockedClasses - (blockedClasses & (1 << 0)));	return;
		}
	}
	
	@Override public void denyUsingToClass(CharacterClass characterClass) {
		int blockedClasses = getKeyEffectiveInteger(NBT_Attribute.BLOCKED_CLASSES.getKey());
		switch(characterClass){
			case WARRIOR: 	setKey(NBT_Attribute.BLOCKED_CLASSES.getKey(), blockedClasses | (1 << 3));	return;
			case BARBARIAN: setKey(NBT_Attribute.BLOCKED_CLASSES.getKey(), blockedClasses | (1 << 2));	return;
			case MAGE:		setKey(NBT_Attribute.BLOCKED_CLASSES.getKey(), blockedClasses | (1 << 1));	return;
			case SLAYER:	setKey(NBT_Attribute.BLOCKED_CLASSES.getKey(), blockedClasses | (1 << 0));	return;
		}
	}

	@Override public int getItemLevel() 					{	return getKeyEffectiveInteger(NBT_Attribute.REQUIRED_LEVEL.getKey());			}
	@Override public boolean isBonusable() 					{	return getKeyEffectiveBoolean(NBT_Attribute.BONUSABLE.getKey());				}
	@Override public void setBonusable(boolean bonusable) 	{	setKey(NBT_Attribute.BONUSABLE.getKey(), bonusable);							}
	@Override public void clearBonuses()					{	CompoundListElement.overrideElement(getNBTTag(), CompoundListElement.BONUSES);	}


	@Override public double getOffensiveBonusChance() {
		if(!isBonusable())		return -1;
		return getKeyEffectiveDouble(NBT_Attribute.OFFENSIVE_BONUS_CHANCE.getKey());
	}

	@Override public double getDefensiveBonusChance() {
		if(!isBonusable())		return -1;
		return getKeyEffectiveDouble(NBT_Attribute.DEFENSIVE_BONUS_CHANCE.getKey());
	}

	@Override public double getSpecialBonusChance() {
		if(!isBonusable())		return -1;
		return getKeyEffectiveDouble(NBT_Attribute.SPECIAL_BONUS_CHANCE.getKey());
	}
	
	@Override public int getUpgradementLevel() {
		if(!getKeyEffectiveBoolean(NBT_Attribute.REGULAR.getKey()))		//Non-upgradeable
			return -1;
		return getKeyEffectiveInteger(NBT_Attribute.UPGRADEMENT.getKey());
	}

	@Override public void setUpgradementLevel(int newLevel) {
		if(getKeyEffectiveBoolean(NBT_Attribute.REGULAR.getKey()))		//Non-upgradeable
			setKey(NBT_Attribute.UPGRADEMENT.getKey(), newLevel);		
	}
	
	@Override public Tier getTier() {									//Returns null if non-tierable
		int tier = getKeyEffectiveInteger(NBT_Attribute.TIER.getKey());
		return Tier.getUsingID(tier);
	}

	@Override public void setTier(Tier newTier) {
		if(getKeyEffectiveBoolean(NBT_Attribute.REGULAR.getKey()))		//Tierable
			setKey(NBT_Attribute.TIER.getKey(), newTier.getID());
	}
	
	@Override public void setRandomTier(int tierBonus){
		if(!getKeyEffectiveBoolean(NBT_Attribute.REGULAR.getKey()))		//Non-tierable
			return;

		Tier tier = null;
		int number = TierCompartment.SUM;
		
		for(int i = 0; i < tierBonus + 1; i++){
			int local = (int) (Math.random() * TierCompartment.SUM + 1);
			if(local < number) number = local;
		}
		if (number >= TierCompartment.DAMAGED)			tier = Tier.DAMAGED;
		else if (number >= TierCompartment.POORLY_MADE)	tier = Tier.POORLY_MADE;
		else if (number >= TierCompartment.CASUAL)		tier = Tier.CASUAL;
		else if (number >= TierCompartment.STRENGHENED)	tier = Tier.STRENGHENED;
		else if (number >= TierCompartment.ELITE)		tier = Tier.ELITE;
		else if (number >= TierCompartment.EPIC)		tier = Tier.EPIC;
		else if (number >= TierCompartment.LEGENDARY)	tier = Tier.LEGENDARY;
		else 											tier = Tier.DIVINE;
		
		setTier(tier);
	}
	
	protected double getStatsMultiplier()	{	
		if(!getKeyEffectiveBoolean(NBT_Attribute.REGULAR.getKey()))
			return 1;
		else
			return (((getItemLevel() / 10 * 10) / 45D) + 1) * ((getUpgradementLevel() / 9D) + 1) * (((getTier().getID() + 1) * 0.25));
	}

	//color is used in Armor subclasses
	@Override public ItemStack getEffectiveAppearance(CharacterClass playerClass, int playerLevel, float colorValue) {
		boolean hasProperClass = hasProperClass(playerClass);
		boolean hasLevel = getItemLevel() <= playerLevel;
		boolean canUse = hasProperClass && hasLevel;
		
		ItemStack item = craftItemStack();
		ItemMeta meta = item.getItemMeta();
		List<String> lore = meta.getLore();
		
		lore = lore == null? new ArrayList<>() : lore;
		
		if(getTier() != null){
			lore.add(0, getTier().getDisplayedName());
			lore.add(1, "");
		}

		appendCoreBonusInfo(lore);
		lore.add("");
		
		NBTTagCompound mainCompound = getNBTTag();
		if(isBonusable()) {
			NBTTagCompound bonusesList = CompoundListElement.getElement(mainCompound, CompoundListElement.BONUSES);
			int defaultMaxValueOfBonus = getBonusMaxValue();
			int offMax = getBonusMaxValue(BonusType.OFFENSIVE, defaultMaxValueOfBonus);
			int defMax = getBonusMaxValue(BonusType.DEFENSIVE, defaultMaxValueOfBonus);
			int spcMax = getBonusMaxValue(BonusType.SPECIAL, defaultMaxValueOfBonus);
			Set<String> bonuses = bonusesList.c();
			if(bonuses.size() > 0) {
				EnvironmentManagerImplementation environment = (EnvironmentManagerImplementation) Core.getServer().getEnvironment();
				for(String bonus : bonusesList.c())
					lore.add(environment.getBonus(bonus).getColoredDescription() + ": " + getColoredBonusValue(bonus, offMax, defMax, spcMax, bonusesList.getInt(bonus)));
				lore.add("");
			}
		}

		String displayedName = new ItemTypeInfo(getKeyEffectiveString(NBT_Attribute.TYPE.getKey())).getDisplayedName();
		if(displayedName != null)
			lore.add(ChatColor.DARK_PURPLE.toString() + ChatColor.BOLD.toString() + "Typ przedmiotu: " + ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD.toString() + displayedName);
		
		int itemLevel = getItemLevel();
		if(itemLevel > 0){
			char sign = hasProperClass? (hasLevel? '\u2714' : '\u2717') : '\u2022';
			lore.add(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + sign +
					ChatColor.DARK_GREEN.toString() + ChatColor.BOLD.toString() + "] Od poziomu: " + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + itemLevel);
		}
		
		
		int blockedClassesSign = getKeyEffectiveInteger(NBT_Attribute.BLOCKED_CLASSES.getKey());
		if(blockedClassesSign > 0){
			char sign = hasProperClass? '\u2714' : '\u2717';
			String whatToInsert1 = null;
			String whatToInsert2 = null;
			
			switch(blockedClassesSign){
				case (1 << 3) + (1 << 2) + (1 << 1) + (0 << 0):
					whatToInsert1 = "y: ";
					whatToInsert2 = "Zabójca";
					break;
				case (1 << 3) + (1 << 2) + (0 << 1) + (1 << 0):
					whatToInsert1 = "y: ";
					whatToInsert2 = "Mag";
					break;
				case (1 << 3) + (1 << 2) + (0 << 1) + (0 << 0):
					whatToInsert1 = ": ";
					whatToInsert2 = "Mag, Zabójca";
					break;
				case (1 << 3) + (0 << 2) + (1 << 1) + (1 << 0):
					whatToInsert1 = "y: ";
					whatToInsert2 = "Barbarzyńca";
					break;
				case (1 << 3) + (0 << 2) + (1 << 1) + (0 << 0):
					whatToInsert1 = ": ";
					whatToInsert2 = "Barbarzyńca, Zabójca";
					break;
				case (1 << 3) + (0 << 2) + (0 << 1) + (1 << 0):
					whatToInsert1 = ": ";
					whatToInsert2 = "Barbarzyńca, Mag";
					break;
				case (1 << 3) + (0 << 2) + (0 << 1) + (0 << 0):
					whatToInsert1 = ": ";
					whatToInsert2 = "Barbarzyńca, Mag, Zabójca";
					break;
				case (0 << 3) + (1 << 2) + (1 << 1) + (1 << 0):
					whatToInsert1 = "y: ";
					whatToInsert2 = "Wojownik";
					break;
				case (0 << 3) + (1 << 2) + (1 << 1) + (0 << 0):
					whatToInsert1 = ": ";
					whatToInsert2 = "Wojownik, Zabójca";
					break;
				case (0 << 3) + (1 << 2) + (0 << 1) + (1 << 0):
					whatToInsert1 = ": ";
					whatToInsert2 = "Wojownik, Mag";
					break;
				case (0 << 3) + (1 << 2) + (0 << 1) + (0 << 0):
					whatToInsert1 = ": ";
					whatToInsert2 = "Wojownik, Mag, Zabójca";
					break;
				case (0 << 3) + (0 << 2) + (1 << 1) + (1 << 0):
					whatToInsert1 = ": ";
					whatToInsert2 = "Wojownik, Barbarzyńca";
					break;
				case (0 << 3) + (0 << 2) + (1 << 1) + (0 << 0):
					whatToInsert1 = ": ";
					whatToInsert2 = "Wojownik, Barbarzyńca, Zabójca";
					break;
		}
			
			lore.add(ChatColor.DARK_GREEN.toString() + ChatColor.BOLD.toString() + "[" + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + sign +
					ChatColor.DARK_GREEN.toString() + ChatColor.BOLD.toString() + "] Dla klas" + whatToInsert1 + ChatColor.GREEN.toString() + ChatColor.BOLD.toString() + whatToInsert2);
		}

		int upgradementLevel = getUpgradementLevel();
		if(upgradementLevel > -1)
			meta.setDisplayName(meta.getDisplayName() + " +" + upgradementLevel);
		
		if(!canUse) {
			meta.setDisplayName(makeRed(meta.getDisplayName()));
			ArrayList<String> newLore = new ArrayList<>();
			lore.forEach(line -> newLore.add(makeRed(line)));
			meta.setLore(newLore);
		} else
			meta.setLore(lore);

		item.setItemMeta(meta);
		return item;
	}
	
	private String getColoredBonusValue(String bonus, int offMax, int defMax, int spcMax, int value){
		BonusType type = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getBonus(bonus).type;
		int max = 0;
		switch(type){
			case OFFENSIVE:
				max = offMax;
				break;
			case DEFENSIVE:
				max = defMax;
				break;
			case SPECIAL:
				max = spcMax;
				break;
		}
		
		ChatColor color = value == max? ChatColor.GOLD : value >= max * 0.8? ChatColor.YELLOW : ChatColor.WHITE;
		return color.toString() + value + "%";
	}
	
	@Deprecated		//Hardcoded color codes
	private String makeRed(String line){
		int i = -1;
		while((i = line.indexOf(ChatColor.COLOR_CHAR, i + 1)) != -1) {
			int colorCode = line.codePointAt(i + 1);
			if(colorCode == '6' || colorCode == 'a' || colorCode == 'd' || colorCode == 'e' || colorCode == 'f'){
				char[] chars = line.toCharArray();
				chars[i + 1] = 'c';
				line = String.valueOf(chars);
			} else
			if(colorCode == '2' || colorCode == '3' || colorCode == '5' || colorCode == '7' || colorCode == '8' || colorCode == '9' || colorCode == 'b'){
				char[] chars = line.toCharArray();
				chars[i + 1] = '4';
				line = String.valueOf(chars);
			}
		}
		return line;
	}
	
	protected abstract void appendCoreBonusInfo(List<String> lore);

	@Override public void changeBonuses(int boost) {
		if(getKeyEffectiveBoolean(NBT_Attribute.BONUSABLE.getKey())){
			NBTTagCompound mainCompound = getNBTTag();
			NBTTagCompound bonusesList = CompoundListElement.overrideElement(mainCompound, CompoundListElement.BONUSES);
			double offChance = getKeyEffectiveDouble(NBT_Attribute.OFFENSIVE_BONUS_CHANCE.getKey());
			double defChance = getKeyEffectiveDouble(NBT_Attribute.DEFENSIVE_BONUS_CHANCE.getKey());
			double spcChance = getKeyEffectiveDouble(NBT_Attribute.SPECIAL_BONUS_CHANCE.getKey());
			int defaultMaxValueOfBonus = getBonusMaxValue();
			int maxOffValue = getBonusMaxValue(BonusType.OFFENSIVE, defaultMaxValueOfBonus);
			int maxDefValue = getBonusMaxValue(BonusType.DEFENSIVE, defaultMaxValueOfBonus);
			int maxSpcValue = getBonusMaxValue(BonusType.SPECIAL, defaultMaxValueOfBonus);
			Set<BonusInfo> offensiveBonuses = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getOffensiveBonuses();
			Set<BonusInfo> defensiveBonuses = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getDefensiveBonuses();
			Set<BonusInfo> specialBonuses = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getSpecialBonuses();
			Random random = new Random();
			for(int i = 0; i < 5; i++) {
				double rand = random.nextDouble();
				if(rand < offChance)
					addBonus(random, offensiveBonuses, maxOffValue, boost, bonusesList);
				else {
					rand -= offChance;
					if(rand < defChance)
						addBonus(random, defensiveBonuses, maxDefValue, boost, bonusesList);
					else {
						rand -= defChance;
						if(rand < spcChance)
							addBonus(random, specialBonuses, maxSpcValue, boost, bonusesList);
					}
				}
			}
		}
	}
	
	private int getBonusMaxValue(BonusType bonusType, int defaultValue){
		return (int) (defaultValue * bonusType.getMaxValueMultiplier());
	}
	
	private void addBonus(Random randomObj, Set<BonusInfo> availableBonusesSet, int maxValueOfBonus, int boost, NBTTagCompound bonusesList){
		BonusInfo bonus = pickBonus(randomObj, availableBonusesSet);
		if(bonus != null) {
			int bonusValue = getBonusValue(randomObj, maxValueOfBonus, boost);
			if(bonusValue >= maxValueOfBonus * 0.2){
				availableBonusesSet.remove(bonus);
				bonusesList.setInt(bonus.key, bonusValue);
			}
		}
	}
	
	private int getBonusValue(Random randomObj, int maxValue, int boost) {		
		double globalmax = 0, local;
		for(int i = 0; i < 1 + boost; i++) {
			local = randomObj.nextGaussian() - 1;
			if(local > globalmax) globalmax = local;							
		}
		
		if(globalmax > 1)
			globalmax = 1;
		
		return (int) (globalmax * maxValue);
	}
		
	private BonusInfo pickBonus(Random randomObj, Set<BonusInfo> availableBonusesSet){
		int index = randomObj.nextInt(availableBonusesSet.size());
		int i = 0;
		for(BonusInfo info : availableBonusesSet){
			if(i == index)
				return info;
			i++;
		}
		return null;
	}

	@Override public void setBonusValue(NBT_DefaultBonus bonus, int value) 	{	setBonusValue(bonus.getKey(), value);	}
	@Override public void setBonusValue(String bonusName, int value) {
		BonusType bonusType = ((EnvironmentManagerImplementation) Core.getServer().getEnvironment()).getBonus(bonusName).type;
		if(bonusType != null){
			int bonusMaxValue = (int) (getBonusMaxValue() * bonusType.getMaxValueMultiplier());
			value = value > bonusMaxValue? bonusMaxValue : value;
			setKey(bonusName, value);
		}
	}
	
	@Override	@Secret
	public Document convertToPlainDocument() {
		Document result = super.convertToPlainDocument();
		putSubDocument(result, getNBTTag(), CompoundListElement.BONUSES);
		return result;
	}
	
	@Secret public ItemSlotType getValidSlotType() 		{	return new ItemTypeInfo(getKeyEffectiveString(NBT_Attribute.TYPE.getKey())).getCorrespondingSlotType();	}
	@Secret public Map<String, Integer> getBonusesValues()	{
		Map<String, Integer> bonusValues = new HashMap<>(); 
		NBTTagCompound bonuses = CompoundListElement.getElement(getNBTTag(), CompoundListElement.BONUSES);
		for(String bonusName : bonuses.c())
			bonusValues.put(bonusName, bonuses.getInt(bonusName));
		return bonusValues;
	}
	
	protected abstract int getBonusMaxValue();
}
