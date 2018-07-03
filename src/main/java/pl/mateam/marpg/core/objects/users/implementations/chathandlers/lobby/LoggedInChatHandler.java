package pl.mateam.marpg.core.objects.users.implementations.chathandlers.lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.core.objects.users.implementations.PlayerInLobbyImplementation;

public class LoggedInChatHandler extends AbstractLobbyChatHandler {
	LoggedInChatHandler(PlayerInLobbyImplementation object) 	{	super(object);	}

	@Override public void handleMessageSending(AsyncPlayerChatEvent event) {
		Player player = object.getBukkitPlayer();
		player.sendMessage(CoreUtils.chat.getCasualMessage("Hej, wybierz klasę postaci i graj!"));
		CoreUtils.ingame.playSoundPrivate(player, true, "chat.pick_character");
	}
}
