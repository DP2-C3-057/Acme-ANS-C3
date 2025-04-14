
package acme.features.customer.bookingRecord;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;

import acme.client.components.models.Dataset;
import acme.client.services.AbstractGuiService;
import acme.client.services.GuiService;
import acme.entities.bookings.BookingRecord;
import acme.realms.customers.Customer;

// Lista todos los pasajeros del customer
@GuiService
public class CustomerBookingRecordListService extends AbstractGuiService<Customer, BookingRecord> {

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
		Collection<BookingRecord> brs;
		int brId;

		brId = super.getRequest().getPrincipal().getActiveRealm().getId();
		brs = this.repository.findBookingRecordsByCustomerId(brId);

		super.getBuffer().addData(brs);
	}

	@Override
	public void unbind(final BookingRecord br) {
		Dataset dataset;

		dataset = super.unbindObject(br, "booking.locatorCode", "passenger.fullName", "draftMode");

		super.getResponse().addData(dataset);
	}

}
