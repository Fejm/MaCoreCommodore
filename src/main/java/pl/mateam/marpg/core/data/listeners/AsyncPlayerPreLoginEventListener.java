package pl.mateam.marpg.core.data.listeners;

import java.util.Date;
import java.util.concurrent.ConcurrentHashMap;

import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.core.CommodoreCore;
import pl.mateam.marpg.core.data.collections.EveryAccountEntity;
import pl.mateam.marpg.core.data.collections.GamemasterAccountEntity;
import pl.mateam.marpg.core.data.collections.PlayerAccountEntity;
import pl.mateam.marpg.core.data.collections.embedded.players.BanInfo;
import pl.mateam.marpg.core.internal.hardcoded.database.EveryAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.internal.hardcoded.database.GamemasterAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.internal.hardcoded.database.PlayerAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.internal.utils.DatabaseUtils;
import pl.mateam.marpg.core.internal.utils.Parsers;

public class AsyncPlayerPreLoginEventListener implements Listener {
	private static final String NOT_VALID_GAMEMASTER_IP =  
			  "    §4§lWYPIEERDALAAAJ STOOOOONTTTTT!!11cos2§r§4\u03C0\n\n\n§7§l\u2B1B\u2B1B§8§l\u2B1B\u2B1B\u2B1B§7§l\u2B1B§8§l\u2B1B§7§l\u2B1B"
			+ "                                                                   \n§8§l\u2B1B§7§l\u2B1B\u2B1B§8§l\u2B1B§7§l\u2B1B\u2B1B\u2B1B\u2B1B"
			+ "                                                                   \n§8§l\u2B1B§7§l\u2B1B\u2B1B\u2B1B§7§l\u2B1B\u2B1B\u2B1B§8§l\u2B1B"
			+ "                                                                   \n§7§l\u2B1B§0§l\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B§8§l\u2B1B"
			+ "          §6§lPróbowałeś zalogować się na konto Mistrza Gry. \n§7§l\u2B1B§0§l\u2B1B§4§l\u2B1B§0§l\u2B1B\u2B1B§4§l\u2B1B§0§l\u2B1B§8§l\u2B1B"
			+ "               §6§lTwoje konto zostało zabanowane, chuju.    \n§7§l\u2B1B§0§l\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B§7§l\u2B1B"
			+ "                                                                   \n§8§l\u2B1B§0§l\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B§7§l\u2B1B"
			+ "                                                                   \n§7§l\u2B1B§7§l\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B§7§l\u2B1B"
			+ "                                                                   ";
	private static final String REGISTRATION_NOT_ALLOWED =  
			  "    §4§lMOŻLIWOŚĆ TWORZENIA NOWYCH KONT ZOSTAŁA WYŁĄCZONA!\n\n\n§4§l\u2B1B§0§l\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B§4§l\u2B1B"
			+ "                                                                   \n§0§l\u2B1B§4§l\u2B1B§0§l\u2B1B\u2B1B\u2B1B\u2B1B§4§l\u2B1B§0§l\u2B1B"
			+ "                                                                   \n§0§l\u2B1B\u2B1B§4§l\u2B1B§0§l\u2B1B\u2B1B§4§l\u2B1B§0§l\u2B1B\u2B1B"
			+ "                                                                   \n§0§l\u2B1B\u2B1B\u2B1B§4§l\u2B1B\u2B1B§0§l\u2B1B\u2B1B\u2B1B"
			+ "         §6§lMożliwość rejestrowania kont została wyłączona. \n§0§l\u2B1B\u2B1B\u2B1B§4§l\u2B1B\u2B1B§0§l\u2B1B\u2B1B\u2B1B"
			+ "         §6§lOdwiedź forum, aby uzyskać więcej informacji.    \n§0§l\u2B1B\u2B1B§4§l\u2B1B§0§l\u2B1B\u2B1B§4§l\u2B1B§0§l\u2B1B\u2B1B"
			+ "                                                                   \n§0§l\u2B1B§4§l\u2B1B§0§l\u2B1B\u2B1B\u2B1B\u2B1B§4§l\u2B1B§0§l\u2B1B"
			+ "                                                                   \n§4§l\u2B1B§0§l\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B\u2B1B§4§l\u2B1B"
			+ "                                                                   ";

