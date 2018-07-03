package pl.mateam.marpg.core.objects.worlds.mobs;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import pl.mateam.marpg.api.regular.objects.mobs.CommodoreMob;

public class CommodoreMobImplementation implements CommodoreMob {
	private final LivingEntity entity;
	private final Location spawnLocation;
	private final MobFamily family;
	private final int level;
	
	public CommodoreMobImplementation(MobFamily family, LivingEntity entity, int level) {
		this.entity = entity;
		this.spawnLocation = entity.getLocation();
		this.family = family;
		this.level = level;
	}
	
	@Override public int getLevel() 					{	return level;			}
	@Override public Location getSpawnLocation() 		{	return spawnLocation;	}
	@Override public LivingEntity getBukkitEntity()		{	return entity;			}
	@Override public double getDamage() 				{	todo;					}
}
