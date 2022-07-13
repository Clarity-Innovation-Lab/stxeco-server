package eco.stx.edao.common.conf;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

@Component
public class JWTHandlerInterceptor implements HandlerInterceptor {

    private static final Logger logger = LogManager.getLogger(JWTHandlerInterceptor.class);

    @Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
        try {
	         // printHeaders(request);
	         // printHeaders(response);
        } catch (Exception e) {
            throw e;
        }
		HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
	}

	@Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    private void printHeaders(HttpServletRequest request) {
        logger.info("PRE remote host: " + request.getRemoteHost());
        logger.info("PRE request url: " + request.getRequestURL());
        logger.info("PRE Method: " + request.getMethod());
        Iterator<String> headers = request.getHeaderNames().asIterator();
        while (headers.hasNext()) {
        	String header = headers.next();
        	logger.info("HEADERS REQUEST: " + header + " = " + request.getHeader(header));
        }
    }
    
    private void printHeaders(HttpServletResponse response) {
        for (String header : response.getHeaderNames()) {
        	logger.info("HEADERS RESPONSE: " + header + ":" + response.getHeader(header));
        }
    }
}
