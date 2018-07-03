package pl.mateam.marpg.api.regular.enums.configuration;

public enum ConfigurationPath {
	BANS				(	ConfigurationDirectory.CONFIGURATION.getPath() + "Bany.yml"),
	GENERIC_SETTINGS	(	ConfigurationDirectory.CONFIGURATION.getPath() + "Ogolne.yml"),
	INTERFACE			(	ConfigurationDirectory.CONFIGURATION.getPath() + "Interfejs.yml"),
	ITEMS				(	ConfigurationDirectory.CONFIGURATION.getPath() + "Przedmioty.yml"),
	LOCATIONS			(	ConfigurationDirectory.CONFIGURATION.getPath() + "Lokacje.yml"),
	LOCATIONS_OTHER		(	ConfigurationDirectory.CONFIGURATION.getPath() + "Lokacje dodatkowe.yml"),
	MECHANICS			(	ConfigurationDirectory.CONFIGURATION.getPath() + "Mechanika.yml"),
	SKINS				(	ConfigurationDirectory.CONFIGURATION.getPath() + "Skiny.yml"),
	SPECIAL_ITEMS		(	ConfigurationDirectory.CONFIGURATION.getPath() + "Specjalne przedmioty.yml"),
	MUSIC				(	ConfigurationDirectory.CONFIGURATION.getPath() + "Muzyka.yml"),
	RANKS				(	ConfigurationDirectory.CONFIGURATION.getPath() + "Rangi.yml");
	
	
	public String getPath()		{	return path;	}
	private final String path;
	private ConfigurationPath(String path){
		this.path = path;
	}
}
