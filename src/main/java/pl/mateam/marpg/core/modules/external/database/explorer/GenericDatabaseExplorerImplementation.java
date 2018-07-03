package pl.mateam.marpg.core.modules.external.database.explorer;

import org.bson.Document;

import pl.mateam.marpg.core.internal.utils.DatabaseUtils;

import com.mongodb.client.result.UpdateResult;

public abstract class GenericDatabaseExplorerImplementation {
	protected abstract String getCollectionName();
	protected abstract String getCollectionIdentificatorName();
		
	public final UpdateResult performUpdate(String playerName, Document updateOperations) {
		return DatabaseUtils.update(getCollectionName(), getCollectionIdentificatorName(), playerName, updateOperations);
	}

	public final UpdateResult updateBySetting(String playerName, Document updateOperations) 	{	return performUpdate(playerName, new Document("$set", updateOperations));	}
}
