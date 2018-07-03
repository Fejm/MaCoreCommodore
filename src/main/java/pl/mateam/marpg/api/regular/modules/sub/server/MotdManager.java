package pl.mateam.marpg.api.regular.modules.sub.server;

import java.util.List;

import com.comphenix.protocol.wrappers.WrappedGameProfile;

public interface MotdManager {
	public String getMotd();
	List<WrappedGameProfile> getHoverMotd();
	String getFirstLine();
	String getSecondLine();
	String getHoverMotdLine(int indexOfLine);
	String getHoverMotdMetaLine(String metaName);
	void setFirstLine(String line);
	void setSecondLine(String line);
	void setHoverMotd(List<String> newMotd);
	void setHoverMotd(int indexOfLine, String line);
	void removeHoverMotdLine(int indexOfLine);
	void restoreMotdFromConfig();
	void restoreHoverMotdFromConfig();
	void setHoverMotdMetaLine(String metaName, String line);
	void removeHoverMotdMetaLine(String metaName);
	void purgeHoverMotdMeta();
	
	void saveCurrentMotdToConfig();
	void saveCurrentHoverMotdToConfig();
	
	boolean doesMotdSaveAutomatically();
	boolean doesHoverMotdSaveAutomatically();
	void setMotdSavingAutomatically(boolean state);
	void setHoverMotdSavingAutomatically(boolean state);
}
