package pl.mateam.marpg.api.regular.modules.sub.server;

public interface OresManager {
	int getMinimalRegenerationTimeInSeconds();
	int getMaximalRegenerationTimeInSeconds();
	void setMinimalRegenerationTimeInSeconds(int newValue);
	void setMaximalRegenerationTimeInSeconds(int newValue);
}
