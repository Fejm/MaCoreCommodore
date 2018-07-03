package pl.mateam.marpg.api.regular.modules.sub.server;

import java.util.Collection;

import org.bukkit.entity.Player;

import pl.mateam.marpg.api.regular.objects.users.AnyUser;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;
import pl.mateam.marpg.api.regular.objects.users.PlayerBasedUser;
import pl.mateam.marpg.api.regular.objects.users.PlayerInLobby;

public interface UsersManager {
	AnyUser getGenericObject(Player player);
	PlayerBasedUser getPlayerBasedUser(Player player);
	PlayerInLobby getPlayerInLobbyObject(Player player);
	ExtendedPlayer getExtendedPlayerObject(Player player);
	Gamemaster getGamemasterObject(Player player);

	Collection<AnyUser> getAllUsersObjects();
	Collection<Gamemaster> getAllGamemasterObjects();
	Collection<PlayerBasedUser> getAllPlayerBasedUsers();
	Collection<PlayerInLobby> getAllPlayersInLobby();
	Collection<ExtendedPlayer> getAllExtendedPlayers();
}
