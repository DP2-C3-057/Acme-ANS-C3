
package acme.features.customer.bookingRecord;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.realms.customers.Customer;

@GuiService
public class CustomerBookingRecordDeleteService extends AbstractGuiService<Customer, BookingRecord> {
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
		;
	}

	@Override
	public void perform(final BookingRecord br) {
		this.repository.delete(br);
	}

	@Override
	public void unbind(final BookingRecord br) {
		Dataset dataset;

		dataset = super.unbindObject(br, "booking", "passenger", "draftMode");

		super.getResponse().addData(dataset);
	}
}
