package pl.mateam.marpg.api.regular.objects.users;

import org.bukkit.entity.Player;

import pl.mateam.marpg.api.regular.classes.inventories.CommodoreInventory;
import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.enums.chat.ChatState;
import pl.mateam.marpg.api.regular.enums.inventories.CommonInventory;

public interface AnyUser {
	Player getBukkitPlayer();
	
	void setDefaultMaSkin();
	void setSkin(String nickOfPlayerPremium);
	
	void muteMusicPlayer();
	void playMusicInTectonicMode();
	void playSong(String songName, boolean looped);
	void stopMusic();
	String getNameOfCurrentlyPlayingSong();
	
	boolean hasCustomSkin();
	

	void saveCurrentStateToDatabase();

	boolean hasOpenInventory();
	void openInventoryIfPossible(CommodoreInventory inventory);
	void openInventoryIfPossible(CommonInventory inventoryType);
	void openInventoryIfPossible(String remoteInventoryAccessKey);
	void forceOpenInventory(CommodoreInventory inventory);
	void forceOpenInventory(CommonInventory inventoryType);
	void forceOpenInventory(String remoteInventoryAccessKey);
	void closeInventory();
	
	ChatChanel getTypingChanel();
	ChatState getChatState(ChatChanel chanel);
	void setChatState(ChatChanel chanel, ChatState newState);
}
