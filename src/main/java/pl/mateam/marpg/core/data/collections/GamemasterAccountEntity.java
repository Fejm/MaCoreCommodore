package pl.mateam.marpg.core.data.collections;

import java.util.HashMap;

import org.mongodb.morphia.annotations.Entity;
import org.mongodb.morphia.annotations.Id;
import org.mongodb.morphia.annotations.Indexed;
import org.mongodb.morphia.annotations.Property;
import org.mongodb.morphia.annotations.Transient;

import pl.mateam.marpg.core.internal.hardcoded.database.GamemasterAccountEntityCollectionHardcodedNames;

@Entity(value = GamemasterAccountEntityCollectionHardcodedNames.COLLECTION_NAME, noClassnameStored = true)
public class GamemasterAccountEntity {
	@SuppressWarnings("unused")		 private GamemasterAccountEntity()	{	this.nick = null;	}	//Morphia needs no-argument constructor to pull object
	
	
	@Transient public boolean isVisible = false;

	
	

	
	public GamemasterAccountEntity(String nick){
		this.nick = nick;
	}



	@Id 	@Property(GamemasterAccountEntityCollectionHardcodedNames.IDENTIFICATOR)    @Indexed
	private final String nick;

	@Property(GamemasterAccountEntityCollectionHardcodedNames.LATEST_IP)
	public String latestIP = "127.0.0.1";

	@Property(GamemasterAccountEntityCollectionHardcodedNames.ADDITIONAL_VARIABLES)
	public HashMap<String, Object> customVariables = new HashMap<>();

	
	@Property(GamemasterAccountEntityCollectionHardcodedNames.INTERACTION_STATE)
	public boolean interactionEnabled = false;
}
