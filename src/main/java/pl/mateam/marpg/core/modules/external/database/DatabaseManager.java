package pl.mateam.marpg.core.modules.external.database;

import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.mongodb.morphia.Datastore;
import org.mongodb.morphia.Morphia;

import pl.mateam.marpg.api.CommodoreComponent;
import pl.mateam.marpg.api.CommodoreComponent.DatabaseEntities;
import pl.mateam.marpg.api.CoreUtils;
import pl.mateam.marpg.api.regular.modules.sub.database.Database;
import pl.mateam.marpg.api.regular.modules.sub.database.explorer.DatabaseExplorer;
import pl.mateam.marpg.core.CommodoreCore;
import pl.mateam.marpg.core.MaCoreCommodoreEngine.Secret;
import pl.mateam.marpg.core.modules.external.database.explorer.DatabaseExplorerImplementation;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class DatabaseManager implements Database {	
	private final MongoClient mongo = new MongoClient();
	private final Morphia morphia = new Morphia();
	private final MongoDatabase database = mongo.getDatabase("MaRPG");
	private final Datastore morphiaDatastore = morphia.createDatastore(mongo, "MaRPG");
	private final DatabaseExplorerImplementation explorer = new DatabaseExplorerImplementation();
	static {
		Logger mongoLogger = Logger.getLogger( "org.mongodb.driver" );
		mongoLogger.setLevel(Level.SEVERE);
	}
	
	

	@Secret public void sendShutdownSignal() 	{	mongo.close();	}
		
	@Secret public void mapClasses(CommodoreComponent component){
		try {
			Method method = component.getClass().getDeclaredMethod("turnedOn", (Class<?>[]) null);
			method.setAccessible(true);
			if(method.isAnnotationPresent(DatabaseEntities.class)){
				Class<?>[] classesToMap = method.getAnnotation(DatabaseEntities.class).entityClasses();
				for(Class<?> classToMap : classesToMap){
					morphia.map(classToMap);
					morphiaDatastore.ensureIndexes(classToMap);
				}
			}
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		}
	}
	

	@Override public MongoDatabase getMongoDatabase()				{	sendSyncWarning(); 	return database;			}
	@Override public Datastore getMorphiaDatastore()				{	sendSyncWarning(); 	return morphiaDatastore;	}
	
	@Override public DatabaseExplorer getExplorer()					{	return explorer;			}
	
	private void sendSyncWarning(){
		if(Bukkit.isPrimaryThread() && !CommodoreCore.getHiddenMethods().getHiddenData().isServerDown)
			CoreUtils.io.sendConsoleMessageWarning("Coś próbowało wykonać operacje w bazie danych w głównym wątku serwera!");
	}
}
