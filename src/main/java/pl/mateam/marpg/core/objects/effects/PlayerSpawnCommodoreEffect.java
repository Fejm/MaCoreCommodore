package pl.mateam.marpg.core.objects.effects;

import org.bukkit.Location;

import pl.mateam.marpg.api.copied.ParticleEffect;
import pl.mateam.marpg.api.regular.classes.CommodoreEffect;

public class PlayerSpawnCommodoreEffect extends CommodoreEffect {
	public PlayerSpawnCommodoreEffect(Location location) 	{	super(location);	}

	@Override public void play(Object... parameters) {
		playInternal();
	}
	
	public void playInternal() {
		ParticleEffect.EXPLOSION_NORMAL.display(0.2f, 0.4f, 0.2f, 0, 100, location, 255);
	}
}
