package pl.mateam.marpg.core.modules.external.server.sub;

import java.util.Calendar;
import java.util.Date;

import org.bson.Document;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.events.PlayerBannedCommodoreEvent;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.globalban.GlobalBanDatabaseWriter;
import pl.mateam.marpg.api.regular.modules.sub.server.BansManager;
import pl.mateam.marpg.api.regular.objects.users.PlayerBasedUser;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.data.collections.embedded.players.BanInfo;
import pl.mateam.marpg.core.internal.utils.Parsers;
import pl.mateam.marpg.core.objects.users.UserBasedPlayerImplementation;

import com.mongodb.client.result.UpdateResult;

public class BansManagerImplementation implements BansManager {		
	@Override public void ban(String playername) 									{	ban(playername, -1, null);						}
	@Override public void ban(String playername, int timeInMinutes) 				{	ban(playername, timeInMinutes, null);			}
	@Override public void ban(String playername, String reason) 					{	ban(playername, -1, reason);					}
	@Override public void ban(String playername, int timeInMinutes, String reason) 	{	ban(playername, null, timeInMinutes, reason);	}
	@Override public void ban(String playername, String whobanned, String reason) 	{	ban(playername, whobanned, -1, reason);			}
	
	@Override public void ban(String playername, String whoBanned, int timeInMinutes, String reason) {
		Player player = Bukkit.getServer().getPlayer(playername);
		Calendar calendar = Calendar.getInstance();
		Date bannedDate = calendar.getTime();
		Date expirationDate = null;
		if(timeInMinutes > 0){
			calendar.add(Calendar.MINUTE, timeInMinutes);
			expirationDate = calendar.getTime();
		}
		if(player != null) {
			PlayerBasedUser user = Core.getServer().getUsers().getPlayerBasedUser(player);
			if(user != null){
				BanInfo ban = ((UserBasedPlayerImplementation) user).additionalInfo.getGlobalBanInfo();
				ban.banDate = bannedDate;
				ban.banExpirationTime = expirationDate;
				ban.whoBanned = whoBanned;
				ban.banReason = reason;
				player.kickPlayer(Parsers.getKickBanReason(reason, whoBanned, bannedDate, expirationDate));
				Bukkit.getPluginManager().callEvent(new PlayerBannedCommodoreEvent(playername, true));
			}
		}
		else {
			Document updateOperations = new Document();
			GlobalBanDatabaseWriter explorer = Core.getDatabase().getExplorer().getPlayerAccountCollection().getWriter(updateOperations).getBanInfo();
			if(timeInMinutes > 0)
				explorer.setExpirationDate(expirationDate);
			explorer.setBannedDate(bannedDate);
			if(reason != null)
				explorer.setReason(reason);
			if(whoBanned != null)
				explorer.setWhoBanned(whoBanned);
			
			Bukkit.getScheduler().runTaskAsynchronously(MaCoreCommodoreEngine.getReference(), () -> {
				UpdateResult result = Core.getDatabase().getExplorer().getPlayerAccountCollection().updateBySetting(playername, updateOperations);
				if(result.getModifiedCount() > 0)
					Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> Bukkit.getPluginManager().callEvent(new PlayerBannedCommodoreEvent(playername, false)));
			});
		}
	}
	
	private int dodacBanowanieCzatow;
	
	@Override public void unban(String playername) {
		Document updateOperations = new Document();
		GlobalBanDatabaseWriter explorer = Core.getDatabase().getExplorer().getPlayerAccountCollection().getWriter(updateOperations).getBanInfo();
		explorer.setBannedDate(null);
		explorer.setExpirationDate(null);
		explorer.setReason(null);
		explorer.setWhoBanned(null);
		Core.getDatabase().getExplorer().getPlayerAccountCollection().updateBySetting(playername, updateOperations);
	}
}
