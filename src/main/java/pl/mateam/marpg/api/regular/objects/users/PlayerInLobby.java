package pl.mateam.marpg.api.regular.objects.users;



public interface PlayerInLobby extends PlayerBasedUser {
	boolean isRegistered();
	boolean isLoggedIn();
}
