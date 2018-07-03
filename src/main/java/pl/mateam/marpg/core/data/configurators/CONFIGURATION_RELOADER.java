package pl.mateam.marpg.core.data.configurators;



/*
 * As ConfigurationFiles class representations requires String "key" property as argument of ReloaderName annotation,
 * this class simply holds ConfigurationFiles reloader names. This is kind of workaround - enum's String parameter cannot be used as String-argument of annotation.
 */
public class CONFIGURATION_RELOADER {
	public static final String BANS = "bans";
	public static final String GENERIC_SETTINGS = "generic settings";
	public static final String INTERFACE = "interface";
	public static final String ITEMS = "items";
	public static final String LOCATIONS = "locations";
	public static final String MECHANICS = "mechanics";
	public static final String MOBS = "mobs";
	public static final String MUSIC = "music";
	public static final String RANKS = "ranks";
	public static final String SKINS = "skins";
	public static final String TECTONIC_PLATES = "tectonic plates";
}
