<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page session="false"%>
<c:set var="url" value="${pageContext.request.contextPath}" />
<!DOCTYPE html>
<html lang="en">
<head>
<title>Home</title>
<script type="text/javascript"
	src="<%=request.getContextPath()%>/resources/js/custom.js"></script>
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
		
		<div class="col-lg-6">
			<div class="panel panel-success">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-6">
							<i class="fa fa-comments fa-5x"></i>
						</div>
						<div class="col-xs-6 text-right">
							<p class="announcement-heading">${total}</p>
							<p class="announcement-text">Requests Total</p>
						</div>
					</div>
				</div>
				<a href="#">
					<div class="panel-footer announcement-bottom">
						<div class="row">
<!-- 							<div class="col-xs-6">Complete Orders</div> -->
							<div class="col-xs-6 text-right">
								<i class="fa fa-arrow-circle-right"></i>
							</div>
						</div>
					</div>
				</a>
			</div>
		</div>
		<div class="col-lg-6">
			<div class="panel panel-danger">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-6">
							<i class="fa fa-tasks fa-5x"></i>
						</div>
						<div class="col-xs-6 text-right">
							<p class="announcement-heading">${errors}</p>
							<p class="announcement-text">Errors</p>
						</div>
					</div>
				</div>
				<a href="#">
					<div class="panel-footer announcement-bottom">
						<div class="row">
							<div class="col-xs-6 text-right">
								<i class="fa fa-arrow-circle-right"></i>
							</div>
						</div>
					</div>
				</a>
			</div>
		</div>
	</div>
	<!-- /.row -->

	<div class="row">
		<div class="col-lg-4">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<i class="fa fa-long-arrow-right"></i> RequestTypes
					</h3>
				</div>
				<div class="panel-body">
					<div id="morris-chart-requestTypes"></div>
				</div>
			</div>
		</div>
		<div class="col-lg-4">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<i class="fa fa-money"></i> Recent Requests
					</h3>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table
							class="table table-bordered table-hover table-striped tablesorter">
							<thead>
								<tr>
									<th>Endpoint<i class="fa fa-sort"></i></th>
									<th>Count<i class="fa fa-sort"></i></th>
								</tr>
							</thead>
							<tbody>
								<tr>
									<td>staff</td>
									<td>${staff}</td>
								</tr>
								<tr>
									<td>building</td>
									<td>${building}</td>
								</tr>
								<tr>
									<td>sport</td>
									<td>${sport}</td>
								</tr>
								<tr>
									<td>news</td>
									<td>${news}</td>
								</tr>
								<tr>
									<td>mensa</td>
									<td>${mensaTotal}</td>
								</tr>
								<tr>
									<td>mailbox</td>
									<td>${mailbox}</td>
								</tr>
								<tr>
									<td>weather</td>
									<td>${weather}</td>
								</tr>
								<tr>
									<td>timetable</td>
									<td>${timetable}</td>
								</tr>
								
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
		<div class="col-lg-4">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<i class="fa fa-money"></i>Mensa-Requests by location
					</h3>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table
							class="table table-bordered table-hover table-striped tablesorter">
							<thead>
								<tr>
									<th>Endpoint<i class="fa fa-sort"></i></th>
									<th>Count<i class="fa fa-sort"></i></th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${mensa}" var="location">
								<tr>
									<td>${location.key }</td>
									<td>${location.value }</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<div class="row">
	<div class="col-lg-12">
			<div class="panel panel-primary">
				<div class="panel-heading">
					<h3 class="panel-title">
						<i class="fa fa-money"></i>ExectutionTime
					</h3>
				</div>
				<div class="panel-body">
					<div class="table-responsive">
						<table
							class="table table-bordered table-hover table-striped tablesorter">
							<thead>
								<tr>
									<th>executionTime in ms<i class="fa fa-sort"></i></th>
								</tr>
							</thead>
							<tbody>
							<c:forEach items="${execution}" var="entry">
								<tr>
									<td>${entry}</td>
								</tr>
							</c:forEach>
							</tbody>
						</table>
					</div>
				</div>
			</div>
		</div>
	</div>
	<script type="text/javascript">
		var data = [];
		<c:forEach var="element" items="${types}">
		var element = new Array();
		element['label'] = "${element.key}";
		element['value'] = ${element.value};
		data.push(element);
		</c:forEach>
		jspArgs.set('requestTypes')(data);
	</script>
</body>
</html>