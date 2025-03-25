
package acme.entities.bookings;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.helpers.SpringHelper;
import acme.constraints.ValidBooking;
import acme.constraints.ValidLastCardNibble;
import acme.constraints.ValidLocatorCode;
import acme.entities.flights.Flight;
import acme.realms.customers.Customer;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@ValidBooking
public class Booking extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidLocatorCode
	@Column(unique = true)
	private String				locatorCode;

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				purchaseMoment;

	@Mandatory
	@Valid
	@Automapped
	private TravelClass			travelClass;

	@Optional
	@ValidLastCardNibble
	@Automapped
	private String				lastCardNibble;

	@Mandatory
	// HINT: @Valid by default.
	@Automapped
	private boolean				draftMode;

	// Relationships ----------------------------------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Customer			customer;

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;

	// Derived attributes -----------------------------------------------------


	//Debe devolver tipo Money
	@Transient
	public Double getBookingCost() {
		BookingRepository repository = SpringHelper.getBean(BookingRepository.class);

		Double flightPrice = this.flight != null ? this.flight.getCost().getAmount() : 0.0;

		Integer passengerCount = repository.countPassengersByLocatorCode(this.locatorCode);
		passengerCount = passengerCount != null ? passengerCount : 0;

		return passengerCount * flightPrice;
	}

}
