
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.components.views.SelectChoices;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.realms.customers.Customer;

@GuiService
public class CustomerBookingRecordUpdateService extends AbstractGuiService<Customer, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int masterId = super.getRequest().getData("id", int.class);
		BookingRecord br = this.repository.findBookingRecordById(masterId);
		Customer customer = br == null ? null : br.getBooking().getCustomer();

		status = br != null && br.isDraftMode() && br.getBooking() != null && br.getBooking().isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

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
			var existing = this.repository.findByBookingIdAndPassengerId(br.getBooking().getId(), br.getPassenger().getId());
			if (existing != null && existing.getId() != br.getId())
				super.state(false, "passenger", "customer.booking-record.form.error.duplicate");

			super.state(br.getBooking().isDraftMode(), "booking", "customer.booking-record.form.error.booking-published");

			int activeCustomerId = super.getRequest().getPrincipal().getActiveRealm().getId();
			int passengerCustomerId = br.getPassenger().getCustomer().getId();

			super.state(activeCustomerId == passengerCustomerId, "passenger", "customer.booking-record.form.error.passenger-not-owned");
		}
	}

	@Override
	public void perform(final BookingRecord br) {
		this.repository.save(br);
	}

	@Override
	public void unbind(final BookingRecord br) {
		Dataset dataset;
		final Integer customer = super.getRequest().getPrincipal().getActiveRealm().getId();

		var bookings = this.repository.findDraftBookingsByCustomerId(customer);
		var bookingChoice = SelectChoices.from(bookings, "locatorCode", br.getBooking());

		var passengers = this.repository.findAllPassengersByCustomerId(customer);
		var passengerChoice = SelectChoices.from(passengers, "fullName", br.getPassenger());

		dataset = super.unbindObject(br, "booking", "passenger", "draftMode");
		dataset.put("bookingChoice", bookingChoice);
		dataset.put("passengerChoice", passengerChoice);
		super.getResponse().addData(dataset);
	}

}
