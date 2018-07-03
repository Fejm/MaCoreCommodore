package pl.mateam.marpg.core.internal.helpers;

import pl.mateam.marpg.api.regular.enums.chat.ChatState;

public class ChatStateInfo {
	private final ChatState state;
	public ChatStateInfo(ChatState state)	{	this.state = state;	}
	
	public boolean isActive()	{	return state == ChatState.TYPING || state == ChatState.ACTIVE || state == ChatState.BLOCKED_ACTIVE;	}
	public boolean isBanned()	{	return state == ChatState.BLOCKED_ACTIVE || state == ChatState.BLOCKED_MUTED;						}
}
