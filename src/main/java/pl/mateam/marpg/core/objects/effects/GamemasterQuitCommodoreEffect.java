package pl.mateam.marpg.core.objects.effects;

import java.util.Random;

import net.md_5.bungee.api.ChatColor;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.scheduler.BukkitRunnable;

import pl.mateam.marpg.api.copied.ParticleEffect;
import pl.mateam.marpg.api.regular.classes.CommodoreEffect;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;

public class GamemasterQuitCommodoreEffect extends CommodoreEffect {
	public GamemasterQuitCommodoreEffect(Location location) 	{	super(location);	}

	@Override public void play(Object... parameters) {
		playInternal((String) parameters[0], (Runnable) parameters[1]);
	}
	
	public void playInternal(String batName, Runnable codeToExecuteAfterBatDisappear) {
		World world = location.getWorld();
		
		final Entity bat = world.spawnEntity(location, EntityType.BAT);
		Location nloc = bat.getLocation(); 
		bat.setCustomName(ChatColor.RED.toString() + ChatColor.BOLD.toString() + batName);
		bat.setCustomNameVisible(true);
		bat.setGlowing(true);
		
		new BukkitRunnable() {
			public void run() {
            	world.playEffect(nloc, Effect.SMOKE, new Random().nextInt(9));
				ParticleEffect.LAVA.display(0.1F, 0.05F, 0.1F, 0, 40, nloc, 255);
            	bat.remove();
            	codeToExecuteAfterBatDisappear.run();
            }
        }.runTaskLater(MaCoreCommodoreEngine.getReference(), 60);
	}
}
