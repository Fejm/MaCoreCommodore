package pl.mateam.marpg.core.objects.effects;

import org.bukkit.Location;

import pl.mateam.marpg.api.copied.ParticleEffect;
import pl.mateam.marpg.api.regular.classes.CommodoreEffect;

public class OreRegenerationCommodoreEffect extends CommodoreEffect {
	public OreRegenerationCommodoreEffect(Location location) 	{	super(location);	}

	@Override public void play(Object... parameters) {
		playInternal();
	}
	
	public void playInternal() {
		ParticleEffect.VILLAGER_HAPPY.display(0.25f, 0.25f, 0.25f, 0, 100, location, 255);
	}
}
