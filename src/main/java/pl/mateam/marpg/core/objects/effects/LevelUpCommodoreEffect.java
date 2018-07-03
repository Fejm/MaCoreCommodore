package pl.mateam.marpg.core.objects.effects;

import java.lang.reflect.Field;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;

import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.copied.ParticleEffect;
import pl.mateam.marpg.api.regular.classes.CommodoreEffect;

public class LevelUpCommodoreEffect extends CommodoreEffect {
	public LevelUpCommodoreEffect(Location location) 	{	super(location);	}

	@Override public void play(Object... parameters) {
		playInternal((int) parameters[0], (float) parameters[1]);
	}
	
	public void playInternal(int newLevel, float rankColour) {
		double multiplier = Math.pow(1.05, newLevel);
		float offset = (float) (Math.sqrt(multiplier) * 0.003);
		
        double saturation = 0.5 +((double) newLevel / 200);
        int[] colours = CoreUtils.parsing.convertHSVtoRGB((short) (newLevel * 2), saturation, rankColour);

        Color color = Color.fromRGB(colours[0], colours[1], colours[2]);
		
        ParticleEffect.CLOUD.display(offset, offset, offset, 0, (int) (10 * multiplier), location, 255);
		
		Firework fw = (Firework) location.getWorld().spawnEntity(location, EntityType.FIREWORK);
		FireworkMeta meta = fw.getFireworkMeta();
        Builder effect = FireworkEffect.builder();
        effect.with(FireworkEffect.Type.BALL_LARGE);
        effect.flicker(true);
        effect.trail(true);
        effect.withFade(Color.fromRGB(0, 0, 0));
        effect.withColor(color);
        meta.clearEffects();
        meta.addEffect(effect.build());
        Field f;
		try {
			f = meta.getClass().getDeclaredField("power");
	        f.setAccessible(true);
	        f.set(meta, -1);
		} catch (Exception e) {
			e.printStackTrace();
		}
        fw.setFireworkMeta(meta);
	}
}
