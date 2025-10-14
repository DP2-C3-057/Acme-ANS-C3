
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
public class CustomerBookingRecordPublishService extends AbstractGuiService<Customer, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status = false;

		try {
			int brId = super.getRequest().getData("id", int.class);
			BookingRecord br = this.repository.findBookingRecordById(brId);
			Customer customer = br == null ? null : br.getBooking().getCustomer();

			Integer bookingId = super.getRequest().getData("booking", int.class);
			Integer passengerId = super.getRequest().getData("passenger", int.class);

			Booking booking = bookingId == null ? null : this.repository.findBookingById(bookingId);
			Passenger passenger = passengerId == null ? null : this.repository.findPassengerById(passengerId);

			status = br != null && booking != null && passenger != null && booking.getCustomer().equals(passenger.getCustomer()) && booking.getCustomer().equals(br.getBooking().getCustomer()) && br.isDraftMode() && booking.isDraftMode()
				&& super.getRequest().getPrincipal().hasRealm(customer);

		} catch (Exception e) {
			status = false;
		}

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
		super.bindObject(br, "booking", "passenger");
	}

	@Override
	public void validate(final BookingRecord br) {
		if (br.getPassenger() != null && br.getPassenger().isDraftMode())
			super.state(false, "passenger", "customer.booking-record.error.passenger-draft");

		//Validacion de relacion ya existnte
		if (br.getBooking() != null && br.getPassenger() != null) {
			BookingRecord existing = this.repository.findByBookingIdAndPassengerId(br.getBooking().getId(), br.getPassenger().getId());
			if (existing != null && existing.getId() != br.getId())
				super.state(false, "passenger", "customer.booking-record.form.error.duplicate");
		}
	}

	@Override
	public void perform(final BookingRecord br) {
		br.setDraftMode(false);
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
