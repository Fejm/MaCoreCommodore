package pl.mateam.marpg.core.data.listeners.protocollib;

import java.util.List;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.modules.sub.server.ItemsManager;
import pl.mateam.marpg.api.regular.objects.items.CommodoreItem;
import pl.mateam.marpg.api.regular.objects.items.CommodoreSpecialItem;
import pl.mateam.marpg.api.regular.objects.users.ExtendedPlayer;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;

public class ServerSetSlotServerWindowsItems {
	public ServerSetSlotServerWindowsItems(){
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		manager.addPacketListener(new PacketAdapter(MaCoreCommodoreEngine.getReference(), ListenerPriority.HIGHEST, PacketType.Play.Server.SET_SLOT, PacketType.Play.Server.WINDOW_ITEMS){
			@Override
			public void onPacketSending(PacketEvent event) {
				ItemsManager items = Core.getServer().getItems();
				Player player = event.getPlayer();
				PacketContainer packet = event.getPacket();
				if(packet.getType().equals(PacketType.Play.Server.WINDOW_ITEMS)){
					ExtendedPlayer exPlayer = Core.getServer().getUsers().getExtendedPlayerObject(player);
					if(exPlayer == null)
						return;
					List<ItemStack> list = packet.getItemListModifier().read(0);
					for(int i = 0; i < list.size(); i++){
						ItemStack item = list.get(i);
						if(item.getType() != Material.AIR){
							CommodoreItem cItem = items.getCommodoreLayer(item);
							if(cItem instanceof CommodoreSpecialItem) {
								CommodoreSpecialItem csItem = (CommodoreSpecialItem) cItem;
								list.set(i, csItem.getEffectiveAppearance(exPlayer.getCharacterClass(), player.getLevel(), exPlayer.getRank().getHSVArmorColorValue()));
							}
						}
					}
					packet.getItemListModifier().write(0, list);
					return;
				}
				StructureModifier<ItemStack> itemq = packet.getItemModifier();
				ItemStack item = itemq.read(0);
				if(item.getType() != Material.AIR){
					ExtendedPlayer exPlayer = Core.getServer().getUsers().getExtendedPlayerObject(player);
					if(exPlayer == null)
						return;
					CommodoreItem cItem = items.getCommodoreLayer(item);
					if(cItem instanceof CommodoreSpecialItem){
						CommodoreSpecialItem csItem = (CommodoreSpecialItem) cItem;
						itemq.write(0, csItem.getEffectiveAppearance(exPlayer.getCharacterClass(), player.getLevel(), exPlayer.getRank().getHSVArmorColorValue()));
					}
				}
			}
		});
	}
}
