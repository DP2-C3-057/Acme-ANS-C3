
package acme.entities.legs;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.entities.flights.Flight;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Leg extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(pattern = "^[A-Z]{2}[0-9]{4}$")
	@Column(unique = true)
	private String				flightNumber;

	@Mandatory
	//@ValidMoment(max = )
	@Automapped
	private Date				scheduledDeparture;

	@Mandatory
	//@ValidMoment(min = )
	@Automapped
	private Date				scheduledArrival;

	@Mandatory
	@ValidNumber
	@Automapped
	private int					duration;

	@Mandatory
	@Valid
	@Automapped
	private LegStatus			status;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------
	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Flight				flight;
}
