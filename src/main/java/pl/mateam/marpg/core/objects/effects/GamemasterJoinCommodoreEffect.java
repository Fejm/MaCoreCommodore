package pl.mateam.marpg.core.objects.effects;

import java.util.Random;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.World;

import pl.mateam.marpg.api.regular.classes.CommodoreEffect;

public class GamemasterJoinCommodoreEffect extends CommodoreEffect {
	public GamemasterJoinCommodoreEffect(Location location) {	super(location);	}

	@Override public void play(Object... parameters) {
		playInternal();
	}
	
	public void playInternal() {
		Location oneUp = location.clone().add(0.0, 1.0, 0.0);
		World world = location.getWorld();
		Random random = new Random();
		for (int i = 0; i < 10; ++i) {
			world.playEffect(location, Effect.SMOKE, random.nextInt(9));
			world.playEffect(oneUp, Effect.MOBSPAWNER_FLAMES, random.nextInt(9));
		}
		world.createExplosion(location.getX(), location.getY(), location.getZ(), 0.0f, false, false);
		int x = location.getBlockX();
		double y = location.getBlockY();
		int z = location.getBlockZ();
		for (int i = 0; i < 30; ++i) {
			double xToStrike = random.nextBoolean()? x + random.nextInt(6) : x - random.nextInt(6);
			double zToStrike = random.nextBoolean()? z + random.nextInt(6) : z - random.nextInt(6);
			Location toStrike = new Location(world, xToStrike, y, zToStrike);
			world.strikeLightningEffect(toStrike);
		}
	}
}
