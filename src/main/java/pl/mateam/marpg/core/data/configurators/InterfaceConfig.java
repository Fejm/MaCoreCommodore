package pl.mateam.marpg.core.data.configurators;

import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;
import pl.mateam.marpg.core.internal.utils.Parsers;
import pl.mateam.marpg.core.modules.external.server.sub.ItemsManagerImplementation;

@ReloaderName(name = CONFIGURATION_RELOADER.INTERFACE)
public class InterfaceConfig extends CommodoreConfigurationReloader {
	@Override public void action() {
		YamlConfiguration config = Core.getFiles().getConfig(ConfigurationPath.INTERFACE);
		
		AtomicInteger counter = new AtomicInteger();
		config.getKeys(false).forEach(v -> {
			ConfigurationSection section = config.getConfigurationSection(v);
			((ItemsManagerImplementation) Core.getServer().getItems()).registerInterfaceElement(v, (short) section.getInt("Ikona"), section.getString("Nazwa"), section.getStringList("Lore"));
			counter.addAndGet(1);
		});

		int count = counter.get();
		if(count != 0){
			String word = Parsers.getProperForm(count, " element interfejsu", " elementy interfejsu", " element�w interfejsu");
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(count), word);
		}
	}
}
