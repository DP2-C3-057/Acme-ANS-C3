
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.entities.passengers.Passenger;
import acme.realms.customers.Customer;

@GuiService
public class CustomerBookingDeleteService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		Booking booking;
		Customer customer;

		masterId = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(masterId);
		customer = booking == null ? null : booking.getCustomer();
		status = booking != null && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		int id;

		id = super.getRequest().getData("id", int.class);
		booking = this.repository.findBookingById(id);

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		boolean status = true;
		int id;
		id = super.getRequest().getData("id", int.class);
		Collection<Passenger> passengers = this.repository.findPassengersByBookingId(id);
		if (!passengers.isEmpty())
			for (Passenger passenger : passengers)
				if (!passenger.isDraftMode()) {
					status = false;
					break;
				}
		super.state(status, "*", "customer.booking.delete.published-passengers");
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.deleteBookingRecordsByBookingId(booking.getId());
		this.repository.delete(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices classChoise;
		SelectChoices flightChoice;
		Collection<Flight> flights;

		flights = this.repository.findAllFlightsDraftModeFalse();
		flightChoice = SelectChoices.from(flights, "id", booking.getFlight());
		classChoise = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "lastCardNibble", "draftMode");
		dataset.put("classChoise", classChoise);
		dataset.put("bookingCost", booking.getBookingCost());
		dataset.put("flightChoice", flightChoice);
		super.getResponse().addData(dataset);
	}
}
