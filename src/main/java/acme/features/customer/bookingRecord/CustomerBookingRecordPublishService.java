
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.realms.customers.Customer;

@GuiService
public class CustomerBookingRecordPublishService extends AbstractGuiService<Customer, BookingRecord> {
	// Internal state ---------------------------------------------------------

	@Autowired
	private CustomerBookingRecordRepository repository;

	// AbstractGuiService interface -------------------------------------------


	@Override
	public void authorise() {
		boolean status;
		int brId;
		BookingRecord br;
		Customer customer;

		brId = super.getRequest().getData("id", int.class);
		br = this.repository.findBookingRecordById(brId);
		customer = br == null ? null : br.getBooking().getCustomer();
		status = br != null && br.isDraftMode() && super.getRequest().getPrincipal().hasRealm(customer);

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
	}

	@Override
	public void perform(final BookingRecord br) {
		br.setDraftMode(false);
		this.repository.save(br);
	}

	@Override
	public void unbind(final BookingRecord br) {
		Dataset dataset;

		dataset = super.unbindObject(br, "booking", "passenger", "draftMode");
		super.getResponse().addData(dataset);
	}
}
