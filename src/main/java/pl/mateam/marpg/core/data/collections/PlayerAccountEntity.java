package pl.mateam.marpg.core.data.collections;

import java.util.HashMap;

import org.mongodb.morphia.annotations.Embedded;
import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;

import pl.mateam.marpg.api.regular.enums.chat.ChatChanel;
import pl.mateam.marpg.api.regular.enums.players.Rank;
import pl.mateam.marpg.core.data.collections.embedded.players.BanInfo;
import pl.mateam.marpg.core.data.collections.embedded.players.CharacterClassInfo;
import pl.mateam.marpg.core.internal.hardcoded.database.PlayerAccountEntityCollectionHardcodedNames;

@Entity(value = PlayerAccountEntityCollectionHardcodedNames.COLLECTION_NAME, noClassnameStored = true)
public class PlayerAccountEntity {
	@SuppressWarnings("unused")		private PlayerAccountEntity()	{	this.nick = null;	}	//Morphia needs no-argument constructor to pull object
	
	public PlayerAccountEntity(String nick)		{
		this.nick = nick;
		for(int i = 0; i < chatBans.length; i++)
			chatBans[i] = new BanInfo();
	}



	@Id 	@Property(PlayerAccountEntityCollectionHardcodedNames.IDENTIFICATOR)    @Indexed
	private final String nick;

	
	@Embedded(value = PlayerAccountEntityCollectionHardcodedNames.CLASS_WARRIOR) 		public CharacterClassInfo warriorInfo = null;
	@Embedded(value = PlayerAccountEntityCollectionHardcodedNames.CLASS_BARBARIAN)		public CharacterClassInfo barbarianInfo = null;
	@Embedded(value = PlayerAccountEntityCollectionHardcodedNames.CLASS_MAGE) 			public CharacterClassInfo mageInfo = null;
	@Embedded(value = PlayerAccountEntityCollectionHardcodedNames.CLASS_SLAYER) 		public CharacterClassInfo slayerInfo = null;



	@Embedded(value = PlayerAccountEntityCollectionHardcodedNames.BAN_INFO)
	private final BanInfo globalBanInfo = new BanInfo();
	public BanInfo getGlobalBanInfo()				{	return globalBanInfo;				}

	private final BanInfo[] chatBans = new BanInfo[ChatChanel.values().length];
	public BanInfo getBanInfo(ChatChanel chanel)	{	return chatBans[chanel.ordinal()];	}
	
	@Property(PlayerAccountEntityCollectionHardcodedNames.PASSWORD)
	public String password = null;
	
	@Property(PlayerAccountEntityCollectionHardcodedNames.IS_WOMAN)
	public boolean isWoman = false;
	
	@Property(PlayerAccountEntityCollectionHardcodedNames.IS_MODERATOR)
	public boolean isModerator = false;
	
	@Property(PlayerAccountEntityCollectionHardcodedNames.RANK)
	public Rank rank = Rank.PLAYER;



	@Property(PlayerAccountEntityCollectionHardcodedNames.ADDITIONAL_VARIABLES)
	public HashMap<String, Object> customVariables = new HashMap<>();
}
