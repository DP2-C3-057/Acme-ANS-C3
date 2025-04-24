<%--
- form.jsp
-
- Copyright (C) 2012-2025 Rafael Corchuelo.
-
- In keeping with the traditional purpose of furthering education and research, it is
- the policy of the copyright owner to permit non-commercial use and redistribution of
- this software. It has been tested carefully, but it is not guaranteed for any particular
- purposes.  The copyright owner does not offer any warranties or representations, nor do
- they accept any liabilities with respect to them.
--%>

<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<h2>
	<acme:print code="manager.dashboard.form.title.general-indicators"/>
</h2>

<table class="table table-sm">
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.ranking-position"/>
		</th>
		<td>
			<acme:print value="${rankingPosition}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.years-to-retire"/>
		</th>
		<td>
			<acme:print value="${yearsToRetire}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.on-time-delayed-legs-ratio"/>
		</th>
		<td>
			<acme:print value="${onTimeDelayedLegsRatio}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.most-popular-airport"/>
		</th>
		<td>
			<acme:print value="${mostPopularAirport}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.less-popular-airport"/>
		</th>
		<td>
			<acme:print value="${lessPopularAirport}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.average-cost-flight"/>
		</th>
		<td>
			<acme:print value="${averageCostFlight}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.minimum-cost-flight"/>
		</th>
		<td>
			<acme:print value="${minimumCostFlight}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.maximum-cost-flight"/>
		</th>
		<td>
			<acme:print value="${maximumCostFlight}"/>
		</td>
	</tr>
	<tr>
		<th scope="row">
			<acme:print code="manager.dashboard.form.label.standart-deviation-cost"/>
		</th>
		<td>
			<acme:print value="${standartDeviationCost}"/>
		</td>
	</tr>
</table>

<h2>
	<acme:print code="manager.dashboard.form.title.leg-statuses"/>
</h2>

<div>
	<canvas id="canvas"></canvas>
</div>

<script type="text/javascript">
	$(document).ready(function() {
		var data = {
			labels : [
					"ON_TIME", "DELAYED", "CANCELLED", "LANDED"
			],
			datasets : [
				{
					data : [
						<jstl:out value="${onTimeLegs}"/>, 
						<jstl:out value="${delayedLegs}"/>, 
						<jstl:out value="${cancelledLegs}"/>,
						<jstl:out value="${landedLegs}"/>
					]
				}
			]
		};
		var options = {
			scales : {
				yAxes : [
					{
						ticks : {
							suggestedMin : 0.0,
							suggestedMax : 1.0
						}
					}
				]
			},
			legend : {
				display : false
			}
		};
	
		var canvas, context;
	
		canvas = document.getElementById("canvas");
		context = canvas.getContext("2d");
		new Chart(context, {
			type : "bar",
			data : data,
			options : options
		});
	});
</script>

<acme:return/>

