package pl.mateam.marpg.core.objects.users;

import java.util.Date;

import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.enums.chat.ChatState;
import pl.mateam.marpg.api.regular.enums.players.Rank;
import pl.mateam.marpg.api.regular.objects.users.PlayerBasedUser;
import pl.mateam.marpg.core.data.collections.EveryAccountEntity;
import pl.mateam.marpg.core.data.collections.PlayerAccountEntity;
import pl.mateam.marpg.core.data.collections.embedded.players.BanInfo;

public abstract class UserBasedPlayerImplementation extends AnyUserImplementation implements PlayerBasedUser {
	public final PlayerAccountEntity additionalInfo;

	protected UserBasedPlayerImplementation(Player player, EveryAccountEntity primaryInfo, PlayerAccountEntity additionalInfo) {
		super(player, primaryInfo);
		this.additionalInfo = additionalInfo;
		
		for(ChatChanel chanel : ChatChanel.values()) {
			if(checkChatBanExpirationTime(chanel)) {
				ChatState currentChatState = primaryInfo.chatStates[chanel.ordinal()];
				if(currentChatState == ChatState.TYPING || currentChatState == ChatState.ACTIVE)
					primaryInfo.chatStates[chanel.ordinal()] = ChatState.BLOCKED_ACTIVE;
				else if(currentChatState == ChatState.MUTED)
					primaryInfo.chatStates[chanel.ordinal()] = ChatState.BLOCKED_MUTED;
				if(currentChatState == ChatState.TYPING)
					typingChanel = null;
			}
		}
	}
	protected UserBasedPlayerImplementation(UserBasedPlayerImplementation currentObject){
		this(currentObject.player, currentObject.primaryInfo, currentObject.additionalInfo);
	}
	
	@Override protected Runnable writeAdditionalInfoToDatabase()	{	return () -> Core.getDatabase().getMorphiaDatastore().save(additionalInfo);		}
	
	@Override public boolean isWoman()						{	return additionalInfo.isWoman;			}
	@Override public void setWoman(boolean state) {
		if(isWoman() == state)	return;
		additionalInfo.isWoman = state;
		if(state)
			player.sendMessage(CoreUtils.chat.getSuccessMessage("Zostałaś pomyślnie zweryfikowana jako kobieta! :)"));
		if(!hasCustomSkin())	setDefaultMaSkin();
	}
	
	@Override public Rank getRank()							{	return additionalInfo.rank;				}
	@Override public void setRank(Rank rank) {
		if(getRank() == rank)	return;
		additionalInfo.rank = rank;
		player.sendMessage(CoreUtils.chat.getInfoMessage("Twoja ranga uległa zmianie."));
		Inventory inv = player.getInventory();
		inv.setContents(inv.getContents());		//Inventory refreshing
		if(!hasCustomSkin())	setDefaultMaSkin();
	}

	
	@Override public boolean isModerator()					{	return additionalInfo.isModerator;		}
	@Override public void setModerator(boolean state) {
		if(isModerator() == state)	return;
		int dodacFajerwerki;
		additionalInfo.isModerator = state;
		if(state) {
			player.sendMessage(CoreUtils.chat.getSuccessMessage("Uzyskałeś uprawnienia moderatora! Witamy w MaTeamie!"));
			setChatState(ChatChanel.MODERATOR, ChatState.ACTIVE);
		} else
			setChatState(ChatChanel.MODERATOR, ChatState.NOT_VISIBLE);
	}

	protected boolean checkChatBanExpirationTime(ChatChanel chanel) {			//Returns true if player is still banned
		BanInfo banInfo = additionalInfo.getBanInfo(chanel);
		Date expire = banInfo.banExpirationTime;
		if(expire == null)
			return false;
		if(new Date().after(banInfo.banExpirationTime)) {
			banInfo.clear();
			switch(primaryInfo.chatStates[chanel.ordinal()]) {
				case BLOCKED_ACTIVE:	primaryInfo.chatStates[chanel.ordinal()] = ChatState.ACTIVE;	break;
				case BLOCKED_MUTED:		primaryInfo.chatStates[chanel.ordinal()] = ChatState.MUTED;		break;
				default:				break;
			}
			chatBanHasExpired(chanel);
			return false;
		}
		return true;
	}
	
	protected void chatBanHasExpired(ChatChanel chanel) {
		chatStateHasChanged(chanel);
		int todo;
	}
}
