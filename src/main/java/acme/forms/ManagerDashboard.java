
package acme.forms;

import acme.client.components.basis.AbstractForm;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ManagerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long	serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	int							rankingPosition;
	int							yearsToRetire;
	Double						onTimeDelayedLegsRatio;
	String						mostPopularAirport;
	String						lessPopularAirport;
	int							onTimeLegs;
	int							delayedLegs;
	int							cancelledLegs;
	int							landedLegs;
	Double						averageFlightCost;
	Double						minimumFlightCost;
	Double						maximumFlightCost;
	Double						standardDeviationCost;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
