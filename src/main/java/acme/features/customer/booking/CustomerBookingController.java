
package acme.features.customer.booking;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.controllers.AbstractGuiController;
import acme.client.controllers.GuiController;
import acme.entities.bookings.Booking;
import acme.realms.customers.Customer;

@GuiController
public class CustomerBookingController extends AbstractGuiController<Customer, Booking> {

	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingListService	listService;

	@Autowired
	private CustomerBookingShowService	showService;
	//
	//	@Autowired
	//	private CustomerBookingCreateService	createService;
	//
	//	@Autowired
	//	private CustomerBookingUpdateService	updateService;
	//
	//	@Autowired
	//	private CustomerBookingDeleteService	deleteService;
	//
	//	@Autowired
	//	private CustomerBookingPublishService	publishService;

	// Constructors -----------------------------------------------------------


	@PostConstruct
	protected void initialise() {
		super.addBasicCommand("list", this.listService);
		//		super.addBasicCommand("show", this.showService);
		//		super.addBasicCommand("create", this.createService);
		//		super.addBasicCommand("update", this.updateService);
		//		super.addBasicCommand("delete", this.deleteService);
		//
		//		super.addCustomCommand("publish", "update", this.publishService);
	}
}
