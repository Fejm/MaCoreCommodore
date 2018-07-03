package pl.mateam.marpg.api.regular.enums.configuration;

public enum ConfigurationDirectory {
	CONFIGURATION("Konfiguracja/"),
	MOBS(ConfigurationDirectory.CONFIGURATION.getPath() + "Moby/"),
	TECTONIC(ConfigurationDirectory.CONFIGURATION.getPath() + "Plyty tektoniczne/");

	private final String path;
	private ConfigurationDirectory(String path){
		this.path = path;
	}
	
	public String getPath()		{	return path;	}
}
