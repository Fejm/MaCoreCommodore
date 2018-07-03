package pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.characterclass;

public interface CharacterClassDatabaseWriter {
	void setLevel(int newValue);
	void setExperience(float newValue);
	void setHealth(double newValue);
	void setEnergy(int newValue);
	void setMoney(int newValue);
	void setTimePlayed(long newValue);
}
