package eco.stx.edao.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "application")
public class ApplicationSettings {
	private String mongoIp;
	private String mongoDbName;
	private String mongoPort;
	@Value("${spring.profiles.active}") private String activeProfile;

	public ApplicationSettings() {
	}

	public String getMongoIp() {
		return mongoIp;
//		if (activeProfile.equals("staging") || activeProfile.equals("production")) {
//        	return containerHostIp;
//		} else {
//    		return mongoIp;
//        }
	}

	public void setMongoIp(String mongoIp) {
		this.mongoIp = mongoIp;
	}

	public String getActiveProfile() {
		return activeProfile;
	}

	public void setActiveProfile(String activeProfile) {
		this.activeProfile = activeProfile;
	}


	public String getMongoDbName() {
		return mongoDbName;
	}


	public void setMongoDbName(String mongoDbName) {
		this.mongoDbName = mongoDbName;
	}


	public String getMongoPort() {
		return mongoPort;
	}


	public void setMongoPort(String mongoPort) {
		this.mongoPort = mongoPort;
	}

}
