package pl.mateam.marpg.core.modules.external.utils;

import java.lang.reflect.Constructor;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.bukkit.Location;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.enums.effects.CommonEffect;
import pl.mateam.marpg.api.regular.modules.sub.server.EnvironmentManager;
import pl.mateam.marpg.api.utils.IngameUtils;
import pl.mateam.marpg.api.utils.NMSUtils;

public class IngameUtilsImplementation implements IngameUtils {
	@Override public void playSoundPrivate(Player player, boolean lector, String path){
		String directory = lector? "lektor." : "dzwieki.";
		player.stopSound("marpg." + directory + path);
		player.playSound(player.getLocation(), "marpg." + directory + path, 100F, 1F);
	}

	@Override public boolean isDay() {
		SimpleDateFormat format = new SimpleDateFormat("HH");
		byte currentHour = Byte.parseByte(format.format(new Date()));
		EnvironmentManager environment = Core.getServer().getEnvironment();
		int nightStart = environment.getStartOfTheNightHour();
		int nightEnd = environment.getEndOfTheNightHour();
		return currentHour < nightStart && currentHour > nightEnd;
	}

	@Override public void playEffect(CommonEffect effect, Location location, Object... parameters) {
		switch(effect) {
			default:	int todo;	throw new RuntimeException("Do efektu nie została podpięta żadna klasa!");
		}
	}
	
	@Override public void sendTitle(Player player, int fadeIn, int stay, int fadeOut, String title, String subtitle) {
		try {
			NMSUtils nms = CoreUtils.nms;
            if (title != null) {
                Object e = nms.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatTitle = nms.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                Constructor<?> subtitleConstructor = nms.getNMSClass("PacketPlayOutTitle").getConstructor(nms.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], nms.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object titlePacket = subtitleConstructor.newInstance(e, chatTitle, fadeIn, stay, fadeOut);
                nms.sendPacket(player, titlePacket);
                e = nms.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TITLE").get(null);
                chatTitle = nms.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                subtitleConstructor = nms.getNMSClass("PacketPlayOutTitle").getConstructor(nms.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], nms.getNMSClass("IChatBaseComponent"));
                titlePacket = subtitleConstructor.newInstance(e, chatTitle);
                nms.sendPacket(player, titlePacket);
            }
            if (subtitle != null) {
                Object e = nms.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("TIMES").get(null);
                Object chatSubtitle = nms.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + title + "\"}");
                Constructor<?> subtitleConstructor = nms.getNMSClass("PacketPlayOutTitle").getConstructor(nms.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], nms.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                Object subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
                nms.sendPacket(player, subtitlePacket);
                e = nms.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0].getField("SUBTITLE").get(null);
                chatSubtitle = nms.getNMSClass("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\"" + subtitle + "\"}");
                subtitleConstructor = nms.getNMSClass("PacketPlayOutTitle").getConstructor(nms.getNMSClass("PacketPlayOutTitle").getDeclaredClasses()[0], nms.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE);
                subtitlePacket = subtitleConstructor.newInstance(e, chatSubtitle, fadeIn, stay, fadeOut);
                nms.sendPacket(player, subtitlePacket);
            }
        }
        catch (Exception var11) {
            var11.printStackTrace();
        }
	}
}
