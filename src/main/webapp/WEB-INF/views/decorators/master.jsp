<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8" isELIgnored="false"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="decorator"
	uri="http://www.opensymphony.com/sitemesh/decorator"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en" lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<title><decorator:title /></title>

<!-- Always force latest IE rendering engine (even in intranet) & Chrome Frame -->
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta name="viewport" content="width=device-width, initial-scale=1.0" />
<meta name="description" content="" />
<meta name="author" content="" />
<decorator:head />
<%@ include file="/WEB-INF/views/includes/style.jsp"%>

</head>
<body>
	<!-- Wrap all page content here -->
	<div id="wrapper">
		<div class="navbar navbar-default navbar-fixed-top" role="navigation"
			id="navigation">
			<%@ include file="/WEB-INF/views/includes/navigation.jsp"%>
		</div>

		<div id="page-wrapper" class="container">
			<decorator:body />
		</div>

	</div>
	<div id="javascript">
		<%@ include file="/WEB-INF/views/includes/script.jsp"%>
	</div>
</body>
</html>
