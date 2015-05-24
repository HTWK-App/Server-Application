$(document).ready(function() {
	console.log(jspArgs.get('requestTypes'));
	Morris.Donut({
		element : 'morris-chart-requestTypes',
		data : jspArgs.get('requestTypes')
	});
});