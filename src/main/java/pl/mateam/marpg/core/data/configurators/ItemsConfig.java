package pl.mateam.marpg.core.data.configurators;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;
import pl.mateam.marpg.api.regular.enums.configuration.ConfigurationPath;
import pl.mateam.marpg.core.internal.enums.NBT_Attribute;
import pl.mateam.marpg.core.internal.helpers.ItemTypeInfo;
import pl.mateam.marpg.core.internal.utils.Parsers;
import pl.mateam.marpg.core.modules.external.server.sub.ItemsManagerImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.ItemsManagerImplementation.SpecialItemBuilder;



@ReloaderName(name = CONFIGURATION_RELOADER.ITEMS)
public class ItemsConfig extends CommodoreConfigurationReloader {
	@Override public void action() {
		ItemsManagerImplementation items = (ItemsManagerImplementation) Core.getServer().getItems();
				
		YamlConfiguration config = Core.getFiles().getConfig(ConfigurationPath.ITEMS);
		
		AtomicInteger counter = new AtomicInteger();
		config.getKeys(false).forEach(v -> {
			ConfigurationSection section = config.getConfigurationSection(v);
			
			short durability = (short) section.getInt("Durability");
			int value = section.getInt("Wartosc");
			String name = section.getString("Nazwa");
			List<String> lore = section.getStringList("Lore");
			
			HashMap<String, Object> itemMetadata = getItemMetadataFromConfig(section);
			
			if(section.contains("ID"))
				items.registerStackableItem(Integer.valueOf(v), section.getInt("ID"), durability, value, name, lore, itemMetadata);
			else
				items.registerUnstackableItem(Integer.valueOf(v), durability, value, name, lore, itemMetadata);

			counter.addAndGet(1);
		});

		int count = counter.get();
		String word = Parsers.getProperForm(count, " przedmiot", " przedmioty", " przedmiot�w");
		CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(count), word);
		
	
		YamlConfiguration config2 = Core.getFiles().getConfig(ConfigurationPath.SPECIAL_ITEMS);
		counter.set(0);
		config2.getKeys(false).forEach(v -> {
			ConfigurationSection mainSection = config2.getConfigurationSection(v);
			ConfigurationSection section = mainSection;
			
			SpecialItemBuilder builder;
			String type;
			if(section.contains("Grupa")) {
				section = mainSection.getConfigurationSection("Grupa");
				type = section.getString("Zastosowanie");
				short iconsStart = (short) section.getInt("StartIkon");
				int value = section.getInt("Wartosc");
				List<String> names = section.getStringList("Nazwy");
				List<String> lore = section.getStringList("Lore");
				HashMap<String, Object> itemsMetadata = getItemMetadataFromConfig(section);
				boolean regular = section.getBoolean("Modyfikowalny");
				int blockedClasses = getBlockedClassesNumber(section);
				builder = items.registerSpecialItemsGroup(Integer.valueOf(v), type, iconsStart, value, names, lore, itemsMetadata, regular, blockedClasses);
			} else {
				section = mainSection.getConfigurationSection("Pojedynczy");
				type = section.getString("Zastosowanie");
				short iconID = (short) section.getInt("Ikona");
				int value = section.getInt("Wartosc");
				String name = section.getString("Nazwa");
				List<String> lore = section.getStringList("Lore");
				HashMap<String, Object> itemMetadata = getItemMetadataFromConfig(section);
				boolean regular = section.getBoolean("Modyfikowalny");
				int blockedClasses = getBlockedClassesNumber(section);
				int level = section.getInt("WymaganyPoziom");
				builder = items.registerSingleSpecialItem(Integer.valueOf(v), type, iconID, value, name, lore, itemMetadata, regular, blockedClasses, level);
			}
			
			section = mainSection.getConfigurationSection("Wlasciwosci");
			switch(new ItemTypeInfo(type).assignToGroup()){
				case HANDWEAR:
					section.addDefault("SzybkoscAtaku", 4);
					section.addDefault("MnoznikObrazen", 1);
					section.addDefault("MnoznikUmiejetnosci", 1);
					double attackSpeed = section.getDouble("SzybkoscAtaku");
					builder.setAttackSpeed(attackSpeed);
					builder.setAttributeValue(NBT_Attribute.DAMAGE_MULTIPLIER, section.getDouble("MnoznikObrazen"));
					builder.setAttributeValue(NBT_Attribute.SKILL_DAMAGE_MULTIPLIER, section.getDouble("MnoznikUmiejetnosci"));
					builder.setAttributeValue(NBT_Attribute.DAMAGE_AMPLITUDE, section.getInt("ProcentWahaniaObrazen"));
					int mining = section.getInt("WydobywanieSurowcow");
					if(mining > 0)
						builder.setMining(mining);
					break;
				case ARMOR:
					builder.setAttributeValue(NBT_Attribute.DEFENCE_MULTIPLIER, section.getDouble("MnoznikObrony"));
					break;
				case SHIELD:
					builder.setAttributeValue(NBT_Attribute.BLOCKING_MULTIPLIER, section.getDouble("MnoznikBloku"));
					break;
				case JEWELLERY:
					builder.setAttributeValue(NBT_Attribute.REGENERATION_MULTIPLIER, section.getDouble("MnoznikRegeneracji"));
					builder.setAttackSpeed(4);
					break;
			}
			
			section = mainSection.getConfigurationSection("Bonusy");
			if(section != null){
				int offBon = section.getInt("SzansaNaOfensywny");
				int defBon = section.getInt("SzansaNaDefensywny");
				int spcBon = section.getInt("SzansaNaSpecjalny");
				builder.setBonusable(offBon, defBon, spcBon);
			}
						
			counter.addAndGet(1);
		});
		
		count = counter.get();
		word = Parsers.getProperForm(count, " specjalny przedmiot", " specjalne przedmioty", " specjalnych przedmiotów");
		CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(count), word);
	}
	
	private int getBlockedClassesNumber(ConfigurationSection section) {
		if(!section.contains("OgraniczeniaKlas"))
			return 0;
		String blockedClassesString = section.getString("OgraniczeniaKlas");
		return Integer.parseInt(blockedClassesString, 2);
	}
	
	private HashMap<String, Object> getItemMetadataFromConfig(ConfigurationSection section){
		List<String> metaList = section.getStringList("Metadane");
		HashMap<String, Object> itemMetadata = new HashMap<>();
		metaList.forEach(entry -> {
			String[] x = entry.trim().split(" ");
			String key = x[0];
			String mapValue = x[1];
			try {
				Boolean b = Boolean.parseBoolean(mapValue);
				itemMetadata.put(key, b);
			} catch(Exception e1){
				try {
					Double d = Double.parseDouble(mapValue);
					itemMetadata.put(key, d);
				} catch(Exception e2){
					try {
						Integer i = Integer.parseInt(mapValue);
						itemMetadata.put(key, i);
					} catch(Exception e3){
						try {
							Integer i = Integer.parseInt(mapValue);
							itemMetadata.put(key, i);
						} catch(Exception e4){
							itemMetadata.put(key, mapValue);
						}
					}
				}
			}
		});
		return itemMetadata;
	}
}
