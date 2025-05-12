
package acme.features.flightCrewMember.activityLog;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.activityLogs.ActivityLog;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiService
public class FlightCrewMemberActivityLogCreateService extends AbstractGuiService<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogRepository repository;


	@Override
	public void authorise() {
		boolean status;
		int assignmentId;
		FlightAssignment assignment;

		assignmentId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findFlightAssignmentById(assignmentId);
		status = assignment != null && assignment.isDraftMode() && super.getRequest().getPrincipal().hasRealm(assignment.getFlightCrewMember());

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		ActivityLog log;
		int masterId;
		FlightAssignment assignment;

		masterId = super.getRequest().getData("masterId", int.class);
		assignment = this.repository.findFlightAssignmentById(masterId);

		log = new ActivityLog();
		log.setFlightAssignment(assignment);
		log.setRegistrationMoment(MomentHelper.getCurrentMoment());
		log.setDraftMode(false);

		super.getBuffer().addData(log);
	}

	@Override
	public void bind(final ActivityLog log) {
		super.bindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment");
	}

	@Override
	public void validate(final ActivityLog log) {
		;
	}

	@Override
	public void perform(final ActivityLog log) {
		this.repository.save(log);
	}

	@Override
	public void unbind(final ActivityLog log) {
		Dataset dataset;

		SelectChoices assignmentChoice;
		Collection<FlightAssignment> assignments;

		assignments = this.repository.findAllFlightAssignments();
		assignmentChoice = SelectChoices.from(assignments, "id", log.getFlightAssignment());

		dataset = super.unbindObject(log, "registrationMoment", "typeOfIncident", "description", "severityLevel", "flightAssignment");
		dataset.put("assignmentChoice", assignmentChoice);
		dataset.put("masterId", super.getRequest().getData("masterId", int.class));

		super.getResponse().addData(dataset);
	}
}
