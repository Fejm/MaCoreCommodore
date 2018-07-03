package pl.mateam.marpg.api.regular.modules.sub.database;

import org.mongodb.morphia.Datastore;

import pl.mateam.marpg.api.regular.modules.sub.database.explorer.DatabaseExplorer;

import com.mongodb.client.MongoDatabase;

public interface Database {
	Datastore getMorphiaDatastore();
	MongoDatabase getMongoDatabase();
	DatabaseExplorer getExplorer();
}
