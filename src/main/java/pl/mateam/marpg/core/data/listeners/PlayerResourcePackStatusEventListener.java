package pl.mateam.marpg.core.data.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerResourcePackStatusEvent;
import org.bukkit.event.player.PlayerResourcePackStatusEvent.Status;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;


public class PlayerResourcePackStatusEventListener implements Listener{
	private static final String RESOURCEPACK_NOT_ACCEPTED = 
			"§4§lNie zaakceptowałeś paczki zasobów MaRPG! :(\n\n"
			+ "§cTylko dzięki specjalnej paczce zasobów gra na MaRPG posiada swój urok.\n"
			+ "§cPaczka zostanie pobrana i uruchomiona w pełni automatycznie!\n"
			+ "§cNie wymagamy modyfikacji Twojego klienta gry, a sama paczka jest wolna od wirusów.\n\n"
			+ "§cUpewnij się, że w Twojej liście serwerów uruchomiłeś pobieranie paczki zasobów dla MaRPG.\n"
			+ "§cW razie problemów zerknij w odpowiedni dział pomocy na forum MaRPG.";
	private static final String RESOURCEPACK_DOWNLOAD_FAILED =
			"§4§lNie udało się zainstalować paczki zasobów MaRPG!\n\n"
			+ "§cNiestety nie udało się zainstalować paczki zasobów MaRPG.\n"
			+ "§cOdwiedź proszę nasze forum, aby dowiedzieć się, co mogło być tego przyczyną.\n"
			+ "§cMożliwe, że serwer jest przeciążony i w tej chwili sporo osób ma ten sam problem.\n\n"
			+ "§cPrzepraszamy za problem i życzymy miłego dnia\n"
			+ "§c§o                           Zespół MaTeam";
	
	@EventHandler(priority = EventPriority.HIGHEST)		//Should always run at the end
	public void on(PlayerResourcePackStatusEvent event){
		Player player = event.getPlayer();
		Status status = event.getStatus();
		
		if(Core.getServer().getUsers().getGamemasterObject(player) != null){
			if(status.equals(Status.DECLINED)) player.sendMessage(CoreUtils.chat.getCasualAdminMessage("Ponieważ jesteś Mistrzem Gry wymuszanie paczki zasobów zostało anulowane."));
			return;
		}
		switch(status){
			case DECLINED:
				Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> player.kickPlayer(RESOURCEPACK_NOT_ACCEPTED));
				return;
				
			case FAILED_DOWNLOAD:
				Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> player.kickPlayer(RESOURCEPACK_DOWNLOAD_FAILED));
				return;

			default:
				boolean hasRegistered = Core.getServer().getUsers().getPlayerInLobbyObject(player).isRegistered();
				switch(status){
					case ACCEPTED:
						if(!hasRegistered){
							player.sendMessage(CoreUtils.chat.getSuccessMessage("Dziękujemy za zaakceptowanie pobierania paczki zasobów MaRPG!"));	
							player.sendMessage(CoreUtils.chat.getSuccessMessage("Paczka zasobów zostanie pobrana i przygotowana do gry automatycznie! :)"));	
						}
						return;
						
					case SUCCESSFULLY_LOADED:
						if(hasRegistered){
							player.sendMessage(CoreUtils.chat.getCasualMessage("Witaj z powrotem na MaRPG ") + CoreUtils.chat.getMaRPGcolorCasualHighlighted() + player.getName() + CoreUtils.chat.getMaRPGcolorCasual() + "!");
							player.sendMessage(CoreUtils.chat.getCasualMessage("Podaj proszę hasło, które ustawiłeś podczas rejestracji konta."));
							Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> CoreUtils.ingame.playSoundPrivate(player, true, "auth.logged_in"));
						} else {
							player.sendMessage(CoreUtils.chat.getCasualMessage("W porządku, ten klient jest już skonfigurowany do gry na MaRPG!"));
							player.sendMessage(CoreUtils.chat.getCasualMessage("Zanim rozpoczniesz grę musisz jeszcze tylko zarejestrować swoje konto."));
							player.sendMessage(CoreUtils.chat.getCasualMessage("To bardzo proste - wpisz po prostu hasło, którego będziesz używać do logowania."));
							player.sendMessage(CoreUtils.chat.getCasualMessage("Nie musisz wpisywać znaku \"") + CoreUtils.chat.getMaRPGcolorCasualHighlighted() + "/" + CoreUtils.chat.getMaRPGcolorCasual() + "\" na początku linijki. :)");
							Bukkit.getScheduler().runTask(MaCoreCommodoreEngine.getReference(), () -> CoreUtils.ingame.playSoundPrivate(player, true, "auth.please_register"));
						}
						return;
					default: return;
				}
		}
	}
}
