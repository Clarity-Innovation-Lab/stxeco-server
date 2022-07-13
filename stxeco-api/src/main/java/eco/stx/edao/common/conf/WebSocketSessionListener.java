package eco.stx.edao.common.conf;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

@Component
public class WebSocketSessionListener {
	private static final Logger logger = LogManager.getLogger(WebSocketSessionListener.class);
	private List<String> connectedClientId = new ArrayList<String>();

	@EventListener
	public void connectionEstablished(SessionConnectedEvent sce) {
		MessageHeaders msgHeaders = sce.getMessage().getHeaders();
		Principal princ = (Principal) msgHeaders.get("simpUser");
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(sce.getMessage());
		List<String> nativeHeaders = sha.getNativeHeader("userId");
		if (nativeHeaders != null) {
			String userId = nativeHeaders.get(0);
			connectedClientId.add(userId);
			if (logger.isDebugEnabled()) {
				logger.debug("Connessione websocket stabilita. ID Utente " + userId);
			}
		} else if (princ != null) {
			String userId = princ.getName();
			connectedClientId.add(userId);
			if (logger.isDebugEnabled()) {
				logger.debug("Connessione websocket stabilita. ID Utente " + userId);
			}
		}
	}

	@EventListener
	public void webSockectDisconnect(SessionDisconnectEvent sde) {
		MessageHeaders msgHeaders = sde.getMessage().getHeaders();
		Principal princ = (Principal) msgHeaders.get("simpUser");
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(sde.getMessage());
		List<String> nativeHeaders = sha.getNativeHeader("userId");
		if (nativeHeaders != null) {
			String userId = nativeHeaders.get(0);
			connectedClientId.remove(userId);
			if (logger.isDebugEnabled()) {
				logger.debug("Connessione websocket stabilita. ID Utente " + userId);
			}
		} else if (princ != null) {
			String userId = princ.getName();
			connectedClientId.remove(userId);
			if (logger.isDebugEnabled()) {
				logger.debug("Connessione websocket stabilita. ID Utente " + userId);
			}
		}
	}

	public List<String> getConnectedClientId() {
		return connectedClientId;
	}

	public void setConnectedClientId(List<String> connectedClientId) {
		this.connectedClientId = connectedClientId;
	}

}
