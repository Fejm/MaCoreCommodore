package pl.mateam.marpg.api.regular.modules.sub.server;

public interface BansManager {
	void ban(String playername);
	void ban(String playername, int timeInMinutes);
	void ban(String playername, String reason);
	void ban(String playername, int timeInMinutes, String reason);
	void ban(String playername, String whobanned, String reason);
	void ban(String playername, String whoBanned, int timeInMinutes, String reason);
	void unban(String playername);
}
