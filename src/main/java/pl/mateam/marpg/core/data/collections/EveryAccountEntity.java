package pl.mateam.marpg.core.data.collections;

import java.util.HashMap;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;

import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.enums.chat.ChatState;
import pl.mateam.marpg.core.data.collections.embedded.every.MusicPlayerInfo;
import pl.mateam.marpg.core.internal.hardcoded.database.EveryAccountEntityCollectionHardcodedNames;

@Entity(value = EveryAccountEntityCollectionHardcodedNames.COLLECTION_NAME, noClassnameStored = true)
public class EveryAccountEntity {
	@SuppressWarnings("unused") private EveryAccountEntity()	{	this.nick = null;	}	//Morphia needs no-argument constructor to pull object
	
	public EveryAccountEntity(String nick) {
		this.nick = nick;
		chatStates[ChatChanel.GAMEMASTER.ordinal()] = ChatState.NOT_VISIBLE;
		chatStates[ChatChanel.MODERATOR.ordinal()] = ChatState.NOT_VISIBLE;
		chatStates[ChatChanel.PRIVATE.ordinal()] = ChatState.NOT_VISIBLE;
		chatStates[ChatChanel.TRADE.ordinal()] = ChatState.MUTED;
		chatStates[ChatChanel.NORMAL.ordinal()] = ChatState.TYPING;
	}



	@Id 	@Property(EveryAccountEntityCollectionHardcodedNames.IDENTIFICATOR)    @Indexed
	private final String nick;
	
	@Embedded(value = EveryAccountEntityCollectionHardcodedNames.MUSIC_PLAYER)
	public final MusicPlayerInfo music = new MusicPlayerInfo();
	
	@Property(EveryAccountEntityCollectionHardcodedNames.ADDITIONAL_VARIABLES)
	public final HashMap<String, Object> customVariables = new HashMap<>();
	
	@Property(EveryAccountEntityCollectionHardcodedNames.CHAT_STATES)
	public final ChatState[] chatStates = new ChatState[ChatChanel.values().length];
}
