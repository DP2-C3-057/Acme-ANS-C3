
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentCompletedListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Collection<FlightAssignment> assignments = this.repository.assignmentsWithCompletedLegs(memberId);
		super.getBuffer().addData(assignments);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {

		Dataset dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus");

		super.addPayload(dataset, assignment, "remarks", "draftMode", "flightCrewMember.identity.fullName", "leg.status");

		super.getResponse().addData(dataset);
	}

}
