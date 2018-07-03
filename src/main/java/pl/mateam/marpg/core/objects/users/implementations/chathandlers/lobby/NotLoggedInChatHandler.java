package pl.mateam.marpg.core.objects.users.implementations.chathandlers.lobby;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.events.PlayerSuccessfullyLoggedCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerSuccessfullyRegisteredCommodoreEvent;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.internal.utils.Parsers;
import pl.mateam.marpg.core.objects.users.implementations.PlayerInLobbyImplementation;

public class NotLoggedInChatHandler extends AbstractLobbyChatHandler {
	public NotLoggedInChatHandler(PlayerInLobbyImplementation object) 	{	super(object);	}

	@Override public void handleMessageSending(AsyncPlayerChatEvent event) {
		String message = event.getMessage();
		String password = object.additionalInfo.password;
		PluginManager events = Bukkit.getServer().getPluginManager();
		Player player = object.getBukkitPlayer();
		if(password != null){
			if(passwordHasProperLength(message)){
				if(passwordMatch(message)){
					player.sendMessage(CoreUtils.chat.getSuccessMessage("Hasło przyjęte."));
					player.sendMessage(CoreUtils.chat.getCasualMessage("Życzymy miłej rozgrywki na ") + CoreUtils.chat.getMaRPGcolorCasualHighlighted() + "MaRPG" + CoreUtils.chat.getMaRPGcolorCasual() + "!");
					successfulLogin();
					CoreUtils.ingame.playSoundPrivate(player, true, "auth.sucessfully_logged");
					player.teleport(Core.getServer().getWorlds().getWarp("LobbyPostaci"));
					object.playMusicInTectonicMode();
					events.callEvent(new PlayerSuccessfullyLoggedCommodoreEvent(object));
				}
				else informThatPasswordDoesntMatch();
			}
			else{
				informThatPasswordDoesntMatch();
				player.sendMessage(CoreUtils.chat.getCasualMessage("Pamiętaj, że Twoje hasło zawiera od 6 do 15 znaków!"));
			}
			return;
		} else {
			if(message.length() >= 6){
				if(message.length() <= 15) {
					object.additionalInfo.password = Parsers.getHash(message, player.getName());
					successfulLogin();
					player.sendMessage(CoreUtils.chat.getSuccessMessage("Twoje konto zostało zarejestrowane pomyślnie."));
					player.sendMessage(CoreUtils.chat.getCasualMessageWithHighlight("", "MaRPG ", "czeka na Ciebie..."));
					CoreUtils.ingame.playSoundPrivate(player, true, "auth.sucessfully_registered");
					player.teleport(Core.getServer().getWorlds().getWarp("LobbyPostaci"));
					object.playMusicInTectonicMode();
					events.callEvent(new PlayerSuccessfullyRegisteredCommodoreEvent(object));
					return;
				}
				else passwordTooLong();
			}
			else passwordTooShort();
		}
	}
	
	private boolean passwordMatch(String givenPassword) {
		String hash = Parsers.getHash(givenPassword, object.getBukkitPlayer().getName());
		return hash.equals(object.additionalInfo.password);
	}
	
	private boolean passwordHasProperLength(String givenPassword){
		return givenPassword.length() >= 6 && givenPassword.length() <= 15;
	}
	
	private void successfulLogin(){
		Player player = object.getBukkitPlayer();
		object.setChatHandler(new LoggedInChatHandler(object));
		//Sync part:
		Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> {
			stopSounds();
			player.removePotionEffect(PotionEffectType.SLOW);
			player.addPotionEffect(new PotionEffect(PotionEffectType.SPEED, Integer.MAX_VALUE, 1, false, false), true);
			object.showLobby();
		});
	}
	
	private void stopSounds(){
		Player player = object.getBukkitPlayer();
		player.stopSound("marpg.lektor.auth.password_too_short");
		player.stopSound("marpg.lektor.auth.password_too_long");
		player.stopSound("marpg.lektor.auth.wrong_password");
		player.stopSound("marpg.lektor.auth.logged_in");
		player.stopSound("marpg.lektor.auth.please_register");
	}
	
	private void informThatPasswordDoesntMatch(){
		Player player = object.getBukkitPlayer();
		stopSounds();
		CoreUtils.ingame.playSoundPrivate(player, true, "auth.wrong_password");
		player.sendMessage(CoreUtils.chat.getErrorMessage("Podane przez Ciebie hasło jest nieprawidłowe."));
	}
	
	private void passwordTooShort(){
		Player player = object.getBukkitPlayer();
		player.sendMessage(CoreUtils.chat.getErrorMessage("Twoje hasło jest za krótkie."));
		stopSounds();
		CoreUtils.ingame.playSoundPrivate(player, true, "auth.password_too_short");
		player.sendMessage(CoreUtils.chat.getCasualMessage("Minimalna długość hasła wynosi ") + CoreUtils.chat.getMaRPGcolorCasualHighlighted() + "6 " + CoreUtils.chat.getMaRPGcolorCasual() + "znaków.");
	}
	
	private void passwordTooLong(){
		Player player = object.getBukkitPlayer();
		player.sendMessage(CoreUtils.chat.getErrorMessage("Twoje hasło jest za długie."));
		stopSounds();
		CoreUtils.ingame.playSoundPrivate(player, true, "auth.password_too_long");
		player.sendMessage(CoreUtils.chat.getCasualMessage("Maksymalna długość hasła wynosi ") + CoreUtils.chat.getMaRPGcolorCasualHighlighted() + "15 " + CoreUtils.chat.getMaRPGcolorCasual() + "znaków.");
	}
}
