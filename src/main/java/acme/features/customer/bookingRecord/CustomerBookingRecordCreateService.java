
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
public class CustomerBookingRecordCreateService extends AbstractGuiService<Customer, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		super.getResponse().setAuthorised(true);
	}

	@Override
	public void load() {
		BookingRecord br;

		br = new BookingRecord();
		br.setDraftMode(true);

		super.getBuffer().addData(br);
	}

	@Override
	public void bind(final BookingRecord br) {
		super.bindObject(br, "booking", "passenger");
	}

	@Override
	public void validate(final BookingRecord br) {
		if (br.getBooking() != null && br.getPassenger() != null) {
			BookingRecord existing = this.repository.findByBookingIdAndPassengerId(br.getBooking().getId(), br.getPassenger().getId());
			super.state(existing == null, "passenger", "customer.booking-record.form.error.duplicate");
		}
	}

	@Override
	public void perform(final BookingRecord br) {
		this.repository.save(br);
	}

	@Override
	public void unbind(final BookingRecord br) {
		Dataset dataset;
		SelectChoices bookingChoice;
		Collection<Booking> bookings;
		SelectChoices passengerChoice;
		Collection<Passenger> passengers;

		Integer customer;
		customer = super.getRequest().getPrincipal().getActiveRealm().getId();

		bookings = this.repository.findAllBookingsByCustomerId(customer);
		bookingChoice = SelectChoices.from(bookings, "locatorCode", br.getBooking());

		passengers = this.repository.findAllPassengersByCustomerId(customer);
		passengerChoice = SelectChoices.from(passengers, "fullName", br.getPassenger());

		dataset = super.unbindObject(br, "booking", "passenger", "draftMode");
		dataset.put("bookingChoice", bookingChoice);
		dataset.put("passengerChoice", passengerChoice);

		super.getResponse().addData(dataset);
	}

}
