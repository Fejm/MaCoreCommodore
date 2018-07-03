package pl.mateam.marpg.core.data.listeners.protocollib;

import java.util.Arrays;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.core.MaCoreCommodoreEngine;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerOptions;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.events.PacketListener;

public class ServerOutServerInfo {
	@SuppressWarnings("deprecation")
	public ServerOutServerInfo(){
		ProtocolManager manager = ProtocolLibrary.getProtocolManager();
		manager.addPacketListener((PacketListener) new PacketAdapter(MaCoreCommodoreEngine.getReference(),
																		ListenerPriority.HIGHEST,
																		Arrays.asList(PacketType.Status.Server.OUT_SERVER_INFO),
																		new ListenerOptions[] { ListenerOptions.ASYNC }) {
			public void onPacketSending(PacketEvent event) {
				event.getPacket().getServerPings().read(0).setPlayers(Core.getServer().getMotd().getHoverMotd());
			}
		});
	}
}
