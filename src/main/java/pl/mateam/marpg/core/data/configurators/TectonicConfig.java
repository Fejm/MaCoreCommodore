package pl.mateam.marpg.core.data.configurators;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.Bukkit;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.core.modules.external.server.sub.WorldsManagerImplementation;

@ReloaderName(name = CONFIGURATION_RELOADER.TECTONIC_PLATES)
public class TectonicConfig extends CommodoreConfigurationReloader {
	@Override public void action() {
		AtomicInteger counter = new AtomicInteger();
		
		Bukkit.getServer().getWorlds().forEach(v -> {
			((WorldsManagerImplementation) Core.getServer().getWorlds()).reloadTectonicInfoFromConfig(v);
			counter.addAndGet(Core.getServer().getWorlds().getTectonicPlatesCount(v));
		});
		
		int count = counter.get();
		if(count != 0){
			String word = count == 1? " płyty tektoniczne" : " płyt tektonicznych";
				
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- świat składa się z ", String.valueOf(count), word);
		}
	}
}
