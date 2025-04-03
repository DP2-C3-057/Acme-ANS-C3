
package acme.features.flightCrewMember.flightAssignment;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.flightAssignments.FlightAssignment;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiController
public class FlightCrewMemberAssignmentController extends AbstractGuiController<FlightCrewMember, FlightAssignment> {

	@Autowired
	private FlightCrewMemberAssignmentCompletedListService	listCompletedService;

	@Autowired
	private FlightCrewMemberAssignmentPlannedListService	listPlannedService;

	@Autowired
	private FlightCrewMemberAssignmentShowService			showService;

	@Autowired
	private FlightCrewMemberAssignmentUpdateService			updateService;

	@Autowired
	private FlightCrewMemberAssignmentCreateService			createService;

	@Autowired
	private FlightCrewMemberAssignmentDeleteService			deleteService;

	@Autowired
	private FlightCrewMemberAssignmentPublishService		publishService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listCompletedService);
		super.addCustomCommand("list-planned", "list", this.listPlannedService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("delete", this.deleteService);
		super.addCustomCommand("publish", "update", this.publishService);
	}

}
