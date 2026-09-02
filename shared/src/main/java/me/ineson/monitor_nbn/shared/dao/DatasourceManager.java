/**
 * 
 */
package me.ineson.monitor_nbn.shared.dao;

import java.util.Objects;

import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.mongodb.ConnectionString;
import com.mongodb.MongoClientSettings;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * @author peter
 *
 */
public class DatasourceManager {

    private static final Logger LOG = LoggerFactory.getLogger(DatasourceManager.class);

    private MongoDatabase mongoDatabase;        

    private MongoClient mongoClient;
    
    private String dbUrl = null;

    private String dbName = "nbn";

    public DatasourceManager() {
        super();
        mongoClient = MongoClients.create(); 
        createDatabase(mongoClient);
    }

    public DatasourceManager(String url) {
        super();
        dbUrl = url;
        mongoClient = MongoClients.create(
        		MongoClientSettings.builder().applyConnectionString(new ConnectionString(dbUrl)).build()); 
        createDatabase(mongoClient);
    }
    
    public DatasourceManager(String url, String name) {
        super();
        dbUrl = url;
        dbName = name;
        mongoClient = MongoClients.create(
        		MongoClientSettings.builder().applyConnectionString(new ConnectionString(dbUrl)).build()); 
        createDatabase(mongoClient);
    }

    public MongoDatabase getDatabase() {
    	if(mongoDatabase == null) {
    		throw new IllegalStateException("MongoDatase has not been initialised");
    	}
    	
    	return mongoDatabase;
    }
	
    private void createDatabase(MongoClient client) {
    	CodecRegistry pojoCodecRegistry = CodecRegistries.fromRegistries(
        	    MongoClientSettings.getDefaultCodecRegistry(),
        	    CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true).build())
        	);
        mongoDatabase = mongoClient.getDatabase(dbName).withCodecRegistry(pojoCodecRegistry);
        
    }
    
    public synchronized void close() {
    	if (Objects.nonNull(mongoClient)) {
            mongoClient.close();
            mongoClient = null;
    	}
    }
	
}
