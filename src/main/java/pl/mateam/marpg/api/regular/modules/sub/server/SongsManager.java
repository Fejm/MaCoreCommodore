package pl.mateam.marpg.api.regular.modules.sub.server;

public interface SongsManager {
	int getDurability(String songName);
	int getIntervalBetweenSongs();
	void add(String songName, int durability);
	void setIntervalBetweenSongs(int intervalInTicks);
}
