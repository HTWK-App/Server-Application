package com.htwk.app.utils.bean;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.omg.CORBA.SystemException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.htwk.app.repository.StatisticRepository;

public class RequestStatisticInterceptor extends HandlerInterceptorAdapter {

	private static final Logger logger = LoggerFactory.getLogger(RequestStatisticInterceptor.class);

	@Autowired
	private StatisticRepository stats;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		try {

			long startTime = System.currentTimeMillis();
			request.setAttribute("startTime", startTime);
			stats.incrementTotalRequests();
			
			String requestUri = request.getRequestURI();
			logger.info("Intercepting: " + requestUri);
			if (requestUri.contains("staff")) {
				stats.incrementStaffRequest();
			} else if (requestUri.contains("building")) {
				stats.incrementBuildingRequest();
			} else if (requestUri.contains("sport")) {
				stats.incrementSportRequest();
			} else if (requestUri.contains("news")) {
				stats.incrementNewsRequest();
			} else if (requestUri.contains("mailbox")) {
				stats.incrementMailBoxRequest();
			} else if (requestUri.contains("weather")) {
				stats.incrementWeatherRequest();
			} else if (requestUri.contains("timetable")) {
				stats.incrementTimetableRequest();
			} else if (requestUri.contains("mensa")) {
				String[] requestParts= requestUri.split("/");
				int location=Integer.parseInt(requestParts[3]);
				stats.incrementMensaRequest(location);
			}

			RequestMethod type = RequestMethod.valueOf(request.getMethod());
			stats.incrementRequestType(type);

			return true;
		} catch (SystemException e) {
			logger.error("failure while request-statistics");
			return false;
		}
	}

	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		try {
			long startTime = (Long) request.getAttribute("startTime");
			long endTime = System.currentTimeMillis();
			long executionTime = endTime - startTime;
			response.addHeader("executionTime:", ""+executionTime);
			stats.addExecutionTime(executionTime);

		} catch (SystemException e) {
			logger.error("failure while request-statistics");
		}
	}
}
