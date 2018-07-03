package pl.mateam.marpg.api.regular.objects.users;

import pl.mateam.marpg.api.regular.enums.players.Rank;

public interface PlayerBasedUser extends AnyUser {
	boolean isWoman();
	boolean isModerator();
	Rank getRank();
	
	void setWoman(boolean state);
	void setModerator(boolean state);
	void setRank(Rank rank);
}
