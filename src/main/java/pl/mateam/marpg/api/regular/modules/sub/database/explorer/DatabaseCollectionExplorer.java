package pl.mateam.marpg.api.regular.modules.sub.database.explorer;

import org.bson.Document;

import com.mongodb.client.result.UpdateResult;

public interface DatabaseCollectionExplorer {
	UpdateResult performUpdate(String playerName, Document updateOperations);
	UpdateResult updateBySetting(String playerName, Document updateOperations);		//Uses $set update
}
