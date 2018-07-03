package pl.mateam.marpg.core.modules.external.files;

import java.lang.reflect.Method;
import java.util.HashMap;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.CommodoreComponent.ConfigurationFiles;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader.ReloaderName;

class AddOnConfigurationStorage {
	private static class ErrorsInfo {
		private boolean errorOccured;
		private boolean annotationErrorOccured;
		private boolean constructorErrorOccured;
	}
	
	private final CommodoreComponent component;
	private final HashMap<String, CommodoreConfigurationReloader> configuration = new HashMap<>();
			
	AddOnConfigurationStorage(CommodoreComponent component){
		this.component = component;
	}
			
	void add(Class<? extends CommodoreConfigurationReloader> file, ErrorsInfo errorsInfo){
		boolean single = errorsInfo == null;
		errorsInfo = errorsInfo == null? new ErrorsInfo() : errorsInfo;
		try{
			CommodoreConfigurationReloader instance = file.newInstance();
			if(file.isAnnotationPresent(ReloaderName.class)){
				String key = file.getAnnotation(ReloaderName.class).name();
				CommodoreConfigurationReloader previousInstance = configuration.put(key, instance);
				if(previousInstance != null && previousInstance.getClass() != file){
					if(!errorsInfo.errorOccured){
						CoreUtils.io.sendConsoleMessageWarning("Plugin " + component.getName() + ":");
						errorsInfo.errorOccured = true;
					}
					CoreUtils.io.sendConsoleMessageWarning("W rejestrze znajdowa� si� ju� plik konfiguracyjny o kluczu " + key + "!");
					CoreUtils.io.sendConsoleMessageWarning("Skr�cone nazwy klas rejestrator�w:");
					CoreUtils.io.sendConsoleMessageWarning("  - Stara: " + previousInstance.getClass().getSimpleName());
					CoreUtils.io.sendConsoleMessageWarning("  - Nowa: " + file.getSimpleName());
					CoreUtils.io.sendConsoleMessageWarning("Nowy plik konfiguracyjny NIE zosta� dodany do rejestru.");
					return;
				}
			} else {
				if(!errorsInfo.errorOccured)
					CoreUtils.io.sendConsoleMessageWarning("Plugin " + component.getName() + ":");
				errorsInfo.errorOccured = true;
				errorsInfo.annotationErrorOccured = true;
				CoreUtils.io.sendConsoleMessageWarning("- Klasa pliku konfiguracyjnego: " + file.getName() + " - brak adnotacji @ReloaderName!");
			}
		} catch(Exception e){
			if(!errorsInfo.errorOccured)
				CoreUtils.io.sendConsoleMessageWarning("Plugin " + component.getName() + ":");
			errorsInfo.errorOccured = true;
			errorsInfo.constructorErrorOccured = true;						
			CoreUtils.io.sendConsoleMessageWarning("- Klasa pliku konfiguracyjnego " + file.getName() + " posiada w�asny konstruktor!");
		}
		if(single){
			if(errorsInfo.annotationErrorOccured)
				CoreUtils.io.sendConsoleMessageWarning("Adnotacja @ReloaderName jest wymagana jako nag��wek klasy pliku konfiguracyjnego.");
			if(errorsInfo.constructorErrorOccured)
				CoreUtils.io.sendConsoleMessageWarning("Klasy plik�w konfiguracyjnych nie mog� definiowa� w�asnego konstruktora!");
		}
	}
	
	int getSize()	{	return configuration.size();	}
	
	boolean remove(String fileName){
		configuration.remove(fileName);
		return configuration.size() == 0;
	}
	
	private Class<? extends CommodoreConfigurationReloader>[] getAssociatedConfigurationClasses(CommodoreComponent component){
		try {
			Method method = component.getClass().getDeclaredMethod("turnedOn", (Class<?>[]) null);
			method.setAccessible(true);
			if(method.isAnnotationPresent(ConfigurationFiles.class))
				return method.getAnnotation(ConfigurationFiles.class).configurationClasses();
		} catch (Exception e){
			e.printStackTrace();
		}
		return null;
	}

	void addAll(){
		Class<? extends CommodoreConfigurationReloader>[] configs = getAssociatedConfigurationClasses(component);
		if(configs != null){
			ErrorsInfo info = new ErrorsInfo();
			for(Class<? extends CommodoreConfigurationReloader> config : configs)
				add(config, info);
			if(info.annotationErrorOccured)
				CoreUtils.io.sendConsoleMessageWarning("Adnotacja @ReloaderName jest wymagana jako nag��wek klasy pliku konfiguracyjnego.");
			if(info.constructorErrorOccured)
				CoreUtils.io.sendConsoleMessageWarning("Klasy plik�w konfiguracyjnych nie mog� definiowa� w�asnego konstruktora!");
		}
	}
	
	void reload(String configurationKey){
		CommodoreConfigurationReloader config = configuration.get(configurationKey);
		if(config != null)
			config.action();
	}
	void reloadAll(){
		configuration.values().forEach(config -> config.action());
	}
}
