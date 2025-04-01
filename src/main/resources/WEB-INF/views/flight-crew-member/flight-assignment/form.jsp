<%@page%>
<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.flightCrewDuty" path="duty" choices="${dutyChoice}"/>
 	<acme:input-moment code="flight-crew-member.flight-assignment.form.label.lastUpdate" path="moment" readonly="true"/>
 	<acme:input-select code="flight-crew-member.flight-assignment.form.label.assignmentStatus" path="currentStatus" choices="${currentStatusChoice}"/>
	<acme:input-textarea code="flight-crew-member.flight-assignment.form.label.remarks" path="remarks"/>
	<acme:input-checkbox code="flight-crew-member.flight-assignment.form.label.draftMode" path="draftMode"/>
	<acme:input-select code="flight-crew-member.flight-assignment.form.label.flightCrewMember" path="flightCrewMember" choices="${flightCrewMemberChoice}"/>
 	<acme:input-select code="flight-crew-member.flight-assignment.form.label.leg" path="leg" choices="${legChoice}"/>
 	
	<jstl:choose>
		<jstl:when test="${_command == 'show' && draftMode == false}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.return" action="/flightCrewMember/flightAssignment/list"/>
		</jstl:when>

		<jstl:when test="${acme:anyOf(_command, 'show|update|delete') && draftMode == true}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.update" action="/flight-crew-member/flight-assignment/update"/>
 			<acme:submit code="flight-crew-member.flight-assignment.form.button.delete" action="/flight-crew-member/flight-assignment/delete"/>
			<acme:button code="flight-crew-member.flight-assignment.form.button.activity-log" action="/flight-crew-member/activity-log/list?masterId=${id}"/>
		</jstl:when>

		<jstl:when test="${_command == 'create'}">
			<acme:submit code="flight-crew-member.flight-assignment.form.button.create" action="/flight-crew-member/flight-assignment/create"/>
		</jstl:when>
	</jstl:choose>
</acme:form>