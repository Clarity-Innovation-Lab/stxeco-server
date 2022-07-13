package eco.stx.edao.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.client.ResourceAccessException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LogManager.getLogger(GlobalExceptionHandler.class);
	@ExceptionHandler(ResourceAccessException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleResourceAccessException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
		return e.getMessage();
	}

	@ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleException(HttpServletRequest request, HttpServletResponse response, Exception e) {
		// return new ResponseEntity<Object>(e.getMessage(), HttpStatus.BAD_REQUEST);
		response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
		return e.getMessage();
	}

	@ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleResourceAccessException(HttpServletRequest request, HttpServletResponse response,
			RuntimeException e) {
		response.setStatus(HttpStatus.SERVICE_UNAVAILABLE.value());
		logger.error(e);
		return e.getMessage();
	}

}
