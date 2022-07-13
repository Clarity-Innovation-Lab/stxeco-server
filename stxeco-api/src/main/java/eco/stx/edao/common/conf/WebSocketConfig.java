package eco.stx.edao.common.conf;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer  {

    private static final Logger logger = LogManager.getLogger(WebSocketConfig.class);
    
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws").setAllowedOrigins("*").withSockJS();
		registry.addEndpoint("/news").setAllowedOrigins("*").withSockJS();
		registry.addEndpoint("/api-news").setAllowedOrigins("*").withSockJS();
    }

    public void configureMessageBroker(MessageBrokerRegistry config) {
		logger.info("====================================================================================");
		SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd 'at' HH:mm:ss z");
		Date date = new Date(System.currentTimeMillis());
		logger.info("starting spring application " + formatter.format(date));
		logger.info("====================================================================================");

		config.enableSimpleBroker("/topic", "/queue/");   // Enables a simple in-memory broker
		config.setApplicationDestinationPrefixes("/app");

//        registry.enableStompBrokerRelay("/topic", "/queue").setRelayHost("rabbit1").setSystemLogin("mijobc").setSystemPasscode("b1dl0g1x");        
//        registry.setApplicationDestinationPrefixes("/app");
    }
}