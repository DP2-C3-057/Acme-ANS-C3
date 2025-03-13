
package acme.forms;

import acme.client.components.basis.AbstractForm;
import acme.entities.airports.Airport;
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
	Airport						mostPopularAirport;
	Airport						lessPopularAirport;
	int							numbLegPerStatus;
	Double						averageCostFlight;
	Double						minimumCostFlight;
	Double						maximumCostFlight;
	Double						standartDeviationCost;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
