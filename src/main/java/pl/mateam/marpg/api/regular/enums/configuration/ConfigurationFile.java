package pl.mateam.marpg.api.regular.enums.configuration;

public enum ConfigurationFile {
	GENERIC_SETTINGS("generic settings"),
	INTERFACE("interface"),
	ITEMS("items"),
	LOCATIONS("locations"),
	MECHANICS("mechanics"),
	SKINS("skins"),
	MUSIC("music"),
	RANKS("ranks");
	
	private final String reloaderName;
	private ConfigurationFile(String reloaderName){
		this.reloaderName = reloaderName;
	}
	public String getReloaderName()		{	return reloaderName;	}
}
