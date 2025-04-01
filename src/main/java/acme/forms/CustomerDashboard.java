
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.bookings.TravelClass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long			serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private List<String>				lastFiveDestinations;
	private Double						moneySpendLastYear;
	private Map<TravelClass, Integer>	numBookingsByTravelClass;

	private Double						bookingCostLastFiveYearsCount;
	private Double						bookingCostLastFiveYearsAverage;
	private Double						bookingCostLastFiveYearsMin;
	private Double						bookingCostLastFiveYearsMax;
	private Double						bookingCostLastFiveYearsStandardDesviation;

	private Integer						numPassengersCount;
	private Double						numPassengersAverage;
	private Integer						numPassengersMin;
	private Integer						numPassengersMax;
	private Double						numPassengersStandardDesviation;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
