package eco.stx.edao.common.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.AbstractMongoClientConfiguration;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;

@EnableMongoRepositories(basePackages = "eco.stx.edao")
@Configuration
public class MongodbConfiguration extends AbstractMongoClientConfiguration {

    @Value("${eco-stx.mongo.mongoIp}")
    String mongoIp;
    @Value("${eco-stx.mongo.mongoPort}")
    String mongoPort;
    @Value("${eco-stx.mongo.mongoDbName}")
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
