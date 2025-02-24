
package acme.entities.flights;

import javax.persistence.Entity;
import javax.validation.Valid;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Flight extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				tag;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				selfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@ValidString(max = 255)
	@Automapped
	private String				description;

	// Derived attributes -----------------------------------------------------
	/*
	 * @Transient
	 * public Date scheduledDeparture() {
	 * }
	 * 
	 * @Transient
	 * public Date scheduledArrival() {
	 * 
	 * }
	 * 
	 * @Transient
	 * public String originCity() {
	 * 
	 * }
	 * 
	 * @Transient
	 * public String destinationCity() {
	 * 
	 * }
	 */
	// Relationships ----------------------------------------------------------

}
