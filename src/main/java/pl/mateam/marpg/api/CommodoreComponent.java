package pl.mateam.marpg.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import pl.mateam.marpg.api.regular.classes.CommodoreBonus;
import pl.mateam.marpg.api.regular.classes.CommodoreConfigurationReloader;
import pl.mateam.marpg.api.regular.classes.CommodoreEffect;
import pl.mateam.marpg.api.regular.classes.commands.CommodoreCommand;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.classes.observers.ExtendedPlayerActionLocalObserver;
import pl.mateam.marpg.api.regular.classes.observers.ExtendedPlayerActionObserver;

/*
 * MaCoreCommodore extends CommodoreComponent so it can create files/commands registries before using them.
 * Anyway all add-ons should extend CommodoreAddOn class to be recognised as MaCoreCommodore extension. 
 */
public abstract class CommodoreComponent extends JavaPlugin {
	@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
	public @interface Bonuses 									{	Class<? extends CommodoreBonus>[] bonusesClasses(); 						}
	
	@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
	public @interface CommodoreCommands 						{	Class<? extends CommodoreCommand>[] commandClasses(); 						}

	@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
	public @interface ConfigurationFiles 						{	Class<? extends CommodoreConfigurationReloader>[] configurationClasses(); 	}
	
	@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
	public @interface Inventories								{	Class<? extends CommodoreInventory>[] inventoryClasses();					}
	
	@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
	public @interface Effects									{	Class<? extends CommodoreEffect>[] effectClasses();							}

	@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
	public @interface DatabaseEntities							{	Class<?>[] entityClasses();													}
	
	@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
	public @interface ExtendedPlayerActionObservers				{	Class<? extends ExtendedPlayerActionObserver>[] observerClasses();			}

	@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
	public @interface ExtendedPlayerActionLocalObservers		{	Class<? extends ExtendedPlayerActionLocalObserver>[] observerClasses();		}

	@Target(ElementType.METHOD) @Retention(RetentionPolicy.RUNTIME)
	public @interface ResourcesFolder							{	String path(); 																}

	protected abstract void turnedOn();
	protected abstract void turnedOff();

	
	
	

	public void regenerateResources() 																	{	Core.communicate().unpackResources(this, false);						}
	public void regenerateResource(String relativePath)													{	Core.communicate().unpackResource(this, relativePath, false);			}
	public void restoreOriginalResource(String relativePath)											{	Core.communicate().unpackResource(this, relativePath, true);			}
	public void restoreOriginalResources()																{	Core.communicate().unpackResources(this, true);							}

	public YamlConfiguration getConfig(String relativePathToFile)										{	return Core.getFiles().getConfig(this, relativePathToFile);				}
	public void reloadConfig(Class<? extends CommodoreConfigurationReloader> reloaderClass)				{	Core.getFiles().reloadConfiguration(this, reloaderClass);				}
	public void reloadConfig(String reloaderKey)														{	Core.getFiles().reloadConfiguration(this.getName(), reloaderKey);		}
	public void reloadConfiguration()																	{	Core.getFiles().reloadConfiguration(this.getName());					}

	
	
	public static abstract class CommodoreAddOn extends CommodoreComponent {
		@Override public final void onEnable(){
			if(Core.communicate().startEnabling(this)){
				turnedOn();
				Core.communicate().enablingFinished(this);
			}
		}
		
		@Override public final void onDisable(){
			if(Core.communicate().startDisabling(this)){
				turnedOff();
				Core.communicate().disablingFinished(this);
			}
		}
	}
}
