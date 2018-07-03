package pl.mateam.marpg.core.modules.external.server.sub;

import java.util.Collection;
import java.util.stream.Collectors;

import org.bukkit.entity.Player;

import pl.mateam.marpg.api.regular.modules.sub.server.UsersManager;
import pl.mateam.marpg.api.regular.objects.users.AnyUser;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;
import pl.mateam.marpg.api.regular.objects.users.PlayerBasedUser;
import pl.mateam.marpg.api.regular.objects.users.PlayerInLobby;
import pl.mateam.marpg.core.objects.users.AnyUserImplementation;

public class UsersManagerImplementation implements UsersManager {
	@Override public AnyUser getGenericObject(Player player){
		return AnyUserImplementation.get(player);
	}
	
	@Override public PlayerBasedUser getPlayerBasedUser(Player player){
		AnyUser object = getGenericObject(player);
		if(object != null && object instanceof PlayerBasedUser)
			return (PlayerBasedUser) object;
		else return null;
	}
	
	@Override public PlayerInLobby getPlayerInLobbyObject(Player player){
		AnyUser object = getGenericObject(player);
		if(object != null && object instanceof PlayerInLobby)
			return (PlayerInLobby) object;
		else return null;
	}
	
	@Override public ExtendedPlayer getExtendedPlayerObject(Player player){
		AnyUser object = getGenericObject(player);
		if(object != null && object instanceof ExtendedPlayer)
			return (ExtendedPlayer) object;
		else return null;
	}
	
	@Override public Gamemaster getGamemasterObject(Player player){
		AnyUser object = getGenericObject(player);
		if(object != null && object instanceof Gamemaster)
			return (Gamemaster) object;
		else return null;
	}
	
	@Override public Collection<AnyUser> getAllUsersObjects()	{	return AnyUserImplementation.getAll();	}
	
	@Override public Collection<Gamemaster> getAllGamemasterObjects(){
		return AnyUserImplementation.getAll().stream().filter(v -> v instanceof Gamemaster).map(v -> (Gamemaster) v).collect(Collectors.toList());
	}
	
	@Override public Collection<PlayerBasedUser> getAllPlayerBasedUsers(){
		return AnyUserImplementation.getAll().stream().filter(v -> v instanceof PlayerBasedUser).map(v -> (PlayerBasedUser) v).collect(Collectors.toList());
	}
	
	@Override public Collection<PlayerInLobby> getAllPlayersInLobby(){
		return AnyUserImplementation.getAll().stream().filter(v -> v instanceof PlayerInLobby).map(v -> (PlayerInLobby) v).collect(Collectors.toList());
	}
	
	@Override public Collection<ExtendedPlayer> getAllExtendedPlayers(){
		return AnyUserImplementation.getAll().stream().filter(v -> v instanceof ExtendedPlayer).map(v -> (ExtendedPlayer) v).collect(Collectors.toList());
	}
}
