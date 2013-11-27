package com.htwk.app;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class ErrorController {

	@RequestMapping(value = "/error/404", method = RequestMethod.GET)
    public final Map<String, Object> error404(HttpServletRequest req, Exception e) throws Exception{
       throw new NotFoundException();
    }
	
	@RequestMapping(value = "/error/500", method = RequestMethod.GET)
    public final Map<String, Object> error500(HttpServletRequest req, Exception e) throws Exception{
       throw new NotFoundException();
    }
}
