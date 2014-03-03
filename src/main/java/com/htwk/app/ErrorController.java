package com.htwk.app;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

@Controller
public class ErrorController {

	@ResponseStatus(value = HttpStatus.NOT_FOUND)
	@RequestMapping(value = "/error/404", method = RequestMethod.GET)
    public final Map<String, Object> error404(HttpServletRequest req, Exception e) throws Exception{
       throw new NotFoundException(req);
    }
	
	@ResponseStatus(value = HttpStatus.METHOD_NOT_ALLOWED)
	@RequestMapping(value = "/error/405", method = RequestMethod.GET)
    public final Map<String, Object> error405(HttpServletRequest req, Exception e) throws Exception{
       throw new HttpRequestMethodNotSupportedException(req.getMethod());
    }
	
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	@RequestMapping(value = "/error/500", method = RequestMethod.GET)
    public final Map<String, Object> error500(HttpServletRequest req, Exception e) throws Exception{
       throw new NotFoundException(req);
    }
}
