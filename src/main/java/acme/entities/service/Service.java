
package acme.entities.service;

import javax.persistence.Column;
import javax.persistence.Entity;

import acme.client.components.basis.AbstractEntity;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import acme.client.components.validation.ValidUrl;
import acme.constraints.ValidPromoCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Service extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				name;

	@Mandatory
	@ValidUrl
	@Automapped
	private String				imageUrl;

	@Mandatory
	@ValidNumber(min = 0)
	@Automapped
	private Double				averageUsageTime;

	@Optional
	@ValidPromoCode
	@Column(unique = true)
	@Automapped
	private String				promoCode;

	@Optional
	@ValidNumber(min = 0)
	@Automapped
	private Double				discountAmount;

}
