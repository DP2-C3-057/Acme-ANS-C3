
package acme.features.administrator.airline;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.principals.Administrator;
import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.airlines.Airline;

@GuiController
public class AdministratorAirlineController extends AbstractGuiController<Administrator, Airline> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private AdministratorAirlineListService		listService;

	@Autowired
	private AdministratorAirlineShowService		showService;

	@Autowired
	private AdministratorAirlineCreateService	createService;

	@Autowired
	private AdministratorAirlineUpdateService	updateService;

	//	@Autowired
	//	private AdministratorAirlineDeleteService	deleteService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		super.addBasicCommand("show", this.showService);
		super.addBasicCommand("create", this.createService);
		super.addBasicCommand("update", this.updateService);
		//		super.addBasicCommand("delete", this.deleteService);

	}
}
