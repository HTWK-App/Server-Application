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
		<div class="col-lg-3">
			<div class="panel panel-info">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-6">
							<i class="fa fa-comments fa-5x"></i>
						</div>
						<div class="col-xs-6 text-right">
							<p class="announcement-heading">456</p>
							<p class="announcement-text">New Mentions!</p>
						</div>
					</div>
				</div>
				<a href="#">
					<div class="panel-footer announcement-bottom">
						<div class="row">
							<div class="col-xs-6">View Mentions</div>
							<div class="col-xs-6 text-right">
								<i class="fa fa-arrow-circle-right"></i>
							</div>
						</div>
					</div>
				</a>
			</div>
		</div>
		<div class="col-lg-3">
			<div class="panel panel-warning">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-6">
							<i class="fa fa-check fa-5x"></i>
						</div>
						<div class="col-xs-6 text-right">
							<p class="announcement-heading">12</p>
							<p class="announcement-text">To-Do Items</p>
						</div>
					</div>
				</div>
				<a href="#">
					<div class="panel-footer announcement-bottom">
						<div class="row">
							<div class="col-xs-6">Complete Tasks</div>
							<div class="col-xs-6 text-right">
								<i class="fa fa-arrow-circle-right"></i>
							</div>
						</div>
					</div>
				</a>
			</div>
		</div>
		<div class="col-lg-3">
			<div class="panel panel-danger">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-6">
							<i class="fa fa-tasks fa-5x"></i>
						</div>
						<div class="col-xs-6 text-right">
							<p class="announcement-heading">18</p>
							<p class="announcement-text">Crawl Errors</p>
						</div>
					</div>
				</div>
				<a href="#">
					<div class="panel-footer announcement-bottom">
						<div class="row">
							<div class="col-xs-6">Fix Issues</div>
							<div class="col-xs-6 text-right">
								<i class="fa fa-arrow-circle-right"></i>
							</div>
						</div>
					</div>
				</a>
			</div>
		</div>
		<div class="col-lg-3">
			<div class="panel panel-success">
				<div class="panel-heading">
					<div class="row">
						<div class="col-xs-6">
							<i class="fa fa-comments fa-5x"></i>
						</div>
						<div class="col-xs-6 text-right">
							<p class="announcement-heading">56</p>
							<p class="announcement-text">New Orders!</p>
						</div>
					</div>
				</div>
				<a href="#">
					<div class="panel-footer announcement-bottom">
						<div class="row">
							<div class="col-xs-6">Complete Orders</div>
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

</body>
</html>