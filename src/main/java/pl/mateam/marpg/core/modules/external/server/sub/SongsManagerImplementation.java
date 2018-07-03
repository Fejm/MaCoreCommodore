package pl.mateam.marpg.core.modules.external.server.sub;

import java.util.HashMap;

import pl.mateam.marpg.api.regular.modules.sub.server.SongsManager;

public class SongsManagerImplementation implements SongsManager {
	private HashMap<String, Integer> songsDurability = new HashMap<>();
	private int songsInterval;
	
	@Override public int getDurability(String songName)						{	return songsDurability.getOrDefault(songName, -1);	}
	@Override public int getIntervalBetweenSongs()							{	return songsInterval;								}
	@Override public void add(String songName, int durability)				{	songsDurability.put(songName, durability);			}
	@Override public void setIntervalBetweenSongs(int intervalInTicks)		{	songsInterval = intervalInTicks;					}
}
