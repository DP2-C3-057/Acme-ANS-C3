
package acme.entities.claims;

import java.util.Date;

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
import acme.client.components.validation.ValidNumber;
import acme.client.components.validation.ValidString;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class TrackingLog extends AbstractEntity {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	@Mandatory
	@ValidMoment(past = true)
	@Temporal(TemporalType.TIMESTAMP)
	private Date				lastUpdateMoment;

	@Mandatory
	@ValidString(max = 50)
	@Automapped
	private String				step;

	@Mandatory
	@ValidNumber(min = 0, max = 100)
	@Automapped
	private Double				resolutionPercentage;

	@Mandatory
	@Valid
	@Automapped
	private Boolean				indicator;

	@Optional
	@ValidString
	@Automapped
	private String				resolution;

	//Relationships ----------------------------------

	@Mandatory
	@Valid
	@ManyToOne(optional = false)
	private Claim				claim;


	// Derived attributes ------------------------------------------------------
	@Transient
	public boolean isIndicatorTrue() {
		boolean indicatorValue;
		indicatorValue = this.getIndicator() == true && !this.getResolution().isBlank() || this.getIndicator() == false && this.getResolution().isBlank();
		return indicatorValue;
	}
}
