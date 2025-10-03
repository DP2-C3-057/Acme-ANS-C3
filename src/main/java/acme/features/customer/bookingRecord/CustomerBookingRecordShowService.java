
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.Booking;
import acme.entities.bookings.BookingRecord;
import acme.entities.passengers.Passenger;
import acme.realms.customers.Customer;

@GuiService
public class CustomerBookingRecordShowService extends AbstractGuiService<Customer, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId;
		BookingRecord br;
		Customer customer;

		masterId = super.getRequest().getData("id", int.class);
		br = this.repository.findBookingRecordById(masterId);
		customer = br == null ? null : br.getBooking().getCustomer();
		status = super.getRequest().getPrincipal().hasRealm(customer) && br != null;

		super.getResponse().setAuthorised(status);
	}

	@Override
	public void load() {
		BookingRecord br;
		int id;

		id = super.getRequest().getData("id", int.class);
		br = this.repository.findBookingRecordById(id);

		super.getBuffer().addData(br);
	}

	@Override
	public void unbind(final BookingRecord br) {
		Dataset dataset;
		SelectChoices bookingChoice;
		Collection<Booking> bookings;
		SelectChoices passengerChoice;
		Collection<Passenger> passengers;

		final Integer customer = super.getRequest().getPrincipal().getActiveRealm().getId();

		final java.util.List<Booking> draftBookings = new java.util.ArrayList<>(this.repository.findDraftBookingsByCustomerId(customer));

		if (br.getBooking() != null && !br.getBooking().isDraftMode() && draftBookings.stream().noneMatch(b -> b.getId() == br.getBooking().getId()))
			draftBookings.add(0, br.getBooking());

		bookings = draftBookings;
		bookingChoice = SelectChoices.from(bookings, "locatorCode", br.getBooking());

		passengers = this.repository.findAllPassengersByCustomerId(customer);
		passengerChoice = SelectChoices.from(passengers, "fullName", br.getPassenger());

		dataset = super.unbindObject(br, "booking.locatorCode", "passenger.fullName", "draftMode");
		dataset.put("bookingChoice", bookingChoice);
		dataset.put("passengerChoice", passengerChoice);

		super.getResponse().addData(dataset);
	}

}
