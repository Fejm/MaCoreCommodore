package pl.mateam.marpg.core.data.configurators;


import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;
import pl.mateam.marpg.api.regular.modules.sub.server.MechanicsManager;

@ReloaderName(name = CONFIGURATION_RELOADER.MECHANICS)
public class MechanicsConfig extends CommodoreConfigurationReloader {
	@Override public void action() {
		YamlConfiguration config = Core.getFiles().getConfig(ConfigurationPath.MECHANICS);
		
		MechanicsManager manager = Core.getServer().getMechanics();
		
		manager.setDelayBetweenPotions(config.getInt("Walka.OdstepMiedzyMiksturami"));
		manager.setOptimalPvMTime(config.getInt("Walka.OrientacyjnyCzasZabijaniaPotwora"));
		manager.setOptimalPvPTime(config.getInt("Walka.OrientacyjnyCzasPojedynku"));
		manager.setSkillDamagePercentage(config.getInt("Walka.ProcentUdzialuSkilliWWalce"));
		manager.setSkillRegenerationTime(config.getInt("Walka.CzasRegeneracjiUmiejetnosci"));
		manager.setFistsDamageAmplitude(config.getInt("Walka.WahaniaObrazenZPiesci"));
		manager.setRegenerationValue(config.getDouble("Walka.RegenerowaneWartosciPEiPZ"));
		
		manager.setMobsAmountToNextLevel(config.getInt("Doswiadczenie.IloscPotworowDoNastepnegoPoziomu"));
		manager.setNextLevelPowerBase((float) config.getDouble("Doswiadczenie.PodstawaPotegiWyzszegoPoziomu"));
		manager.setMobsAtLowLevelExpPowerBase((float) config.getDouble("Doswiadczenie.PodstawaPotegiMobyNizszegoPoziomu"));
	}
}
