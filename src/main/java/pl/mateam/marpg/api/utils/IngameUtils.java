package pl.mateam.marpg.api.utils;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.regular.enums.effects.CommonEffect;


public interface IngameUtils {
	boolean isDay();
	void playSoundPrivate(Player player, boolean lector, String path);
	void playEffect(CommonEffect effect, Location location, Object... parameters);
	void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle);
}
