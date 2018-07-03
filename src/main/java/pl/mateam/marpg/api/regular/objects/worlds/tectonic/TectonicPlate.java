package pl.mateam.marpg.api.regular.objects.worlds.tectonic;


public interface TectonicPlate {
	int getLayersCount();
	TectonicPlateMobsManager getMobsManager();
	TectonicPlateSettingsManager getSettingsManager();
	TectonicPlateMusicManager getMusicManager();
	TectonicPlateOresManager getOresManager();
	void spawnRandomMob();
}