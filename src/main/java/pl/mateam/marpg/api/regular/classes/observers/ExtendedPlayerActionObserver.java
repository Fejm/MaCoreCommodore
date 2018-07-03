package pl.mateam.marpg.api.regular.classes.observers;

import javax.annotation.Nullable;

import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLevelChangeEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import pl.mateam.marpg.api.regular.events.PlayerInventoryChangeCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPFinishedCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPRequestExpiredCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPRequestSendCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerTradeRequestExpiredCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerTradeRequestSendCommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;

public class ExtendedPlayerActionObserver {
	public void handle(@Nullable PlayerQuitEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {}
	public void handle(PlayerLevelChangeEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {}
	public void handle(PlayerPvPRequestExpiredCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {}
	public void handle(PlayerPvPRequestSendCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {}
	public void handle(PlayerTradeRequestExpiredCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {}
	public void handle(PlayerTradeRequestSendCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {}
	public void handle(PlayerInventoryChangeCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {}
	public void handle(PlayerPvPFinishedCommodoreEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {}
	public void handle(PlayerDeathEvent event, ExtendedPlayer caller, ExtendedPlayer observer) {}
}
