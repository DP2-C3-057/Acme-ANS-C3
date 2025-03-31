<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.flightCrewDuty" path="duty"/>
	<acme:input-moment code="flight-crew-member.flight-assignment.form.label.lastUpdate" path="moment"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.assignmentStatus" path="currentStatus"/>
	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks"/>
	<acme:input-checkbox code="flight-crew-member.flight-assignment.form.label.draftMode" path="draftMode"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.flightCrewMember" path="flightCrewMember.identity.fullName" readonly="true"/>
	<acme:input-textbox code="flight-crew-member.flight-assignment.form.label.legStatus" path="leg.status" readonly="true"/>

	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.return" action="/flightCrewMember/flightAssignment/list"/>
		</jstl:when>

		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flightCrewMember/flightAssignment/update"/>
			<acme:submit code="flight-crew-member.flight-assignment.form.button.delete" action="/flightCrewMember/flightAssignment/delete"/>
		</jstl:when>

		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.create" action="/flightCrewMember/flightAssignment/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>