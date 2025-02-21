
package acme.realms;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Version;

import acme.client.components.basis.AbstractRole;
import acme.client.components.mappings.Automapped;
import acme.client.components.validation.Mandatory;
import acme.client.components.validation.Optional;
import acme.client.components.validation.ValidMoment;
import acme.client.components.validation.ValidUrl;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Manager extends AbstractRole {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE)
	@Column(unique = true)
	//@Pattern("^[A-Z]{2-3}\d{6}$")
	private int					id;

	@Version
	private int					version;

	@Mandatory
	@Automapped
	private int					experienceYears;

	@Mandatory
	@ValidMoment(past = true)
	private Date				birth;

	@Optional
	@ValidUrl
	@Automapped
	private String				picture;
}
