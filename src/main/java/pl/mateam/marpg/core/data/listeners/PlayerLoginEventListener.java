package pl.mateam.marpg.core.data.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerLoginEvent;

import pl.mateam.marpg.api.regular.events.GamemasterJoinedCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerJoinedCommodoreEvent;
import pl.mateam.marpg.core.objects.users.implementations.GamemasterImplementation;
import pl.mateam.marpg.core.objects.users.implementations.PlayerInLobbyImplementation;

public class PlayerLoginEventListener implements Listener {	
	@EventHandler(priority = EventPriority.LOWEST)	//Player's object should be initialized fastest as possible - so in LOWEST priority event
	public void on(PlayerLoginEvent event){
		Player player = event.getPlayer();
		String playerName = player.getName();
		
		GamemasterInfoWrapper wrappedGamemaster = AsyncPlayerPreLoginEventListener.getGamemasterAccountInfo(playerName);
		if(wrappedGamemaster != null){
			GamemasterImplementation gamemaster = new GamemasterImplementation(player, wrappedGamemaster.primaryInfo, wrappedGamemaster.additionalInfo);
			Bukkit.getPluginManager().callEvent(new GamemasterJoinedCommodoreEvent(gamemaster));
		}
		else {
			RegularPlayerInfoWrapper wrappedPlayer = AsyncPlayerPreLoginEventListener.getRegularAccountInfo(playerName);
			PlayerInLobbyImplementation playerInLobby = new PlayerInLobbyImplementation(player, wrappedPlayer.primaryInfo, wrappedPlayer.additionalInfo);
			Bukkit.getPluginManager().callEvent(new PlayerJoinedCommodoreEvent(playerInLobby));
		}
	}
}
