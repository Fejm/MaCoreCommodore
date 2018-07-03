package pl.mateam.marpg.core.objects.users.implementations;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Collection;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.HoverEvent.Action;
import net.md_5.bungee.api.chat.TextComponent;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.events.GamemasterVisibilityChangeCommodoreEvent;
import pl.mateam.marpg.api.regular.objects.users.Gamemaster;
import pl.mateam.marpg.api.regular.objects.users.PlayerBasedUser;
import pl.mateam.marpg.core.data.collections.EveryAccountEntity;
import pl.mateam.marpg.core.data.collections.GamemasterAccountEntity;
import pl.mateam.marpg.core.modules.external.server.sub.SkinsManagerImplementation;
import pl.mateam.marpg.core.objects.users.AnyUserImplementation;
import pl.mateam.marpg.core.objects.users.implementations.inventories.gamemaster.StandardGamemasterInventory;

public class GamemasterImplementation extends AnyUserImplementation implements Gamemaster {
	private final GamemasterAccountEntity additionalInfo;
	private final TextComponent messageBase = new TextComponent();

	public GamemasterImplementation(Player player, EveryAccountEntity primaryInfo, GamemasterAccountEntity additionalInfo) {
		super(player, primaryInfo);
		assignToChatChannels();
		this.additionalInfo = additionalInfo;
		hideFromPlayers();
		messageBase.setText(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + player.getName() + ": ");
		messageBase.setHoverEvent(new HoverEvent(Action.SHOW_TEXT, new ComponentBuilder(ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + player.getName() + ", " + ChatColor.DARK_RED.toString() + ChatColor.BOLD.toString() + "Mistrz Gry\n\n"
				+ ChatColor.RED.toString() + "Mistrzowie Gry to wysłannicy samego Poteflona.\n"
				+ ChatColor.RED.toString() + "Każdy, kto choć trochę zbliżył się do uzyskania tego tytułu,\n"
				+ ChatColor.RED.toString() + "jest prawdziwym herosem!").create()));
	}
	
	@Override protected void setDefaultSkin() {	((SkinsManagerImplementation) Core.getServer().getSkins()).changeSkin(player, player.getName());	}
	
	@Override protected void writeCurrentFieldsState() {
		int todo;
	}
	@Override protected Runnable writeAdditionalInfoToDatabase(){
		return () -> Core.getDatabase().getMorphiaDatastore().save(additionalInfo);
	}

	@Override public void handleMessageSending(AsyncPlayerChatEvent event) {
		int privateChat;
		if(!additionalInfo.isVisible && typingChanel != ChatChanel.GAMEMASTER) {
			getBukkitPlayer().sendMessage(CoreUtils.chat.getCasualAdminMessage("Nie możesz wysyłać wiadomości na ten kanał kiedy nie jesteś widoczny."));
			return;
		}
		
		TextComponent base = new TextComponent(ChatColor.DARK_AQUA + new SimpleDateFormat("[HH:mm] ").format(Instant.now().toEpochMilli()));
		base.addExtra(new TextComponent(Core.getServer().getEnvironment().getChatMessagePrefix(typingChanel)));
		base.addExtra(" ");
		base.addExtra(messageBase);
        base.addExtra(new TextComponent(ChatColor.GOLD.toString() + ChatColor.ITALIC.toString() + event.getMessage()));
	
        pushMessage(base);
	}

	@Override public void handleInventoryClicking(InventoryClickEvent event) {
		int todo;
	}


	@Override public boolean hasInteractionEnabled() 				{	return additionalInfo.interactionEnabled;		}
	@Override public void setInteractionEnabled(boolean newValue) 	{	additionalInfo.interactionEnabled = newValue;	}
	
	private void hideFromPlayers(){
		Collection<PlayerBasedUser> foreigners = Core.getServer().getUsers().getAllPlayerBasedUsers();
		foreigners.forEach(foreigner ->	foreigner.getBukkitPlayer().hidePlayer(player));
	}

	@Override public boolean isVisible() 	{	return additionalInfo.isVisible;	}

	@Override public void setVisible(boolean newValue, boolean silent) {
		if(additionalInfo.isVisible == newValue)
			return;
		if(!silent) {
			if(newValue)	Core.getServer().getUsers().getAllPlayerBasedUsers().forEach(user -> user.getBukkitPlayer().showPlayer(player));
			else			Core.getServer().getUsers().getAllPlayerBasedUsers().forEach(user -> user.getBukkitPlayer().hidePlayer(player));
		}
		Bukkit.getServer().getPluginManager().callEvent(new GamemasterVisibilityChangeCommodoreEvent(this, silent));
		additionalInfo.isVisible = newValue;
	}

	@Override protected CommodoreInventory getDefaultInventory() 	{	return new StandardGamemasterInventory(this);	}
	@Override protected void chatStateHasChanged(ChatChanel chanel) {}
	@Override public boolean hasOpenInventory() 	{	return !(getOpenInventory() instanceof StandardGamemasterInventory);	}
}
