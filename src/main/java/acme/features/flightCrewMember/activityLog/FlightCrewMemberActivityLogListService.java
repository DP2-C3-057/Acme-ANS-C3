
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogListService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		int masterId;
		int memberId;
		FlightAssignment flightAssignment;
		boolean status;

		masterId = super.getRequest().getData("masterId", int.class);
		memberId = super.getRequest().getPrincipal().getActiveRealm().getId();
		flightAssignment = this.repository.findFlightAssignmentById(masterId);

		status = flightAssignment.getFlightCrewMember().getId() == memberId;
		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Collection<ActivityLog> logs;
		int masterId;

		masterId = super.getRequest().getData("masterId", int.class);
		logs = this.repository.findActivityLogsByAssignmentId(masterId);
		super.getBuffer().addData(logs);
	}

	@Override
	public void unbind(final ActivityLog activityLog) {
		Dataset dataset;
		int masterId;

		dataset = super.unbindObject(activityLog, "registrationMoment", "typeOfIncident", "severityLevel");
		masterId = super.getRequest().getData("masterId", int.class);

		super.addPayload(dataset, activityLog, "draftMode");
		super.getResponse().addGlobal("masterId", masterId);
		super.getResponse().addData(dataset);
	}
}
