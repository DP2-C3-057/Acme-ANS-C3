<%@page%>

<%@taglib prefix="jstl" uri="http://java.sun.com/jsp/jstl/core"%>
<%@taglib prefix="acme" uri="http://acme-framework.org/"%>

<acme:form> 
	<acme:input-select code="customer.bookingRecord.form.label.booking.locatorCode" path="booking.locatorCode" choices="${bookingChoice}"/>
	<acme:input-select code="customer.bookingRecord.form.label.passenger.fullName" path="passenger.fullName" choices="${passengerChoice}"/>
	
	<jstl:choose>
		<jstl:when test="${acme:anyOf(_command, 'show|update|delete|publish') && draftMode == true}">
			<acme:submit code="customer.bookingRecord.form.button.update" action="/customer/booking-record/update"/>
			<acme:submit code="customer.bookingRecord.form.button.delete" action="/customer/booking-record/delete"/>
			<acme:submit code="customer.bookingRecord.form.button.publish" action="/customer/booking-record/publish"/>
		</jstl:when>
		<jstl:when test="${_command == 'create'}">
			<acme:submit code="customer.bookingRecord.form.button.create" action="/customer/booking-record/create"/>
		</jstl:when>		
	</jstl:choose>
</acme:form>
