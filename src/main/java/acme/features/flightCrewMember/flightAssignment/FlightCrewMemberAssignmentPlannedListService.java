
package acme.features.flightCrewMember.flightAssignment;

import java.util.Collection;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberAssignmentPlannedListService extends AbstractGuiService<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentRepository repository;


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		int memberId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Date now = MomentHelper.getCurrentMoment();
		Collection<FlightAssignment> planned = this.repository.assignmentsWithPlannedLegs(memberId, now);
		super.getBuffer().addData(planned);
	}

	@Override
	public void unbind(final FlightAssignment assignment) {
		Dataset dataset;

		dataset = super.unbindObject(assignment, "duty", "moment", "currentStatus");
		super.addPayload(dataset, assignment, "remarks", "draftMode", "flightCrewMember.identity.fullName", "leg.status");

		super.getResponse().addData(dataset);
	}

}
