
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
		int customerId = super.getRequest().getPrincipal().getActiveRealm().getId();

		Integer bookingId = super.getRequest().getData("booking", int.class);
		Integer passengerId = super.getRequest().getData("passenger", int.class);

		var booking = this.repository.findDraftBookingByIdAndCustomerId(bookingId, customerId);
		var passenger = this.repository.findPassengerByIdAndCustomerId(passengerId, customerId);

		if (booking == null)
			throw new RuntimeException("Security violation: attempted to assign booking not owned by customer.");

		if (passenger == null)
			throw new RuntimeException("Security violation: attempted to assign passenger not owned by customer.");

		br.setBooking(booking);
		br.setPassenger(passenger);
	}

	@Override
	public void validate(final BookingRecord br) {
		if (br.getBooking() != null && br.getPassenger() != null) {
			final BookingRecord existing = this.repository.findByBookingIdAndPassengerId(br.getBooking().getId(), br.getPassenger().getId());
			super.state(existing == null, "passenger", "customer.booking-record.form.error.duplicate");

			super.state(br.getBooking().isDraftMode(), "booking", "customer.booking-record.form.error.booking-published");
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

		final Integer customer = super.getRequest().getPrincipal().getActiveRealm().getId();

		bookings = this.repository.findDraftBookingsByCustomerId(customer);
		bookingChoice = SelectChoices.from(bookings, "locatorCode", br.getBooking());

		passengers = this.repository.findAllPassengersByCustomerId(customer);
		passengerChoice = SelectChoices.from(passengers, "fullName", br.getPassenger());

		dataset = super.unbindObject(br, "booking", "passenger", "draftMode");
		dataset.put("bookingChoice", bookingChoice);
		dataset.put("passengerChoice", passengerChoice);

		super.getResponse().addData(dataset);
	}

}
