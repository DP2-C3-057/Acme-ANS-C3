
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.realms.customers.Customer;

@GuiService
public class CustomerBookingCreateService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;
		String method = super.getRequest().getMethod();

		try {
			if (method.equals("GET"))
				status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
			else if (method.equals("POST")) {
				Integer flightId = super.getRequest().getData("flight", int.class);
				String travelClassValue = super.getRequest().getData("travelClass", String.class);

				Flight flight = flightId == null ? null : this.repository.findFlightById(flightId);
				boolean validTravelClass = false;
				try {
					TravelClass.valueOf(travelClassValue);
					validTravelClass = true;
				} catch (Exception e) {
					validTravelClass = false;
				}

				status = flight != null && !flight.isDraftMode() && validTravelClass && super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
			}
		} catch (Exception e) {
			status = false;
		}

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		Booking booking;
		Customer customer;

		customer = (Customer) super.getRequest().getPrincipal().getActiveRealm();

		booking = new Booking();
		booking.setDraftMode(true);
		booking.setCustomer(customer);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());

		super.getBuffer().addData(booking);
	}

	@Override
	public void bind(final Booking booking) {
		super.bindObject(booking, "locatorCode", "travelClass", "lastCardNibble", "flight");
	}

	@Override
	public void validate(final Booking booking) {
		if (booking.getLocatorCode() != null) {
			Booking existing = this.repository.findByLocatorCode(booking.getLocatorCode());
			if (existing != null)
				super.state(false, "locatorCode", "customer.booking.form.error.duplicated-locator-code");
		}
	}

	@Override
	public void perform(final Booking booking) {
		this.repository.save(booking);
	}

	@Override
	public void unbind(final Booking booking) {
		Dataset dataset;
		SelectChoices flightChoice;
		SelectChoices classChoise;
		Collection<Flight> flights;

		flights = this.repository.findAllFlightsDraftModeFalse();
		flightChoice = SelectChoices.from(flights, "id", booking.getFlight());
		classChoise = SelectChoices.from(TravelClass.class, booking.getTravelClass());

		dataset = super.unbindObject(booking, "locatorCode", "purchaseMoment", "travelClass", "lastCardNibble", "flight", "draftMode");
		dataset.put("bookingCost", booking.getBookingCost());
		dataset.put("classChoise", classChoise);
		dataset.put("flightChoice", flightChoice);
		super.getResponse().addData(dataset);
	}
}
