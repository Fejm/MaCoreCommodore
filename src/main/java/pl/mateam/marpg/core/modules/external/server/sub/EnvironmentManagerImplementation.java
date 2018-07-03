package pl.mateam.marpg.core.modules.external.server.sub;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.bukkit.ChatColor;

import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.CommodoreEffect;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.classes.observers.ExtendedPlayerActionLocalObserver;
import pl.mateam.marpg.api.regular.classes.observers.ExtendedPlayerActionObserver;
import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.modules.sub.server.EnvironmentManager;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.internal.enums.BonusType;
import pl.mateam.marpg.core.objects.users.implementations.ExtendedPlayerImplementation.PvPOpponentObserver;
import pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer.InteractionWithAnotherPlayerInventory.ObserverInventoryActionLocalObserver;
import pl.mateam.marpg.core.objects.users.implementations.inventories.extendedplayer.InteractionWithAnotherPlayerInventory.ObserverInventoryActionObserver;


public class EnvironmentManagerImplementation implements EnvironmentManager {
	private boolean isRegistrationAllowed;
	
	private Set<BonusInfo> offensiveBonuses = new HashSet<>();
	private Set<BonusInfo> defensiveBonuses = new HashSet<>();
	private Set<BonusInfo> specialBonuses = new HashSet<>();
	
	private Map<String, BonusInfo> fullInfo = new HashMap<>();
	
	private int startOfTheNightHour, endOfTheNightHour;
	private int messageSendingDelayAtNight, messageSendingDelayAtDay;
	
	private String[] chatMessagePrefixes = new String[ChatChanel.values().length];
	
	private int characterSavingFrequencyInMinutes = 30;
	
	
	public EnvironmentManagerImplementation() {
		localObserverInstances.put(ObserverInventoryActionObserver.class, new ObserverInventoryActionObserver());
		localObserverInstances.put(PvPOpponentObserver.class, new PvPOpponentObserver());
		localLocalObserverInstances.put(ObserverInventoryActionLocalObserver.class, new ObserverInventoryActionLocalObserver());
	}
	
	
	
	@Secret public void removeBonusInfo(String nbtKey) {
		BonusInfo info = fullInfo.remove(nbtKey);
		if(info != null) {
			switch(info.type){
				case OFFENSIVE:	offensiveBonuses.remove(info);	break;
				case DEFENSIVE:	defensiveBonuses.remove(info);	break;
				case SPECIAL:	specialBonuses.remove(info);	break;
				default:		break;
			}
		}
	}
	
	@Secret public void putBonusInfo(BonusType type, String nbtKey, String description) {
		removeBonusInfo(nbtKey);
		BonusInfo info = new BonusInfo(type, nbtKey, description);
		fullInfo.put(nbtKey, info);
		if(info != null) {
			switch(info.type){
				case OFFENSIVE:	offensiveBonuses.add(info);		break;
				case DEFENSIVE:	defensiveBonuses.add(info);		break;
				case SPECIAL:	specialBonuses.add(info);		break;
				default:		break;
			}
		}
	}
	
	private Map<String, Class<? extends CommodoreInventory>> remoteAccessInventories = new HashMap<>();
 	@Secret public boolean registerInventory(String remoteName, Class<? extends CommodoreInventory> inventoryClass) {
 		Class<? extends CommodoreInventory> currentClass = remoteAccessInventories.get(remoteName); 
		if(currentClass != null) {
			CoreUtils.io.sendConsoleMessageWarningWithHighlight("Klasa ekwipunku dostępu zdalnego, ", inventoryClass.getName(), ", o kluczu "
		+ CoreUtils.chat.getConsoleColorWarningHighlighted() + remoteName + CoreUtils.chat.getConsoleColorWarning() + " nie może zostać zarejestrowana!");
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("Taki sam klucz posiada klasa ", currentClass.getName(), ".");
			return false;
		} else remoteAccessInventories.put(remoteName, inventoryClass);
		return true;
	}
 	@Secret public Class<? extends CommodoreInventory> getRemoteInventory(String key)	{	return remoteAccessInventories.get(key);	}
 	


