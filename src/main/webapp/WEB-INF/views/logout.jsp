<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false"%>
<c:set var="url" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>Home</title>
<body>


	<div class="row">
		<div class="col-lg-12">
			You are now logged out!! <a
				href="${pageContext.request.contextPath}/login">go back</a>
		</div>
	</div>
</body>
</html>