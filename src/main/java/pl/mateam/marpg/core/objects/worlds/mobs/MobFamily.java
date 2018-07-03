package pl.mateam.marpg.core.objects.worlds.mobs;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;
import org.bukkit.entity.Villager.Profession;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.mobs.CommodoreMob;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.modules.external.server.sub.WorldsManagerImplementation;

public class MobFamily {
	private MobFamily parent;
	private final EntityType entityType;
	private final Profession appearance;
	private final NamesContainer names = new NamesContainer();
	private final List<DropElement> drops = new ArrayList<>();
	private final Map<LivingEntity, CommodoreMobImplementation> mobs = new HashMap<>();
	
	private double moneyMultiplier = 1.0;
	private double healthMultiplier = 1.0;
	private double damageMultiplier = 1.0;

	public MobFamily(String parentName, EntityType entityType) {
		this.parent = (MobFamily) ((WorldsManagerImplementation) Core.getServer().getWorlds()).getMobFamily(parentName);
		this.entityType = entityType;
		this.appearance = null;
		assignParentValues();
	}
	
	public MobFamily(String parentName, int appearance) {
		this.parent = (MobFamily) ((WorldsManagerImplementation) Core.getServer().getWorlds()).getMobFamily(parentName);
		this.entityType = EntityType.ZOMBIE_VILLAGER;
		this.appearance = Profession.values()[appearance];
		assignParentValues();
	}
	
	public MobFamily(String parentName) {
		this.parent = (MobFamily) ((WorldsManagerImplementation) Core.getServer().getWorlds()).getMobFamily(parentName);
		this.entityType = parent.entityType;
		this.appearance = parent.appearance;
		assignParentValues();
	}

	public MobFamily(EntityType entityType) {
		this.entityType = entityType;
		this.appearance = null;
	}
	
	public MobFamily(int appearance) {
		this.entityType = EntityType.ZOMBIE_VILLAGER;
		this.appearance = Profession.values()[appearance];
	}
	
	private void assignParentValues() {
		this.moneyMultiplier = parent.moneyMultiplier;
		this.healthMultiplier = parent.healthMultiplier;
		this.damageMultiplier = parent.damageMultiplier;
	}
	
	@Secret public CommodoreMob createImplementation(int level, Location location) {
		LivingEntity entity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
		if(entity instanceof Villager)
			((Villager) entity).setProfession(appearance);
		CommodoreMobImplementation mobObj = new CommodoreMobImplementation(this, entity, level);
		mobs.put(entity, mobObj);
		return mobObj;
	}
	@Secret public void removeMob(LivingEntity mob) {
		if(!mob.isDead())
			mob.remove();
		mobs.remove(mob);
	}
	
	@Secret public void setMoneyMultiplier(double newValue)		{	this.moneyMultiplier = newValue;	}
	@Secret public void setHealthMultiplier(double newValue)	{	this.healthMultiplier = newValue;	}
	@Secret public void setDamageMultiplier(double newValue)	{	this.damageMultiplier = newValue;	}
	@Secret public void setNames(int[] correspondingLevels, String[] correspondingNames) {
		names.setNames(correspondingLevels, correspondingNames);
	}
	@Secret public void addDrop(DropElement drop) 	{	drops.add(drop);	}
	
	@Secret public String getName(int level) 	{	return names.getNameCorrespondingToLevel(level);	}
	
	public static class DropElement {
		private final int ID;
		private final float dropChance;
		private int bonusesBonus = 0;
		private int tierBonus = 0;
		
		public DropElement(int ID, float dropChance) {
			this.ID = ID;
			this.dropChance = dropChance;
		}
		
		public void setBonusesBonus(int bonus) 	{	this.bonusesBonus = bonus;	}
		public void setTierBonus(int tierBonus)	{	this.tierBonus = tierBonus;	}
		
//		private ItemStack generate() {
//			CommodoreItem item = Core.getServer().getItems().getItem(ID);
//			if(item instanceof CommodoreSpecialItem) {
//				((CommodoreSpecialItem) item).changeBonuses(bonusesBonus);
//				((CommodoreSpecialItem) item).setRandomTier(tierBonus);
//			}
//			return item.craftItemStack();
//		}
	}

	private class NamesContainer {
		private int[] levels;
		private String[] names;
		
		private void setNames(int[] levels, String[] names) {
			this.levels = levels;
			this.names = names;
		}
		
		private String getNameCorrespondingToLevel(int level) {
			for(int i = 0; i < levels.length; i++)
				if(level <= i)
					return names[level];
			return null;
		}
	}
}
