package eco.stx.stacks.voice.common.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@EnableMongoRepositories(basePackages = "eco.stx.stacks.voice")
@Configuration
public class MongodbConfiguration extends AbstractMongoClientConfiguration {

    @Value("${stacks-voice.mongo.mongoIp}")
    String mongoIp;
    @Value("${stacks-voice.mongo.mongoPort}")
    String mongoPort;
    @Value("${stacks-voice.mongo.mongoDbName}")
    String mongoDbName;

    @Override
    protected String getDatabaseName() {
        return mongoDbName;
    }

    @Override
    public MongoClient mongoClient() {
        return MongoClients.create(mongoIp + ":" + mongoPort + "/" + mongoDbName);
    }

}
