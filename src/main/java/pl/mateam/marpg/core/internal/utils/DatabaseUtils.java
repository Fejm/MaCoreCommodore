package pl.mateam.marpg.core.internal.utils;

import java.util.regex.Pattern;

import org.bson.Document;
import org.mongodb.morphia.query.Query;

import pl.mateam.marpg.api.Core;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;

import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.result.UpdateResult;

public class DatabaseUtils {
	@Secret public static Document getUsingSingleCriteria(String collectionName, String identificatorName, String id){
		MongoCollection<Document> collectionObject = Core.getDatabase().getMongoDatabase().getCollection(collectionName);
	    Document filter = new Document().append(identificatorName, Pattern.compile("^" + id + "$", Pattern.CASE_INSENSITIVE));  		//Case insensitive
	    FindIterable<Document> doc = collectionObject.find(filter);
	    return doc.first();
	}

	@Secret public static UpdateResult update(String collectionName, String identificatorName, String idToModify, Document updateOperations){
		MongoCollection<Document> collectionObject = Core.getDatabase().getMongoDatabase().getCollection(collectionName);
        Document filter = new Document().append(identificatorName, Pattern.compile("^" + idToModify + "$", Pattern.CASE_INSENSITIVE));  //Case insensitive
        return collectionObject.updateOne(filter, updateOperations);
	}
	
	@Secret public static <T> T getUsingSingleCriteria(String identificatorName, String key, Class<T> returnType) {
		Query<T> query = Core.getDatabase().getMorphiaDatastore().createQuery(returnType);
		query.field(identificatorName).equalIgnoreCase(key);
		return query.get();
	}
}
