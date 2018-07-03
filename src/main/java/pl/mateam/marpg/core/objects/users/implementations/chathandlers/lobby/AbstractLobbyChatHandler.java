package pl.mateam.marpg.core.objects.users.implementations.chathandlers.lobby;

import org.bukkit.event.player.AsyncPlayerChatEvent;

import pl.mateam.marpg.core.objects.users.implementations.PlayerInLobbyImplementation;

public abstract class AbstractLobbyChatHandler {
	protected final PlayerInLobbyImplementation object;
	AbstractLobbyChatHandler(PlayerInLobbyImplementation object)	{	this.object = object;	}
	
	public abstract void handleMessageSending(AsyncPlayerChatEvent event);
}
