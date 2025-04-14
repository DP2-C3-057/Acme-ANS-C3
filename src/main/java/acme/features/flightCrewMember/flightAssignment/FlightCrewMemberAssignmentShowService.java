
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.CurrentStatus;
import acme.entities.flightAssignments.Duty;
import acme.entities.flightAssignments.FlightAssignment;
import acme.entities.legs.Leg;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentShowService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		FlightCrewMember member;
		FlightAssignment assignment;

		masterId = super.getRequest().getData("id", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);
		member = assignment == null ? null : assignment.getFlightCrewMember();
		status = super.getRequest().getPrincipal().hasRealm(member) || assignment != null && assignment.getDraftMode() == true;

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
		legChoice = SelectChoices.from(legs, "id", assignment.getLeg());

		flightCrewMembers = this.repository.findAllFlightCrewMembers();
		flightCrewMemberChoice = SelectChoices.from(flightCrewMembers, "id", assignment.getFlightCrewMember());

		dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus", "remarks", "draftMode", "leg", "flightCrewMember");
		dataset.put("dutyChoice", dutyChoice);
		dataset.put("currentStatusChoice", currentStatusChoice);
		dataset.put("legChoice", legChoice);
		dataset.put("flightCrewMemberChoice", flightCrewMemberChoice);

		super.getResponse().addData(dataset);
	}
}
