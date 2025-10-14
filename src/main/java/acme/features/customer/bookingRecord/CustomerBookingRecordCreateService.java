
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
		boolean status = false;
		String method = super.getRequest().getMethod();

		if (method.equals("GET"))
			status = super.getRequest().getPrincipal().hasRealmOfType(Customer.class);
		else if (method.equals("POST")) {
			Integer bookingId, passengerId;
			Booking booking;
			Passenger passenger;
			Customer customer;

			try {
				bookingId = super.getRequest().getData("booking", int.class);
				passengerId = super.getRequest().getData("passenger", int.class);
			} catch (final Exception e) {
				super.getResponse().setAuthorised(false);
				return;
			}

			booking = bookingId == null ? null : this.repository.findBookingById(bookingId);
			passenger = passengerId == null ? null : this.repository.findPassengerById(passengerId);
			customer = booking == null ? null : booking.getCustomer();

			status = booking != null && passenger != null && booking.getCustomer().equals(passenger.getCustomer()) && booking.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);
		}

		super.getResponse().setAuthorised(status);
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
