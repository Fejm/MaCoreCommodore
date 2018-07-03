package pl.mateam.marpg.core.modules.external.server.sub;

import java.util.HashMap;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Bat;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.entity.Spider;
import org.bukkit.entity.Wolf;
import org.bukkit.entity.Zombie;

import pl.mateam.marpg.api.regular.modules.sub.server.WorldsManager;
import pl.mateam.marpg.api.regular.objects.worlds.tectonic.TectonicPlate;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.objects.worlds.CommodoreWorldImplementation;
import pl.mateam.marpg.core.objects.worlds.mobs.MobFamily;

public class WorldsManagerImplementation implements WorldsManager {
	private Map<World, CommodoreWorldImplementation> worlds = new HashMap<>();
	private Map<String, MobFamily> mobFamilies = new HashMap<>();
	private Map<String, Location> warps = new HashMap<>();
	
	
	@Secret public void prepareWorlds(){
		Bukkit.getServer().getWorlds().forEach(world -> {
			worlds.put(world, new CommodoreWorldImplementation(world));
			for(Entity en : world.getEntitiesByClasses(Spider.class, Zombie.class, Skeleton.class, Wolf.class, Bat.class)) {
				if(!en.hasMetadata("NPC"))
					en.remove();
			}
			world.setGameRuleValue("doFireTick", "false");
			world.setGameRuleValue("doMobLoot", "false");
			world.setGameRuleValue("mobGriefing", "false");
			world.setGameRuleValue("doMobSpawning", "false");
			world.setGameRuleValue("doWeatherCycle", "false");
			world.setGameRuleValue("showDeathMessages", "false");
			world.setGameRuleValue("naturalRegeneration", "false");
			world.setGameRuleValue("keepInventory", "true");
			world.setGameRuleValue("reducedDebugInfo", "true");
			world.setGameRuleValue("doLimitedCrafting", "true");
			world.setGameRuleValue("randomTickSpeed", "0");
		});
	}
	
	
	
	
	@Override public void putWarp(String locationName, Location location) 	{	warps.put(locationName, location);	}
	@Override public Location getWarp(String locationName) 					{	return warps.get(locationName);		}
	@Secret public MobFamily buildMobFamily(String familyName, EntityType type) {
		MobFamily family = new MobFamily(type);
		mobFamilies.put(familyName, family);
		return family;
	}
	@Secret public MobFamily buildMobFamily(String familyName, int appearanceType) {
		MobFamily family = new MobFamily(appearanceType);
		mobFamilies.put(familyName, family);
		return family;
	}
	@Secret public MobFamily buildMobFamily(String familyName, String parentFamilyName) {
		MobFamily family = new MobFamily(parentFamilyName);
		mobFamilies.put(familyName, family);
		return family;
	}
	@Secret public MobFamily buildMobFamily(String familyName, String parentFamilyName, EntityType type) {
		MobFamily family = new MobFamily(parentFamilyName, type);
		mobFamilies.put(familyName, family);
		return family;
	}
	@Secret public MobFamily buildMobFamily(String familyName, String parentFamilyName, int appearanceType) {
		MobFamily family = new MobFamily(parentFamilyName, appearanceType);
		mobFamilies.put(familyName, family);
		return family;
	}
	@Secret public MobFamily getMobFamily(String familyName)				{	return mobFamilies.get(familyName);	}
	
	@Secret public void reloadTectonicInfoFromConfig(World world)			{	worlds.get(world).reloadTectonicInfoFromConfig(); 					}
	
	@Override public TectonicPlate getTectonicPlate(Location location) 		{	return worlds.get(location.getWorld()).getTectonicPlate(location);	}
	@Override public int getTectonicPlatesCount(World world)				{	return worlds.get(world).getTectonicPlatesCount();					}

	@Override public void spawnMobFreely(MobFamily family, int level, Location location) {
		todo;
	}
	
	@Override public void replenishAllOres(boolean playEffect) {
		worlds.values().forEach(cWorld -> cWorld.getMainTectonicPlate().getOresManager().replenishAllOres(playEffect, true));
	}
}