	private Map<String, Class<? extends CommodoreEffect>> remoteAccessEffects = new HashMap<>();
 	@Secret public boolean registerEffect(String remoteName, Class<? extends CommodoreEffect> effectClass) {
 		Class<? extends CommodoreEffect> currentClass = remoteAccessEffects.get(remoteName); 
		if(currentClass != null) {
			CoreUtils.io.sendConsoleMessageWarningWithHighlight("Klasa efektu dostępu zdalnego, ", effectClass.getName(), ", o kluczu "
		+ CoreUtils.chat.getConsoleColorWarningHighlighted() + remoteName + CoreUtils.chat.getConsoleColorWarning() + " nie może zostać zarejestrowana!");
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("Taki sam klucz posiada klasa ", currentClass.getName(), ".");
			return false;
		} else remoteAccessEffects.put(remoteName, effectClass);
		return true;
	}
 	@Secret public Class<? extends CommodoreEffect> getRemoteEffect(String key)		{	return remoteAccessEffects.get(key);	}
 	
 	
 	private final Map<String, Map<Class<? extends ExtendedPlayerActionObserver>, ExtendedPlayerActionObserver>> remoteAccessObserverInstances = new HashMap<>();
 	private final Map<Class<? extends ExtendedPlayerActionObserver>, ExtendedPlayerActionObserver> localObserverInstances = new HashMap<>();

 	
	@Secret public boolean registerExtendedPlayerActionObserver(String remoteName, Class<? extends ExtendedPlayerActionObserver> observerClass) {
		Map<Class<? extends ExtendedPlayerActionObserver>, ExtendedPlayerActionObserver> currentClass = remoteAccessObserverInstances.get(remoteName); 
		if(currentClass != null) {
			CoreUtils.io.sendConsoleMessageWarningWithHighlight("Klasa obserwatora Commodore'owego gracza dostępu zdalnego, ", observerClass.getName(), ", o kluczu "
		+ CoreUtils.chat.getConsoleColorWarningHighlighted() + remoteName + CoreUtils.chat.getConsoleColorWarning() + " nie może zostać zarejestrowana!");
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("Taki sam klucz posiada klasa ", currentClass.get(observerClass).getClass().getName(), ".");
		} else {
			try {
				observerClass.getConstructor((Class<?>) null).setAccessible(true);
				ExtendedPlayerActionObserver observerInstance = observerClass.newInstance();
				Map<Class<? extends ExtendedPlayerActionObserver>, ExtendedPlayerActionObserver> map = remoteAccessObserverInstances.put(remoteName, new HashMap<>());
				map.put(observerClass, observerInstance);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Secret public ExtendedPlayerActionObserver getExtendedPlayerActionObserver(Class<? extends ExtendedPlayerActionObserver> observerClass) {
		return localObserverInstances.get(observerClass);
	}
	
	@Secret public ExtendedPlayerActionObserver getExtendedPlayerActionObserver(String observerName) {
		Map<Class<? extends ExtendedPlayerActionObserver>, ExtendedPlayerActionObserver> classMap = remoteAccessObserverInstances.get(observerName);
		if(classMap != null)
			return classMap.values().iterator().next();
		return null;
	}
	
	
 	private final Map<String, Map<Class<? extends ExtendedPlayerActionLocalObserver>, ExtendedPlayerActionLocalObserver>> remoteAccessLocalObserverInstances = new HashMap<>();
 	private final Map<Class<? extends ExtendedPlayerActionLocalObserver>, ExtendedPlayerActionLocalObserver> localLocalObserverInstances = new HashMap<>();

 	
	@Secret public boolean registerExtendedPlayerActionLocalObserver(String remoteName, Class<? extends ExtendedPlayerActionLocalObserver> observerClass) {
		Map<Class<? extends ExtendedPlayerActionLocalObserver>, ExtendedPlayerActionLocalObserver> currentClass = remoteAccessLocalObserverInstances.get(remoteName); 
		if(currentClass != null) {
			CoreUtils.io.sendConsoleMessageWarningWithHighlight("Klasa lokalnego obserwatora Commodore'owego gracza dostępu zdalnego, ", observerClass.getName(), ", o kluczu "
		+ CoreUtils.chat.getConsoleColorWarningHighlighted() + remoteName + CoreUtils.chat.getConsoleColorWarning() + " nie może zostać zarejestrowana!");
			CoreUtils.io.sendConsoleMessageSuccessWithHighlight("Taki sam klucz posiada klasa ", currentClass.get(observerClass).getClass().getName(), ".");
		} else {
			try {
				observerClass.getConstructor((Class<?>) null).setAccessible(true);
				ExtendedPlayerActionLocalObserver observerInstance = observerClass.newInstance();
				Map<Class<? extends ExtendedPlayerActionLocalObserver>, ExtendedPlayerActionLocalObserver> map = remoteAccessLocalObserverInstances.put(remoteName, new HashMap<>());
				map.put(observerClass, observerInstance);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}
	
	@Secret public ExtendedPlayerActionLocalObserver getExtendedPlayerLocalActionObserver(Class<? extends ExtendedPlayerActionLocalObserver> observerClass) {
		return localLocalObserverInstances.get(observerClass);
	}
	
	@Secret public ExtendedPlayerActionLocalObserver getExtendedPlayerLocalActionObserver(String observerName) {
		Map<Class<? extends ExtendedPlayerActionLocalObserver>, ExtendedPlayerActionLocalObserver> classMap = remoteAccessLocalObserverInstances.get(observerName);
		if(classMap != null)
			return classMap.values().iterator().next();
		return null;
	}

 
 
	
	@Secret public BonusInfo getBonus(String key)						{	return fullInfo.get(key);							}			
	@Secret public Set<BonusInfo> getOffensiveBonuses()					{	return new HashSet<>(offensiveBonuses);				}
	@Secret public Set<BonusInfo> getDefensiveBonuses()					{	return new HashSet<>(defensiveBonuses);				}
	@Secret public Set<BonusInfo> getSpecialBonuses()					{	return new HashSet<>(specialBonuses);				}

	
	@Override public boolean isRegistrationAllowed() 					{	return isRegistrationAllowed;						}
	@Override public void setRegistrationAllowed(boolean state) 		{	this.isRegistrationAllowed = state;					}
	
	@Override public int getStartOfTheNightHour() 						{	return startOfTheNightHour;							}
	@Override public int getEndOfTheNightHour() 						{	return endOfTheNightHour;							}

	@Override public void setStartOfTheNightHour(int hour)				{	this.startOfTheNightHour = hour;					}
	@Override public void setEndOfTheNightHour(int hour)				{	this.endOfTheNightHour = hour;						}
	
	@Override public String getChatMessagePrefix(ChatChanel chanel)		{	return chatMessagePrefixes[chanel.ordinal()];		}
	
	@Override public void setChatMessagePrefix(ChatChanel chanel, String prefix) {
		chatMessagePrefixes[chanel.ordinal()] = prefix;;
	}
	
	
	@Override public int getMessageSendingDelayAtNightInTicks() 		{	return messageSendingDelayAtNight;					}
	@Override public int getMessageSendingDelayAtDayInTicks() 			{	return messageSendingDelayAtDay;					}

	@Override public void setMessageSendingDelayAtNight(int inSeconds) 	{	messageSendingDelayAtNight = inSeconds;				}
	@Override public void setMessageSendingDelayAtDay(int inSeconds) 	{	messageSendingDelayAtDay = inSeconds;				}
	
	@Override public int getCurrentMessageSendingDelayInSeconds() {
		return CoreUtils.ingame.isDay()? getMessageSendingDelayAtDayInTicks() : getMessageSendingDelayAtNightInTicks();
	}
	
	@Override public void setCharacterInfoSavingFrequency(int inMinutes) {
		characterSavingFrequencyInMinutes = inMinutes;
	}
	
	@Override public int getCharacterInfoSavingFrequencyInTicks()	{	return characterSavingFrequencyInMinutes * 1200;	}
	
	
	public static class BonusInfo {		
		public final String key;
		public final BonusType type;
		public final String description;
		
		public BonusInfo(BonusType type, String key, String description) {
			this.key = key;
			this.type = type;
			this.description = description;
		}
		
		public String getColoredDescription() {
			switch(type){
				case OFFENSIVE:	return ChatColor.DARK_AQUA.toString() + description;
				case DEFENSIVE:	return ChatColor.AQUA.toString() + description;
				case SPECIAL:	return ChatColor.BLUE.toString() + description;
				default:		return null;
			}
		}
	}
}
