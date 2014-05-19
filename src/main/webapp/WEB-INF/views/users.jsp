<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false"%>
<c:set var="url" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>Home</title>
</head>

<body>


	<div class="row">
		<div class="col-lg-12">
			<h1>
				Dashboard <small>Statistics Overview</small>
			</h1>
			<ol class="breadcrumb">
				<li class="active"><i class="fa fa-dashboard"></i> Dashboard</li>
			</ol>
		</div>
	</div>
	<!-- /.row -->

	<div class="row">
		<div class="col-lg-12">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<i class="fa fa-money"></i>Registered Users for PushNotification
					</h3>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table
							class="table table-bordered table-hover table-striped tablesorter">
							<thead>
								<tr>
									<th>GCM-ID <i class="fa fa-sort"></i></th>
									<th>Info<i class="fa fa-sort"></i></th>
									<th>Action<i class="fa fa-sort"></i></th>
								</tr>
							</thead>
							<tbody>
								<c:forEach  items="${users}" var="user">
									<tr>
										<td>${user.key}</td>
										<td></td>
										<td><a href="${url}/admin/test?regId=${user.key}">end Message</a></td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
					<div class="text-right">
						<a href="#">View All Transactions <i
							class="fa fa-arrow-circle-right"></i></a>
					</div>
				</div>
			</div>
		</div>
	</div>
	<!-- /.row -->

</body>
</html>