<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:list>
	<acme:list-column code="manager.leg.list.label.scheduledDeparture" path="scheduledDeparture" width="20%"/>
	<acme:list-column code="manager.leg.list.label.scheduledArrival" path="scheduledArrival" width="20%"/>
	<acme:list-column code="manager.leg.list.label.departureAirport" path="departureAirport" width="20%" sortable="false"/>
	<acme:list-column code="manager.leg.list.label.arrivalAirport" path="arrivalAirport" width="20%" sortable="false"/>
	<acme:list-column code="manager.leg.list.label.status" path="status" width="20%" sortable="false"/>
</acme:list>

<jstl:if test="${showCreate}">
	<acme:button code="manager.leg.list.button.create" action="/manager/leg/create?masterId=${masterId}"/>
</jstl:if>