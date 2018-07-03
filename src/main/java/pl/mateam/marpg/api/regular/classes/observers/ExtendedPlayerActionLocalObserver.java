package pl.mateam.marpg.api.regular.classes.observers;

import org.bukkit.event.entity.PlayerDeathEvent;

import pl.mateam.marpg.api.regular.events.PlayerInventoryChangeCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPRequestExpiredCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerPvPRequestSendCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerTradeRequestExpiredCommodoreEvent;
import pl.mateam.marpg.api.regular.events.PlayerTradeRequestSendCommodoreEvent;

public class ExtendedPlayerActionLocalObserver {
	public void handle(PlayerPvPRequestExpiredCommodoreEvent event) {}
	public void handle(PlayerPvPRequestSendCommodoreEvent event) {}
	public void handle(PlayerTradeRequestExpiredCommodoreEvent event) {}
	public void handle(PlayerTradeRequestSendCommodoreEvent event) {}
	public void handle(PlayerInventoryChangeCommodoreEvent event) {}
	public void handle(PlayerDeathEvent event) {}
}
