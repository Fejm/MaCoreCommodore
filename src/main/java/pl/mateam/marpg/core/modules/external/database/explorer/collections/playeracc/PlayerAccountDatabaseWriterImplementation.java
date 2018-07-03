package pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc;

import org.bson.Document;

import pl.mateam.marpg.api.regular.enums.players.Rank;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.PlayerAccountDatabaseWriter;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.collections.playeracc.globalban.GlobalBanDatabaseWriter;
import pl.mateam.marpg.core.internal.hardcoded.database.PlayerAccountEntityCollectionHardcodedNames;
import pl.mateam.marpg.core.modules.external.database.explorer.AbstractDatabaseWriter;
import pl.mateam.marpg.core.modules.external.database.explorer.collections.playeracc.globalban.GlobalBanDatabaseWriterImplementation;

public class PlayerAccountDatabaseWriterImplementation extends AbstractDatabaseWriter implements PlayerAccountDatabaseWriter {
	public PlayerAccountDatabaseWriterImplementation(Document document) 	{	super(document);	}

	@Override public void setHashedPassword(String newValue) 	{	document.put(PlayerAccountEntityCollectionHardcodedNames.PASSWORD, newValue);			}
	@Override public void setIsWoman(boolean newValue) 			{	document.put(PlayerAccountEntityCollectionHardcodedNames.IS_WOMAN, newValue);			}
	@Override public void setIsModerator(boolean newValue) 		{	document.put(PlayerAccountEntityCollectionHardcodedNames.IS_MODERATOR, newValue);		}
	@Override public void setRank(Rank newValue) 				{	document.put(PlayerAccountEntityCollectionHardcodedNames.RANK, newValue.toString());	}

	@Override public GlobalBanDatabaseWriter getBanInfo() 		{	return getSubWriter(GlobalBanDatabaseWriterImplementation::new, PlayerAccountEntityCollectionHardcodedNames.BAN_INFO);	}
}
