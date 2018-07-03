package pl.mateam.marpg.api.regular.objects.mobs;

import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

public interface CommodoreMob {
	int getLevel();
	double getDamage();
	Location getSpawnLocation();
	LivingEntity getBukkitEntity();
}
