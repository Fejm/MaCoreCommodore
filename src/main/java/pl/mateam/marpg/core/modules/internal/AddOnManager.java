package pl.mateam.marpg.core.modules.internal;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.CommodoreComponent.Bonuses;
import pl.mateam.marpg.api.CommodoreComponent.CommodoreAddOn;
import pl.mateam.marpg.api.CommodoreComponent.Effects;
import pl.mateam.marpg.api.CommodoreComponent.ExtendedPlayerActionLocalObservers;
import pl.mateam.marpg.api.CommodoreComponent.ExtendedPlayerActionObservers;
import pl.mateam.marpg.api.CommodoreComponent.Inventories;
import pl.mateam.marpg.api.CommodoreComponent.ResourcesFolder;
import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.CoreUtils.RemoteAccessName;
import pl.mateam.marpg.api.regular.classes.CommodoreBonus;
import pl.mateam.marpg.api.regular.classes.CommodoreBonus.CommodoreDefensiveBonus;
import pl.mateam.marpg.api.regular.classes.CommodoreBonus.CommodoreOffensiveBonus;
import pl.mateam.marpg.api.regular.classes.CommodoreBonus.CommodoreSpecialBonus;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.classes.observers.ExtendedPlayerActionLocalObserver;
import pl.mateam.marpg.api.regular.classes.observers.ExtendedPlayerActionObserver;
import pl.mateam.marpg.api.regular.classes.CommodoreEffect;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.internal.enums.BonusType;
import pl.mateam.marpg.core.internal.utils.Parsers;
import pl.mateam.marpg.core.modules.external.database.DatabaseManager;
import pl.mateam.marpg.core.modules.external.files.FilesImplementation;
import pl.mateam.marpg.core.modules.external.server.sub.EnvironmentManagerImplementation;

public class AddOnManager {
	
	private Map<CommodoreComponent, AddOnProperties> enabledAddOns = new HashMap<>();
	
	public static class AddOnProperties {
		private String resourcesFolder = null;
		private long enablingStartTime = System.nanoTime();				//-1 means that plugin is already running
		public String getResourcesFolderName()					{	return resourcesFolder;				}
		public boolean stillEnabling()							{	return enablingStartTime != -1;		}
	}
	
