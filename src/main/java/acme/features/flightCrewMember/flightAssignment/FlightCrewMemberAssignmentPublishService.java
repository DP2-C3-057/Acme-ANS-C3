
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.CurrentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMembers.AvailabilityStatus;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentPublishService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {
		FlightAssignment assignment;
		boolean status;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		status = assignment != null && assignment.getFlightCrewMember().getId() == super.getRequest().getPrincipal().getActiveRealm().getId();

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		FlightAssignment assignment;
		int id;

		id = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(id);

		super.getBuffer().addData(assignment);
	}

	@Override
	public void bind(final FlightAssignment assignment) {
		super.bindObject(assignment, "duty", "currentStatus", "remarks", "flightCrewMember", "leg");
	}

	@Override
	public void validate(final FlightAssignment assignment) {
		if (assignment.getLeg() == null || assignment.getDuty() == null)
			return; // Bean Validation se encargará de los mensajes

		int flightCrewMemberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		Leg leg = assignment.getLeg();

		boolean completedLeg = MomentHelper.isBefore(leg.getScheduledArrival(), MomentHelper.getCurrentMoment());
		boolean availableMember = this.repository.findFlightCrewMemberById(flightCrewMemberId).getAvailabilityStatus().equals(AvailabilityStatus.AVAILABLE);

		Collection<FlightAssignment> pilotAssignments = this.repository.findFlightAssignmentByLegAndDuty(leg, Duty.PILOT);
		Collection<FlightAssignment> copilotAssignments = this.repository.findFlightAssignmentByLegAndDuty(leg, Duty.CO_PILOT);
		boolean hasPilot = !(assignment.getDuty().equals(Duty.PILOT) && pilotAssignments.size() + 1 >= 2);
		boolean hasCopilot = !(assignment.getDuty().equals(Duty.CO_PILOT) && copilotAssignments.size() + 1 >= 2);

		if (!this.getBuffer().getErrors().hasErrors("publish")) {
			super.state(!completedLeg, "leg", "acme.validation.flightassignment.leg.completed.message", assignment);
			super.state(availableMember, "flightCrewMember", "acme.validation.flightassignment.flightcrewmember.available.message", assignment);
			super.state(hasPilot, "duty", "acme.validation.flightassignment.duty.pilot.message", assignment);
			super.state(hasCopilot, "duty", "acme.validation.flightassignment.duty.copilot.message", assignment);
		}
	}

	@Override
	public void perform(final FlightAssignment assignment) {
		assignment.setDraftMode(false);
		this.repository.save(assignment);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;
		SelectChoices dutyChoice;
		SelectChoices currentStatusChoice;

		SelectChoices legChoice;
		Collection<Leg> legs;

		SelectChoices flightCrewMemberChoice;
		Collection<FlightCrewMember> flightCrewMembers;

		dutyChoice = SelectChoices.from(Duty.class, assignment.getDuty());
		currentStatusChoice = SelectChoices.from(CurrentStatus.class, assignment.getCurrentStatus());

		legs = this.repository.findAllLegsPublish();
		legChoice = SelectChoices.from(legs, "flightNumber", assignment.getLeg());

		flightCrewMembers = this.repository.findAllFlightCrewMembers();
		flightCrewMemberChoice = SelectChoices.from(flightCrewMembers, "employeeCode", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus", "remarks", "flightCrewMember", "leg", "draftMode");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("flightCrewMemberChoice", flightCrewMemberChoice);
		dataset.put("legChoice", legChoice);

		super.getResponse().addData(dataset);
	}
}
