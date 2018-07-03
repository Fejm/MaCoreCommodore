package pl.mateam.marpg.core;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.apache.commons.lang.StringUtils;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.core.data.bonuses.defensive.BonusBarbariansResistance;
import pl.mateam.marpg.core.data.bonuses.defensive.BonusMagesResistance;
import pl.mateam.marpg.core.data.bonuses.defensive.BonusMobsResistance;
import pl.mateam.marpg.core.data.bonuses.defensive.BonusSlayersResistance;
import pl.mateam.marpg.core.data.bonuses.defensive.BonusWarriorsResistance;
import pl.mateam.marpg.core.data.bonuses.offensive.BonusBarbariansDamage;
import pl.mateam.marpg.core.data.bonuses.offensive.BonusMagesDamage;
import pl.mateam.marpg.core.data.bonuses.offensive.BonusMobsDamage;
import pl.mateam.marpg.core.data.bonuses.offensive.BonusSlayersDamage;
import pl.mateam.marpg.core.data.bonuses.offensive.BonusWarriorsDamage;
import pl.mateam.marpg.core.data.bonuses.special.BonusCritical;
import pl.mateam.marpg.core.data.bonuses.special.BonusDefenceBoost;
import pl.mateam.marpg.core.data.bonuses.special.BonusSkillsDamage;
import pl.mateam.marpg.core.data.bonuses.special.BonusSlowness;
import pl.mateam.marpg.core.data.collections.EveryAccountEntity;
import pl.mateam.marpg.core.data.collections.GamemasterAccountEntity;
import pl.mateam.marpg.core.data.collections.PlayerAccountEntity;
import pl.mateam.marpg.core.data.commands.ModificationGender;
import pl.mateam.marpg.core.data.commands.ModificationModerator;
import pl.mateam.marpg.core.data.commands.ModificationRank;
import pl.mateam.marpg.core.data.commands.Vanish;
import pl.mateam.marpg.core.data.configurators.GenericSettingsConfig;
import pl.mateam.marpg.core.data.configurators.InterfaceConfig;
import pl.mateam.marpg.core.data.configurators.ItemsConfig;
import pl.mateam.marpg.core.data.configurators.LocationsConfig;
import pl.mateam.marpg.core.data.configurators.MechanicsConfig;
import pl.mateam.marpg.core.data.configurators.MusicConfig;
import pl.mateam.marpg.core.data.configurators.RanksConfig;
import pl.mateam.marpg.core.data.configurators.SkinsConfig;
import pl.mateam.marpg.core.data.configurators.TectonicConfig;

/*
 * MaCoreCommodoreEngine class allows to distinguish MaCoreCommodore Initializer
 * from badly designed add-ons (which extends CommodoreComponent, not CommodoreAddOn)
 */	
public class MaCoreCommodoreEngine extends CommodoreComponent {
	//This annotation helps me to distinguish methods that won't be visible to add-ons developers
	@Target(ElementType.METHOD) @Retention(RetentionPolicy.SOURCE)
	public @interface Secret {}
	
	@Target(ElementType.FIELD) @Retention(RetentionPolicy.SOURCE)
	public @interface NullAtRuntime {}
	
	private static MaCoreCommodoreEngine singleton = null;
	public static MaCoreCommodoreEngine getReference()	{	return singleton;	}
	
	//Version info
	public final static String TITLE 					= "MaCoreCommodore - Reborn";
	public final static String RELEASE_DATE 			= "18.03.2018";
	
	public final static String BAR = StringUtils.repeat("*", "\\____/\\___/\\/    \\/\\/    \\/\\___/___,'\\___/\\/ \\_/\\__/".length());
	


	/* -----------
	 * Initializer
	 * ----------- */
	
	@Override public void onEnable(){
		singleton = this;
		turnedOn();
	}

	//All of these annotations refers to turnedOn() method!

	@Bonuses(bonusesClasses =						{ 	BonusBarbariansResistance.class, BonusMagesResistance.class, BonusMobsResistance.class, BonusSlayersResistance.class, BonusWarriorsResistance.class,
														BonusBarbariansDamage.class, BonusMagesDamage.class, BonusMobsDamage.class, BonusSlayersDamage.class, BonusWarriorsDamage.class,
														BonusCritical.class, BonusDefenceBoost.class, BonusSkillsDamage.class, BonusSlowness.class})
	
	@ConfigurationFiles(configurationClasses = 		{	GenericSettingsConfig.class, InterfaceConfig.class, ItemsConfig.class, LocationsConfig.class,
														MechanicsConfig.class, MusicConfig.class, RanksConfig.class, SkinsConfig.class, TectonicConfig.class })

	@CommodoreCommands(commandClasses = 			{ 	ModificationGender.class, ModificationModerator.class, ModificationRank.class, Vanish.class })
	
	@DatabaseEntities(entityClasses =				{ 	EveryAccountEntity.class, GamemasterAccountEntity.class, PlayerAccountEntity.class })
		
	@ResourcesFolder(path = "resources")

	@Override protected void turnedOn() 			{	new MaCoreCommodoreInitializer(this);	}
	
	
	


	/* ----------
	 * Shutdowner
	 * ---------- */
	
	@Override public void onDisable()		{	turnedOff();							}
	@Override protected void turnedOff() 	{	new MaCoreCommodoreShutdowner(this);	}
}