	private static ConcurrentHashMap<String, GamemasterInfoWrapper> gamemastersAccountsInfo = new ConcurrentHashMap<>();
	private static ConcurrentHashMap<String, RegularPlayerInfoWrapper> regularPlayersAccountsInfo = new ConcurrentHashMap<>();

	static GamemasterInfoWrapper getGamemasterAccountInfo(String playerName)	{	return gamemastersAccountsInfo.remove(playerName);		}
	static RegularPlayerInfoWrapper getRegularAccountInfo(String playerName)	{	return regularPlayersAccountsInfo.remove(playerName);	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void on(AsyncPlayerPreLoginEvent event){
		if(!(event.getLoginResult().equals(Result.ALLOWED)))
			return;
		
		String playerName = event.getName();
		
		EveryAccountEntity accountInfo = DatabaseUtils.getUsingSingleCriteria(EveryAccountEntityCollectionHardcodedNames.IDENTIFICATOR, playerName, EveryAccountEntity.class);
		
		if(CommodoreCore.getHiddenMethods().getHiddenData().gamemastersNames.contains(playerName)){
			GamemasterInfoWrapper wrapper = new GamemasterInfoWrapper();
			GamemasterAccountEntity gamemasterAccountInfo = DatabaseUtils.getUsingSingleCriteria(GamemasterAccountEntityCollectionHardcodedNames.IDENTIFICATOR, playerName, GamemasterAccountEntity.class);
			if(gamemasterAccountInfo == null || !gamemasterAccountInfo.latestIP.equals(event.getAddress().toString().replace("/", "")))
				event.disallow(Result.KICK_BANNED, NOT_VALID_GAMEMASTER_IP);
			else {
				wrapper.primaryInfo = accountInfo;
				wrapper.additionalInfo = gamemasterAccountInfo;
				gamemastersAccountsInfo.put(playerName, wrapper);
			}
		} else {
			RegularPlayerInfoWrapper wrapper = new RegularPlayerInfoWrapper();
			if(accountInfo == null) {
				if(Core.getServer().getEnvironment().isRegistrationAllowed()){
					accountInfo = new EveryAccountEntity(playerName);
					wrapper.additionalInfo = new PlayerAccountEntity(playerName);
				} else {
					event.disallow(Result.KICK_BANNED, REGISTRATION_NOT_ALLOWED);
					return;
				}
			} else {
				wrapper.additionalInfo = DatabaseUtils.getUsingSingleCriteria(PlayerAccountEntityCollectionHardcodedNames.IDENTIFICATOR, playerName, PlayerAccountEntity.class);
				if(wrapper.additionalInfo != null){
					BanInfo ban = wrapper.additionalInfo.getGlobalBanInfo();
					if(ban.banDate != null){
						if(ban.banExpirationTime != null && ban.banExpirationTime.before(new Date())){
							ban.clear();
							return;
						}
						String banFinalReason = Parsers.getKickBanReason(ban.banReason, ban.whoBanned, ban.banDate, ban.banExpirationTime);
						event.disallow(Result.KICK_BANNED, banFinalReason);
						return;
					}
				}
			}
			wrapper.primaryInfo = accountInfo;
			regularPlayersAccountsInfo.put(playerName, wrapper);
		}
	}
}
class GamemasterInfoWrapper {
	EveryAccountEntity primaryInfo;
	GamemasterAccountEntity additionalInfo;
}
class RegularPlayerInfoWrapper {
	EveryAccountEntity primaryInfo;
	PlayerAccountEntity additionalInfo;
}
