package pl.mateam.marpg.api.regular.modules.sub.server;

public interface SkinsManager {
	boolean isRendered(String name);
	void render(String name, String value, String signature, boolean overrideExisting);
	void renderUsingPremiumNickname(String premiumNickname, boolean overrideExisting);
}
