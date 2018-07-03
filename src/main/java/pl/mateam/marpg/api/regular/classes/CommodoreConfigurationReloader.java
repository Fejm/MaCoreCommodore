package pl.mateam.marpg.api.regular.classes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import pl.mateam.marpg.api.utils.DeveloperUtils.ReflectiveField;

public abstract class CommodoreConfigurationReloader {
	@ReflectiveField(purpose = "setup configuration reloader key")
	private String key;								//Set using @ReloaderName annotation
	public final String getKey()	{	return key;		}
	
	public abstract void action();
	
	@Target(ElementType.TYPE) @Retention(RetentionPolicy.RUNTIME)
	//It is used to set "key" property of CommodoreConfigurationFile to be able to reload it later
	public @interface ReloaderName	{	String name();	}
}
