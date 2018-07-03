package pl.mateam.marpg.api.regular.modules.sub.server;

import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;


public interface EnvironmentManager {
	boolean isRegistrationAllowed();
	void setRegistrationAllowed(boolean state);
	
	int getStartOfTheNightHour();
	int getEndOfTheNightHour();
	void setStartOfTheNightHour(int hour);
	void setEndOfTheNightHour(int hour);
	
	int getMessageSendingDelayAtNightInTicks();
	int getMessageSendingDelayAtDayInTicks();
	int getCurrentMessageSendingDelayInSeconds();
	void setMessageSendingDelayAtNight(int inSeconds);
	void setMessageSendingDelayAtDay(int inSeconds);
	
	String getChatMessagePrefix(ChatChanel chanel);
	void setChatMessagePrefix(ChatChanel chanel, String prefix);

	void setCharacterInfoSavingFrequency(int inMinutes);
	int getCharacterInfoSavingFrequencyInTicks();
}
