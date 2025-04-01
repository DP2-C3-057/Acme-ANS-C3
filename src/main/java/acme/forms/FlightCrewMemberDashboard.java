
package acme.forms;

import java.util.List;
import java.util.Map;

import acme.client.components.basis.AbstractForm;
import acme.entities.flightAssignments.CurrentStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FlightCrewMemberDashboard extends AbstractForm {

	// Serialisation version --------------------------------------------------

	private static final long					serialVersionUID	= 1L;

	// Attributes -------------------------------------------------------------

	private List<String>						lastFiveDestinations;
	private Integer								lowSeverityIncidentLegs;
	private Integer								midSeverityIncidentLegs;
	private Integer								highSeverityIncidentLegs;
	private List<String>						assignedCrewMembers;
	private Map<CurrentStatus, List<String>>	assignmentsByFlightStatus;
	private Double								avgFlightAssignments;
	private Integer								minFlightAssignments;
	private Integer								maxFlightAssignments;
	private Double								flightAssignmentDeviation;

	// Derived attributes -----------------------------------------------------

	// Relationships ----------------------------------------------------------

}
