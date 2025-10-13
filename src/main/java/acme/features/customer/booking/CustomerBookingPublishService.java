
package acme.features.customer.booking;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.helpers.MomentHelper;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.bookings.TravelClass;
import acme.entities.flights.Flight;
import acme.entities.passengers.Passenger;
import acme.realms.customers.Customer;

@GuiService
public class CustomerBookingPublishService extends AbstractGuiService<Customer, Booking> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;

		try {
			int bookingId = super.getRequest().getData("id", int.class);
			Integer flightId = super.getRequest().getData("flight", int.class);
			String travelClassValue = super.getRequest().getData("travelClass", String.class);

			Booking booking = this.repository.findBookingById(bookingId);
			Customer customer = booking == null ? null : booking.getCustomer();
			Flight flight = flightId == null ? null : this.repository.findFlightById(flightId);

			boolean validTravelClass;
			try {
				TravelClass.valueOf(travelClassValue);
				validTravelClass = true;
			} catch (Exception e) {
				validTravelClass = false;
			}

			status = booking != null && booking.isDraftMode() && flight != null && !flight.isDraftMode() && validTravelClass && super.getRequest().getPrincipal().hasRealm(customer);

		} catch (Exception e) {
			status = false;
		}

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
		int id = super.getRequest().getData("id", int.class);

		// Validación de pasajeros
		Collection<Passenger> passengers = this.repository.findPassengersByBookingId(id);
		if (passengers.isEmpty())
			super.state(false, "*", "customer.booking.publish.non-published-passengers");
		else
			for (Passenger passenger : passengers)
				if (passenger.isDraftMode()) {
					super.state(false, "*", "customer.booking.publish.non-published-passengers");
					break;
				}

		// Validación de booking records
		Collection<BookingRecord> bookingRecords = this.repository.findBookingRecordsByBookingId(id);
		for (BookingRecord br : bookingRecords)
			if (br.isDraftMode()) {
				super.state(false, "*", "customer.booking.publish.non-published-bookingrecords");
				break;
			}

		// Validación de lastCardNible
		if (booking.getLastCardNibble() == null || booking.getLastCardNibble().trim().isEmpty())
			super.state(false, "lastCardNibble", "customer.booking.form.error.last-card-nible-required");

		// Validación de locatorCode duplicados
		if (booking.getLocatorCode() != null) {
			Booking existing = this.repository.findByLocatorCode(booking.getLocatorCode());
			if (existing != null && existing.getId() != booking.getId())
				super.state(false, "locatorCode", "customer.booking.form.error.duplicated-locator-code");
		}
	}

	@Override
	public void perform(final Booking booking) {
		booking.setDraftMode(false);
		booking.setPurchaseMoment(MomentHelper.getCurrentMoment());
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
