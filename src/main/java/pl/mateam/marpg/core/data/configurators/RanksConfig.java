package pl.mateam.marpg.core.data.configurators;

import java.util.List;
import java.util.Set;

import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;
import pl.mateam.marpg.api.regular.modules.Server;
import pl.mateam.marpg.core.CommodoreCore;

@ReloaderName(name = CONFIGURATION_RELOADER.RANKS)
public class RanksConfig extends CommodoreConfigurationReloader {
	@Override public void action() {
		YamlConfiguration config = Core.getFiles().getConfig(ConfigurationPath.RANKS);
		Server serverSettings = Core.getServer();
		
		serverSettings.getRanks().setDefaultRankArmorColorValue((float) config.getDouble("Gracz.JasnoscKolorowPancerza"));
		serverSettings.getRanks().setPremiumArmorColorValue((float) config.getDouble("Premium.JasnoscKolorowPancerza"));
		serverSettings.getRanks().setNoblemanArmorColorValue((float) config.getDouble("Szlachcic.JasnoscKolorowPancerza"));
		
		List<String> gamemasters = config.getStringList("MistrzowieGry");
		Set<String> names = CommodoreCore.getHiddenMethods().getHiddenData().gamemastersNames;
		names.clear();
		for(String gamemasterName : gamemasters){
			names.add(gamemasterName);
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- ", gamemasterName, " zarejestrowany jako Mistrz Gry");
		}
	}
}
