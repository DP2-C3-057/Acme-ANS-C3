
package acme.features.flightCrewMember.activityLog;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.activityLogs.ActivityLog;
import acme.realms.flightCrewMembers.FlightCrewMember;

@GuiController
public class FlightCrewMemberActivityLogController extends AbstractGuiController<FlightCrewMember, ActivityLog> {

	@Autowired
	private FlightCrewMemberActivityLogListService		listService;

	@Autowired
	private FlightCrewMemberActivityLogShowService		showService;

	@Autowired
	private FlightCrewMemberActivityLogUpdateService	updateService;


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("update", this.updateService);
	}
}
