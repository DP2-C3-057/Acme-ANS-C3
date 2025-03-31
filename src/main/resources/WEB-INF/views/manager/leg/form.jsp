<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-textbox code="manager.leg.form.label.flightNumber" path="flightNumber"/>
	<acme:input-moment code="manager.leg.form.label.scheduledDeparture" path="scheduledDeparture"/>
	<acme:input-moment code="manager.leg.form.label.scheduledArrival" path="scheduledArrival"/>
	<acme:input-textbox code="manager.leg.form.label.duration" path="duration" readonly="true"/>
	<acme:input-textbox code="manager.leg.form.label.status" path="status"/>
	<acme:input-textbox code="manager.leg.form.label.departureAirport" path="departureAirport"/>
	<acme:input-textbox code="manager.leg.form.label.arrivalAirport" path="arrivalAirport"/>
	<acme:input-textbox code="manager.leg.form.label.flight" path="flight"/>
	<acme:input-textbox code="manager.leg.form.label.aircraft" path="aircraft"/>
	<acme:input-checkbox code="manager.leg.form.label.draftMode" path="draftMode" readonly="true"/>

	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="manager.flight.form.button.update" action="/manager/flight/update"/>
			<acme:submit code="manager.flight.form.button.delete" action="/manager/flight/delete"/>
			<acme:submit code="manager.flight.form.button.publish" action="/manager/flight/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="manager.flight.form.button.create" action="/manager/flight/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
