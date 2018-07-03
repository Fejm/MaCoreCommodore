package pl.mateam.marpg.core.data.listeners.protocollib;

import java.util.HashSet;
import java.util.Set;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.api.regular.objects.users.PlayerInLobby;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;
import pl.mateam.marpg.core.objects.users.implementations.PlayerInLobbyImplementation;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.reflect.StructureModifier;
import com.comphenix.protocol.wrappers.EnumWrappers.EntityUseAction;

public class ClientUseEntity {
	private static int bugWorkaround;
	private static Set<Integer> antiRapeSet = new HashSet<>();		//Bug workaround: normally packets are sent doubled.

	public ClientUseEntity() {
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		manager.addPacketListener(new PacketAdapter(MaCoreCommodoreEngine.getReference(), ListenerPriority.HIGHEST, PacketType.Play.Client.USE_ENTITY){
			@Override
			public void onPacketReceiving(PacketEvent event) {
				PacketContainer packet = event.getPacket();
				StructureModifier<EntityUseAction> action = packet.getEntityUseActions();
				if(!action.read(0).equals(EntityUseAction.INTERACT_AT))
					return;
				if(antiRapeSet.contains(event.getPlayer().getEntityId() * -1))
					antiRapeSet.remove(event.getPlayer().getEntityId() * -1);
				else {
					StructureModifier<Integer> entID = packet.getIntegers();
					PlayerInLobby u = Core.getServer().getUsers().getPlayerInLobbyObject(event.getPlayer());
					if(u != null){
						((PlayerInLobbyImplementation) u).handleClassPicking(entID.read(0));
						antiRapeSet.add(event.getPlayer().getEntityId() * -1);
					}
				}
			}
		});
	}
}
