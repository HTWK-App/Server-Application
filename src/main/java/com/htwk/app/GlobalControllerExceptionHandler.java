package com.htwk.app;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.htwk.app.repository.StatisticRepository;

@ControllerAdvice
public class GlobalControllerExceptionHandler {

	private static final Logger logger = LoggerFactory.getLogger(GlobalControllerExceptionHandler.class);
	
	public static final String DEFAULT_ERROR_VIEW = "error";

	@Autowired 
	private StatisticRepository stats;
	
	@ExceptionHandler(value = Exception.class)
	public @ResponseBody
	Map<String, Object> defaultErrorHandler(HttpServletRequest req, Exception e) throws Exception {
		// If the exception is annotated with @ResponseStatus rethrow it and let
		// the framework handle it - like the OrderNotFoundException example
		// at the start of this post.
		// AnnotationUtils is a Spring Framework utility class.
		logger.error("Exception for uri {} : {}", req.getRequestURL(), ExceptionUtils.getStackTrace(e));
		stats.incrementErrorRequest();
		
		if (AnnotationUtils.findAnnotation(e.getClass(), ResponseStatus.class) != null)
			throw e;

		// Otherwise setup and send the user to a default error-view.
		Map<String, Object> errorResponse = new HashMap<String, Object>();
		errorResponse.put("url", req.getRequestURL());
		errorResponse.put("exception", e.toString());
		errorResponse.put("message", e.getMessage());
		return errorResponse;
	}

}