	@Secret public boolean startEnabling(CommodoreComponent component){
		if(!(component instanceof CommodoreAddOn || component instanceof MaCoreCommodoreEngine)){
			CoreUtils.io.sendConsoleMessageWarningWithHighlight("Dodatek ", component.getName(), " rozszerza klasę CommodoreComponent, a nie CommodoreAddOn!");
			CoreUtils.io.sendConsoleMessageWarning("Ładowanie zatrzymane.");
			return false;
		}
		if(enabledAddOns.containsKey(component))
			return false;
		else {
			AddOnProperties info = new AddOnProperties();
			CoreUtils.io.sendConsoleMessageImportant("");
			if(enabledAddOns.size() == 0)
				CoreUtils.io.sendConsoleMessageImportant("Uruchamianie MaCoreCommodore...");
			else
				CoreUtils.io.sendConsoleMessageImportant("Uruchamianie modułu " + component.getName() + "...");
			info.resourcesFolder = deriveResourcesFolderName(component);
			enabledAddOns.put(component, info);
			registerBonuses(component);
			addConfigurators(component);
			registerCommands(component);
			registerInventories(component);
			registerEffects(component);
			registerExtendedPlayerActionObservers(component);
			registerExtendedPlayerLocalActionObservers(component);
			((DatabaseManager) Core.getDatabase()).mapClasses(component);
			return true;
		}
	}
	private void addConfigurators(CommodoreComponent component){
		((FilesImplementation) Core.getFiles()).addAllConfiguratorsToRegistry(component);
		int amount = Core.getFiles().getReloadersAmount(component);
		if(amount != 0){
			String word = Parsers.getProperForm(amount, " moduł", " moduły", " modułów");
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Dodano ", String.valueOf(amount), word + " reloadera");
		}
	}
	private void registerCommands(CommodoreComponent component){
		Core.getServer().getCommands().registerAll(component);
		int amount = Core.getServer().getCommands().getCommandsCount(component);
		if(amount != 0){
			String word = Parsers.getProperForm(amount, " komendę", " komendy", " komend");
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(amount), word);
		}
	}
	private void registerInventories(CommodoreComponent component) {
		try {
			EnvironmentManagerImplementation environment = (EnvironmentManagerImplementation) Core.getServer().getEnvironment();
			Method method = component.getClass().getDeclaredMethod("turnedOn", (Class<?>[]) null);
			method.setAccessible(true);
			int counter = 0;
			if(method.isAnnotationPresent(Inventories.class)){
				Class<? extends CommodoreInventory>[] inventoryClasses = method.getAnnotation(Inventories.class).inventoryClasses();
				for(Class<? extends CommodoreInventory> inventoryClass : inventoryClasses) {
					if(inventoryClass.isAnnotationPresent(RemoteAccessName.class)){
						String remoteName = inventoryClass.getAnnotation(RemoteAccessName.class).name();
						if(environment.registerInventory(remoteName, inventoryClass))
							counter++;
					} else
						CoreUtils.io.sendConsoleMessageWarningWithHighlight("Klasa ekwipunku dostępu zdalnego, ", inventoryClass.getName(), " nie posiada klucza dostępu!");
				}
				if(counter != 0){
					String word = Parsers.getProperForm(counter, " ekwipunek dostępu zdalnego", " ekwipunki dostępu zdalnego", " ekwipunków dostępu zdalnego");
					CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(counter), word);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void registerEffects(CommodoreComponent component) {
		try {
			EnvironmentManagerImplementation environment = (EnvironmentManagerImplementation) Core.getServer().getEnvironment();
			Method method = component.getClass().getDeclaredMethod("turnedOn", (Class<?>[]) null);
			method.setAccessible(true);
			int counter = 0;
			if(method.isAnnotationPresent(Effects.class)){
				Class<? extends CommodoreEffect>[] effectClasses = method.getAnnotation(Effects.class).effectClasses();
				for(Class<? extends CommodoreEffect> effectClass : effectClasses) {
					if(effectClass.isAnnotationPresent(RemoteAccessName.class)){
						String remoteName = effectClass.getAnnotation(RemoteAccessName.class).name();
						if(environment.registerEffect(remoteName, effectClass))
							counter++;
					} else
						CoreUtils.io.sendConsoleMessageWarningWithHighlight("Klasa efektu dostępu zdalnego, ", effectClass.getName(), " nie posiada klucza dostępu!");
				}
				if(counter != 0){
					String word = Parsers.getProperForm(counter, " efekt dostępu zdalnego", " efekty dostępu zdalnego", " efektów dostępu zdalnego");
					CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(counter), word);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void registerExtendedPlayerActionObservers(CommodoreComponent component) {
		try {
			EnvironmentManagerImplementation environment = (EnvironmentManagerImplementation) Core.getServer().getEnvironment();
			Method method = component.getClass().getDeclaredMethod("turnedOn", (Class<?>[]) null);
			method.setAccessible(true);
			int counter = 0;
			if(method.isAnnotationPresent(ExtendedPlayerActionObservers.class)){
				Class<? extends ExtendedPlayerActionObserver>[] observerClasses = method.getAnnotation(ExtendedPlayerActionObservers.class).observerClasses();
				for(Class<? extends ExtendedPlayerActionObserver> observerClass : observerClasses) {
					if(observerClass.isAnnotationPresent(RemoteAccessName.class)){
						String remoteName = observerClass.getAnnotation(RemoteAccessName.class).name();
						if(environment.registerExtendedPlayerActionObserver(remoteName, observerClass))
							counter++;
					} else
						CoreUtils.io.sendConsoleMessageWarningWithHighlight("Klasa obserwatora akcji Commodore'owego gracza dostępu zdalnego, ", observerClass.getName(), " nie posiada klucza dostępu!");
				}
				if(counter != 0){
					String word = Parsers.getProperForm(counter, " obserwator akcji Commodore'owych obiektów graczy", " obserwatory akcji Commodore'owych obiektów graczy",
							" obserwatorów akcji Commodore'owych obiektów graczy");
					CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(counter), word);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void registerExtendedPlayerLocalActionObservers(CommodoreComponent component) {
		try {
			EnvironmentManagerImplementation environment = (EnvironmentManagerImplementation) Core.getServer().getEnvironment();
			Method method = component.getClass().getDeclaredMethod("turnedOn", (Class<?>[]) null);
			method.setAccessible(true);
			int counter = 0;
			if(method.isAnnotationPresent(ExtendedPlayerActionLocalObservers.class)){
				Class<? extends ExtendedPlayerActionLocalObserver>[] observerClasses = method.getAnnotation(ExtendedPlayerActionLocalObservers.class).observerClasses();
				for(Class<? extends ExtendedPlayerActionLocalObserver> observerClass : observerClasses) {
					if(observerClass.isAnnotationPresent(RemoteAccessName.class)){
						String remoteName = observerClass.getAnnotation(RemoteAccessName.class).name();
						if(environment.registerExtendedPlayerActionLocalObserver(remoteName, observerClass))
							counter++;
					} else
						CoreUtils.io.sendConsoleMessageWarningWithHighlight("Klasa obserwatora akcji wewnętrznych Commodore'owego gracza dostępu zdalnego, ", observerClass.getName(), " nie posiada klucza dostępu!");
				}
				if(counter != 0){
					String word = Parsers.getProperForm(counter, " obserwator akcji wewnętrznych Commodore'owych obiektów graczy", " obserwatory akcji wewnętrznych Commodore'owych obiektów graczy",
							" obserwatorów akcji wewnętrznych Commodore'owych obiektów graczy");
					CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(counter), word);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void registerBonuses(CommodoreComponent component){
		CommodoreBonus[] bonuses = getBonusesInfo(component);
		if(bonuses != null){
			EnvironmentManagerImplementation environment = (EnvironmentManagerImplementation) Core.getServer().getEnvironment();
			for(CommodoreBonus bonus : bonuses){
				if(bonus instanceof CommodoreOffensiveBonus)
					environment.putBonusInfo(BonusType.OFFENSIVE, bonus.getNBTkey(), bonus.getLoreDescription());
				if(bonus instanceof CommodoreDefensiveBonus)
					environment.putBonusInfo(BonusType.DEFENSIVE, bonus.getNBTkey(), bonus.getLoreDescription());
				if(bonus instanceof CommodoreSpecialBonus)
					environment.putBonusInfo(BonusType.SPECIAL, bonus.getNBTkey(), bonus.getLoreDescription());
			}
			int amount = bonuses.length;
			if(amount != 0){
				String word = Parsers.getProperForm(amount, " bonus", " bonusy", " bonusów");
				CoreUtils.io.sendConsoleMessageSuccessWithHighlight("- Zarejestrowano ", String.valueOf(amount), word);
			}
		}
	}
	private void unregisterBonuses(CommodoreComponent component){
		CommodoreBonus[] bonuses = getBonusesInfo(component);
		if(bonuses != null){
			EnvironmentManagerImplementation environment = (EnvironmentManagerImplementation) Core.getServer().getEnvironment();
			for(CommodoreBonus bonus : bonuses)
				environment.removeBonusInfo(bonus.getNBTkey());
		}
	}
	private CommodoreBonus[] getBonusesInfo(CommodoreComponent component){
		try {
			Method method = component.getClass().getDeclaredMethod("turnedOn", (Class<?>[]) null);
			method.setAccessible(true);
			if(method.isAnnotationPresent(Bonuses.class)){
				Class<? extends CommodoreBonus>[] bonusClasses = method.getAnnotation(Bonuses.class).bonusesClasses();
				CommodoreBonus[] bonuses = new CommodoreBonus[bonusClasses.length];
				for(int i = 0; i < bonusClasses.length; i++)
					bonuses[i] = bonusClasses[i].getDeclaredConstructor().newInstance();
				return bonuses;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	@Secret public boolean startDisabling(CommodoreComponent component){
		if(!enabledAddOns.containsKey(component)){
			return false;
		} else {
			try{
				enabledAddOns.remove(component);
				((FilesImplementation) Core.getFiles()).removeAllConfiguratorsFromRegistry(component);
				Core.getServer().getCommands().unregisterAll(component);
				unregisterBonuses(component);
			/*
			 * Exceptions may be thrown because of concurrent command unregistering (Bukkit and MaCoreCommodore)
			 * As there is no way to check whether the server is being shutted down,
			 * I decided to do a little workaround to suppress exceptions.
			 */
			} catch(Exception e){}
			return true;
		}
	}
		
	@Secret public void enablingFinished(CommodoreComponent component){
		if(component instanceof MaCoreCommodoreEngine){
			enabledAddOns.get(component).enablingStartTime = -1;
			return;	//As MaCoreCommodore makes a few things before AddOnManager is set, it should measure its time itself.
		}
		double timeInSeconds = CoreUtils.math.round(((System.nanoTime() - enabledAddOns.get(component).enablingStartTime) / 1000000d)/1000, 3);
		CoreUtils.io.sendConsoleMessageImportant("");
		CoreUtils.io.sendConsoleMessageImportantWithHighlight("- " + component.getName() + ": załadowano! Zajęło to ", timeInSeconds + "s.", "");
		CoreUtils.io.sendConsoleMessageImportant("");
		CoreUtils.io.sendConsoleMessageImportant(MaCoreCommodoreEngine.BAR);

		enabledAddOns.get(component).enablingStartTime = -1;	//Tells that component got enabled
	}

	@Secret public void disablingFinished(CommodoreComponent component){
	}


	
	@Secret public void manageResources(CommodoreComponent addOn, boolean override){
		File destination = addOn.getDataFolder();
		String resourcesFolderName = enabledAddOns.get(addOn).getResourcesFolderName();
		Class<?> pluginClass = addOn.getClass();
		if(resourcesFolderName == null){
			CoreUtils.io.sendConsoleMessageWarning("Plugin " + addOn.getName() + ":");
			CoreUtils.io.sendConsoleMessageWarning("Próbowano wypakować pliki konfiguracyjne za pomocą metody unpackResources() jednak nie zdefiniowano ścieżki!"
													+ " Zapoznaj się z adnotacją @ResourcesFolder.");
			return;
		}
		destination.mkdirs();
		try {
			JarFile jarFile = new JarFile(pluginClass.getProtectionDomain().getCodeSource().getLocation().getPath());
			Enumeration<JarEntry> enu = jarFile.entries();
			int amount = 0;
			while(enu.hasMoreElements()){
			    JarEntry je = enu.nextElement();
			    String entryPath = je.getName();

			    if(!entryPath.startsWith(resourcesFolderName))
			    	continue;
			    entryPath = entryPath.substring(resourcesFolderName.length());
			
			    File fl = new File(destination, entryPath);
			    if(!fl.exists() || override){
			    	amount++;
			    	if(!override)
			    		fl.getParentFile().mkdirs();
			        if(je.isDirectory())
				        continue;
				    InputStream is = jarFile.getInputStream(je);
				    FileOutputStream fo = new FileOutputStream(fl);
				    while(is.available() > 0)
				        fo.write(is.read());
				    fo.close();
				    is.close();
			    }
			}
			jarFile.close();
			if(enabledAddOns.get(addOn).stillEnabling() && amount > 0){
				String prefix = override? "- Nadpisano " : "- Zregenerowano ";
				if(amount != 1){
					String word = Parsers.getProperForm(amount, " plik", " pliki", " plików");
					CoreUtils.io.sendConsoleMessageSuccessWithHighlight(prefix, String.valueOf(amount), word);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private String deriveResourcesFolderName(CommodoreComponent addOn){
		try {
			Method method = addOn.getClass().getDeclaredMethod("turnedOn", (Class<?>[]) null);
			method.setAccessible(true);
			if(method.isAnnotationPresent(ResourcesFolder.class))
				return method.getAnnotation(ResourcesFolder.class).path();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	@Secret public void manageResource(CommodoreComponent component, String relativePath, boolean override) {
		String resourcesFolderName = enabledAddOns.get(component).getResourcesFolderName();
		File destination = component.getDataFolder();
		destination.mkdirs();
		try {
			JarFile jarFile = new JarFile(getClass().getProtectionDomain().getCodeSource().getLocation().getPath());
			JarEntry je = jarFile.getJarEntry(resourcesFolderName + "/" + relativePath);
		    String entryPath = je.getName();
		    entryPath = entryPath.substring(resourcesFolderName.length());
		    File fl = new File(destination, entryPath);
		    
		    if(!fl.exists() || override){
		    	if(!override)
		    		fl.getParentFile().mkdirs();
		        if(je.isDirectory()){
		        	jarFile.close();
			        return;
		        }
			    InputStream is = jarFile.getInputStream(je);
			    FileOutputStream fo = new FileOutputStream(fl);
			    while(is.available() > 0)
			        fo.write(is.read());
			    fo.close();
			    is.close();
		    }

			if(fl.getParentFile() != null)
				fl.getParentFile().mkdirs();
			fl.createNewFile();
			InputStream is = jarFile.getInputStream(je);
			FileOutputStream fo = new FileOutputStream(fl);
			while(is.available() > 0)
				fo.write(is.read());
			fo.close();
			is.close();
			jarFile.close();
		} catch (IOException e) {
			e.printStackTrace();
		}		
	}
}
