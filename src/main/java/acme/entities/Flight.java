
package acme.entities;

import javax.persistence.Entity;
import javax.validation.constraints.Max;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.datatypes.Money;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoney;
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
	@Automapped
	@Max(50)
	private String				tag;

	@Mandatory
	@Automapped
	private Boolean				selfTransfer;

	@Mandatory
	@ValidMoney
	@Automapped
	private Money				cost;

	@Optional
	@Automapped
	@Max(255)
	private String				description;

	// Relationships ----------------------------------------------------------
	/**
	 * @Mandatory
	 * @Valid
	 * @RELACION
	 *           private Leg leg.scheduledDeparture;
	 * @Mandatory
	 * @Valid
	 * @RELACION
	 *           private Leg leg.scheduledArrival;
	 *           Tambien: origen, destino, number layovers
	 **/
}
